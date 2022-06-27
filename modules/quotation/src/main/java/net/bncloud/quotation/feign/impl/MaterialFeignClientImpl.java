package net.bncloud.quotation.feign.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import net.bncloud.common.api.R;
import net.bncloud.common.util.CollectionUtil;
import net.bncloud.quotation.entity.MaterialInfo;
import net.bncloud.quotation.service.MaterialInfoService;
import net.bncloud.quotation.service.api.dto.MaterialInfoDTO;
import net.bncloud.quotation.service.api.dto.MaterialInfoDetailDTO;
import net.bncloud.quotation.service.api.feign.MaterialFeignClient;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author lijiaju
 * @date 2022/2/22 9:37
 */
@RestController
@AllArgsConstructor
public class MaterialFeignClientImpl implements MaterialFeignClient {

    private MaterialInfoService materialInfoService;

    @Override
    public void syncMaterialDataSaveOrUpdate(List<MaterialInfoDTO> materialInfoDTOS) {
        materialInfoService.syncMaterialDataSaveOrUpdate(materialInfoDTOS);
    }

    @Override
    public R<MaterialInfoDetailDTO> getMaterialDetailByCode(String code) {
        return R.data(materialInfoService.getMaterialDetailByCode(code));
    }

    @Override
    public R<List<String>> selectMaterialCodeByGroupIds(List<Long> materialGroupIds) {
        List<MaterialInfo> list = materialInfoService.list(Wrappers
                .<MaterialInfo>lambdaQuery()
                .in(MaterialInfo::getMaterialGroupId, materialGroupIds));

        //为空 当前排程供应商有关联分组,但是这个分组下没有对应的物料
        if(CollectionUtil.isEmpty(list)){
            return R.data( new ArrayList<>());
        }

        List<String> materialCodeList = list.stream().map(item -> item.getMaterialCode()).distinct().collect(Collectors.toList());
        return R.data(materialCodeList);
    }
}
