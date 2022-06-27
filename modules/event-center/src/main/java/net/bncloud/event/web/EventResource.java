package net.bncloud.event.web;

import net.bncloud.api.event.PublishEventRequest;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.security.LoginInfo;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.event.domain.EventType;
import net.bncloud.event.domain.vo.EventTypeVo;
import net.bncloud.event.domain.vo.RoleVO;
import net.bncloud.event.service.EventService;
import net.bncloud.event.service.GrantRoleDTO;
import net.bncloud.event.service.query.EventTypeQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/event-center/events")
public class EventResource {

    private final EventService eventService;

    public EventResource(EventService eventService) {
        this.eventService = eventService;
    }

    /**
     * 发布事件
     */
    @PostMapping("/publish")
    public R<Void> addEvent(PublishEventRequest request) {
        eventService.publishEvent(request);
        return R.success();
    }

    @PostMapping("/types")
    public R<Page<EventType>> types(@RequestBody QueryParam<EventTypeQuery> query, Pageable pageable) {

        Pageable pageablePage = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),Sort.by(new Sort.Order(Sort.Direction.ASC,"modularType")));


        return R.data(eventService.eventTypePageQuery(query.getParam(), pageablePage));
    }

    @PostMapping("/grant")
    public R<Void> grantRoles(@RequestBody GrantRoleDTO dto) {
        eventService.grant(dto.getId(), dto.getRoles());
        return R.success();
    }

    @PostMapping("/create")
    public R<Void> create(@RequestBody EventType eventType) {
        Optional<LoginInfo> loginInfo = SecurityUtils.getLoginInfo();
        eventType.setCreatedBy(loginInfo.get().getId());
        eventService.create(eventType);
        return R.success();
    }

    @PutMapping("/update")
    public R<Void> update(@RequestBody EventTypeVo eventType) {

        eventService.update(eventType);
        return R.success();
    }


    @PutMapping("/changeState/{id}")
    @Transactional
    public R<Void> status(@PathVariable Long id) {
        eventService.switchStatus(id);
        return R.success();
    }

    @DeleteMapping("/delete/{id}")
    public R<Void> delete(@PathVariable Long id) {
        eventService.delete(id);
        return R.success();
    }
    @GetMapping("/getById/{id}")
    public R<EventTypeVo> getById(@PathVariable Long id)
    {
        Optional<EventType> eventType = eventService.getById(id);
        List<Long> list = new ArrayList<Long>();
        for (RoleVO role : eventType.get().getRoles()) {
            list.add(role.getRoleId());
        }
        EventTypeVo eventTypeVo = BeanUtil.copyWithConvert(eventType.get(), EventTypeVo.class);
        eventTypeVo.setRolesList(list);
        return R.data(eventTypeVo);
    }
}
