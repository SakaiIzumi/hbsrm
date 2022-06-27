package net.bncloud.delivery.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseService;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.delivery.entity.FactoryVacation;
import net.bncloud.delivery.enums.FactoryBelongTypeEnum;
import net.bncloud.delivery.param.AutoSubscribeParam;
import net.bncloud.delivery.param.FactoryInfoParam;
import net.bncloud.delivery.param.FactoryVacationParam;
import net.bncloud.delivery.param.FactoryVacationSetParam;
import net.bncloud.delivery.vo.FactoryVacationImportVo;
import net.bncloud.delivery.vo.FactoryVacationVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * 工厂假期 service 接口
 *
 * @author liyh
 * @since 2021-05-16
 */
public interface FactoryVacationService extends BaseService<FactoryVacation> {

    /**
     *采购/供应  列表查询节假日
     **/
    IPage<FactoryVacationVo> selectListPage(IPage<FactoryVacation> toPage, QueryParam<FactoryVacationParam> queryParam);

    void batchDeleteVacation(List<Long> ids);

    void buildHolidayForSave(List<FactoryVacationParam> factoryVacationParamList );

    void autoVacation(AutoSubscribeParam param);

    void deleteAllVacationFromFactoryId(List<Long> ids);

    void buildVacation(FactoryVacationSetParam param, Map<FactoryInfoParam, List<Date>> factoryInfoParamListHashMap,Map<FactoryInfoParam, List<FactoryVacation>> factoryInfoParamListUpdateHashMap);

    void vacationSave(FactoryVacationSetParam param);

    void importVacation(FactoryVacationImportVo vo);

    void vacationButton(FactoryVacationParam param);

    void vacationUpdate(FactoryVacationSetParam param);

    void clearOverdueDate();

    void completionVacation();

    Boolean confirmCover(FactoryVacationSetParam param);

    FactoryVacationImportVo importVacationConflictMessage(MultipartFile file,String workBench);

    FactoryVacationImportVo importSupplierVacationConflictMessage(MultipartFile file,String workBench);

    /**
     * 列表查询假期根据采购方code集合
     * @param purchaserCodeColl
     * @return
     */
    List<FactoryVacation> listByBelongCodeCodeColl(Collection<String> purchaserCodeColl, FactoryBelongTypeEnum factoryBelongTypeEnum);
}
