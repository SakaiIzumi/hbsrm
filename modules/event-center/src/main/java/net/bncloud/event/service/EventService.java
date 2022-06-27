package net.bncloud.event.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.bncloud.api.event.PublishEventRequest;
import net.bncloud.common.exception.BizException;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.event.domain.EventDetail;
import net.bncloud.event.domain.EventDetailData;
import net.bncloud.event.domain.EventType;
import net.bncloud.event.domain.vo.EventTypeVo;
import net.bncloud.event.domain.vo.FromUser;
import net.bncloud.event.domain.vo.RoleVO;
import net.bncloud.event.domain.vo.SourcesCompany;
import net.bncloud.event.event.EventDataStored;
import net.bncloud.event.exception.EventResultCode;
import net.bncloud.event.publisher.DefaultEventPublisher;
import net.bncloud.event.repository.EventDetailDataRepository;
import net.bncloud.event.repository.EventDetailRepository;
import net.bncloud.event.repository.EventTypeRepository;
import net.bncloud.event.service.query.EventTypeQuery;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
public class EventService implements ApplicationEventPublisherAware {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationEventPublisherAware.class);
    public ApplicationEventPublisher applicationEventPublisher;

    private final EventTypeRepository eventTypeRepository;
    private final EventDetailRepository eventDetailRepository;
    private final EventDetailDataRepository eventDetailDataRepository;

    public EventService(EventTypeRepository eventTypeRepository, EventDetailRepository eventDetailRepository,
                        EventDetailDataRepository eventDetailDataRepository) {
        this.eventTypeRepository = eventTypeRepository;
        this.eventDetailRepository = eventDetailRepository;
        this.eventDetailDataRepository = eventDetailDataRepository;
    }

    @Transactional
    public void publishEvent(PublishEventRequest request) {
        String eventCode = request.getEventCode();
        JSONObject json = JSON.parseObject(request.getEventData().toString());
        LOGGER.info("正在触发事件={}",eventCode);
        if(json.getLong("orgId") == null||json.getLong("orgId").longValue() == 0L){
            LOGGER.error("orgId不存在,事件触发失败--------------------------------------");
            return;
        }
        if(json.getString("supplierCode") == null||StringUtils.isEmpty(json.getString("supplierCode"))){
            LOGGER.error("SupplierCode不存在,事件触发失败---------------------------------");
            return;
        }

        Optional<EventType> typeOptional = eventTypeRepository.findOneByCode(eventCode);
        if (!typeOptional.isPresent()) {
            throw new BizException(EventResultCode.EVENT_TYPE_NOT_FOUND);
        }
        EventType eventType = typeOptional.get();

        EventDetail detail = new EventDetail();
        detail.setEventType(eventType);
        detail.setOrgId(json.getLong("orgId").longValue());
        detail.setSupplierCode(json.getString("supplierCode"));
        detail.setUser(FromUser.of(request.getUserId(), request.getUserName(),json.getLong("orgId")));
        detail.setSmsMsgType(json.getInteger("smsMsgType"));
        detail.setSmsParams(json.getString("smsParams"));
        detail.setSmsTempCode(json.getString("smsTempCode"));
        EventDetail eventDetail = eventDetailRepository.save(detail);
        EventDetailData data = new EventDetailData();
        data.setEventDetail(eventDetail);
        data.setData(request.getEventData().toString());
        //eventDetail = eventDetailRepository.save(eventDetail);
        EventDetailData savedData = eventDetailDataRepository.save(data);


        eventDetail.setDetailData(savedData);
        SourcesCompany sources = new SourcesCompany();
        sources.setSourcesCode(request.getSourcesCode());
        sources.setSourcesName(request.getSources());

        applicationEventPublisher.publishEvent(new EventDataStored(this, eventDetail,sources));
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional(readOnly = true)
    public Page<EventType> eventTypePageQuery(EventTypeQuery param, Pageable pageable) {
        return eventTypeRepository.findAll((Specification<EventType>) (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (param == null) {
                return predicate;
            }
            if (param.getId() != null) {
                predicate.getExpressions().add(criteriaBuilder.equal(root.get("id"), param.getId()));
                return predicate;
            }
            if (StringUtils.isNotBlank(param.getCode())) {
                predicate.getExpressions().add(criteriaBuilder.like(root.get("code"), "%" + param.getCode() + "%"));
            }
            if (StringUtils.isNotBlank(param.getName())) {
                predicate.getExpressions().add(criteriaBuilder.like(root.get("name"), "%" + param.getName() + "%"));
            }
            if (StringUtils.isNotBlank(param.getModule())) {
                predicate.getExpressions().add(criteriaBuilder.like(root.get("module"), "%" + param.getModule() + "%"));
            }
            if (StringUtils.isNotBlank(param.getScene())) {
                predicate.getExpressions().add(criteriaBuilder.like(root.get("scene"), "%" + param.getScene() + "%"));
            }
            if (StringUtils.isNotBlank(param.getDescription())) {
                predicate.getExpressions().add(criteriaBuilder.like(root.get("description"), "%" + param.getDescription() + "%"));
            }
            return predicate;
        }, pageable);
    }

    public EventType create(EventType type) {
        if (type.getId() != null) {
            throw new IllegalArgumentException("新增类型id必须为空"); // TODO
        }
        Optional<EventType> exist = eventTypeRepository.findOneByCode(type.getCode());
        if (exist.isPresent()) {
            throw new IllegalArgumentException("编码重复"); // TODO
        }
        return eventTypeRepository.save(type);
    }

    public void update(EventTypeVo eventType) {

        if (eventType.getId() == null) {
            throw new IllegalArgumentException("修改类型时id不能为空"); // TODO
        }
        Optional<EventType> exist = eventTypeRepository.findById(eventType.getId());
        if (!exist.isPresent()) {
            throw new IllegalArgumentException("记录不存在"); // TODO
        }

        List<RoleVO> roles= new ArrayList<RoleVO>();
        for (Long l : eventType.getRolesList()) {
            RoleVO roleVO = new RoleVO();
            roleVO.setRoleId(l);
            roles.add(roleVO);
        }
        EventType eventType1 = exist.get();
        eventType1.setName(eventType.getName());
        eventType1.setRoles(roles);
        eventType1.setDisabled(eventType.isDisabled());
        eventTypeRepository.save(eventType1);


    }

    public void grant(Long id, List<RoleVO> roles) {
        if (roles != null) {
            roles.forEach(roleVO -> {
                roleVO.setRelateAt(Instant.now());
            });
        }
        eventTypeRepository.findById(id).ifPresent(eventType -> {
            eventType.setRoles(roles);
            eventTypeRepository.save(eventType);
        });
    }

    public void disable(Long id) {
        eventTypeRepository.findById(id).ifPresent(eventType -> {
            eventType.setDisabled(true);
            eventTypeRepository.save(eventType);
        });
    }

    public void enable(Long id) {
        eventTypeRepository.findById(id).ifPresent(eventType -> {
            eventType.setDisabled(false);
            eventTypeRepository.save(eventType);
        });
    }

    public Optional<EventType> getById(Long id) {
        Optional<EventType> eventType = eventTypeRepository.findById(id);
        return  eventType;


    }

    public void delete(Long id) {

        eventTypeRepository.deleteById(id);
    }


    public void switchStatus(Long id) {
        eventTypeRepository.findById(id).ifPresent(eventType -> {
            eventType.setDisabled(!eventType.isDisabled());
            eventTypeRepository.save(eventType);
        });
    }
}
