package net.bncloud.quotation.service;

import net.bncloud.quotation.entity.MaterialInfo;
import net.bncloud.quotation.service.api.dto.MaterialInfoDTO;
import net.bncloud.quotation.service.api.dto.MaterialInfoDetailDTO;
import net.bncloud.quotation.vo.MaterialInfoVo;
import net.bncloud.quotation.param.MaterialInfoParam;
import net.bncloud.base.BaseService;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.common.base.domain.QueryParam;

import java.util.List;

/**
 * <p>
 * 物料信息表 服务类
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
public interface MaterialInfoService extends BaseService<MaterialInfo> {

		/**
         * 自定义分页
         * @param page
		 * @param pageParam
         * @return
         */
		IPage<MaterialInfo> selectPage(IPage<MaterialInfo> page, QueryParam<MaterialInfoParam> pageParam);


		void syncMaterialDataSaveOrUpdate(List<MaterialInfoDTO> materialInfoDTOS);

		MaterialInfo getOneBySourcesId(Long sourceId);

		MaterialInfo getOneByMaterialCode(String materialCode);

    void batchDeleteByIdList(List<Long> idList);

	MaterialInfoDetailDTO getMaterialDetailByCode(String code);
}
