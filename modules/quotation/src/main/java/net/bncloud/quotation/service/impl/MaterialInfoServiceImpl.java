package net.bncloud.quotation.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.*;
import net.bncloud.base.BaseUserEntity;
import net.bncloud.common.exception.ApiException;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.common.util.DateUtil;
import net.bncloud.quotation.entity.MaterialInfo;
import net.bncloud.quotation.mapper.MaterialInfoMapper;
import net.bncloud.quotation.service.MaterialInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.quotation.service.api.dto.MaterialInfoDTO;
import net.bncloud.quotation.param.MaterialInfoParam;
import net.bncloud.quotation.service.api.dto.MaterialInfoDetailDTO;
import net.bncloud.support.Condition;
import net.bncloud.utils.AuthUtil;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.common.base.domain.QueryParam;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 物料信息表 服务实现类
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
@Service
public class MaterialInfoServiceImpl extends BaseServiceImpl<MaterialInfoMapper, MaterialInfo> implements MaterialInfoService {

		@Override
		public IPage<MaterialInfo> selectPage(IPage<MaterialInfo> page, QueryParam<MaterialInfoParam> pageParam) {
			// 若不使用mybatis-plus自带的分页方法，则不会自动带入tenantId，所以我们需要自行注入
			return page.setRecords(baseMapper.selectListPage(page, pageParam));
		}

	/**
	 * 因为ERP那边组织不固定 每个组织下面会有一个物料编码一样的物料 同步拉取的时候 使用物料编码进行去重 跟业务沟通过可以只根据物料编码去重
 	 * @param materialInfoDTOS 需要批量保存或者插入的对象
	 */
	@Override
	public void syncMaterialDataSaveOrUpdate(List<MaterialInfoDTO> materialInfoDTOS) {
		//去重
		List<MaterialInfoDTO> distinctDTOS = materialInfoDTOS.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(()
				-> new TreeSet<>(Comparator.comparing(MaterialInfoDTO :: getMaterialCode))), ArrayList::new));

		//批量保存或者插入 去数据库通过物料编码查询去重
		try (SqlSession batchSqlSession = sqlSessionBatch()) {
			for (MaterialInfoDTO entityDTO : distinctDTOS) {
				MaterialInfo materialInfo = new MaterialInfo();
				BeanUtil.copyProperties(entityDTO, materialInfo);

				if (!Objects.isNull(materialInfo.getMaterialCode())) {
					MaterialInfo databaseMaterialInfoForCode = this.getOneByMaterialCode(materialInfo.getMaterialCode());
					MaterialInfo databaseMaterialInfoForId=null;
					//虽然物料编码是唯一的，但是可以修改，只要唯一就行
					// 如果为空，但是有可能是修改了的唯一的编码，所以进一步通过sourceid查询
					if(Objects.isNull(databaseMaterialInfoForCode)){
						databaseMaterialInfoForId = this.getOneBySourcesId(materialInfo.getSourceId());
					}

					materialInfo.setCreatedBy(-1L);
					materialInfo.setLastModifiedBy(-1L);
					Date now = DateUtil.now();
					materialInfo.setLastModifiedDate(now);
					//通过id和编码查询都为空，那就插入，否则更新
					if ( Objects.isNull(databaseMaterialInfoForId) && Objects.isNull(databaseMaterialInfoForCode) ) {
						materialInfo.setCreatedDate(now);
						materialInfo.setIsDeleted(0);
						batchSqlSession.insert(sqlStatement(SqlMethod.INSERT_ONE), materialInfo);
					} else {
						materialInfo.setId(databaseMaterialInfoForCode.getId());
						materialInfo.setIsDeleted(0);
						MapperMethod.ParamMap<MaterialInfo> param = new MapperMethod.ParamMap<>();
						param.put(Constants.ENTITY, materialInfo);
						batchSqlSession.update(sqlStatement(SqlMethod.UPDATE_BY_ID), param);
					}
				}
			}
			batchSqlSession.flushStatements();
		}
	}

	@Override
	public MaterialInfo getOneBySourcesId(Long sourceId) {
		return baseMapper.selectOne(new LambdaQueryWrapper<MaterialInfo>().eq(MaterialInfo::getSourceId,sourceId));
	}

	@Override
	public MaterialInfo getOneByMaterialCode(String materialCode) {
		return	baseMapper.selectOneByMaterialCode(materialCode);
	}

	@Override
	public void batchDeleteByIdList(List<Long> idList) {
		baseMapper.deleteBatchIds(idList);
	}

	@Override
	public MaterialInfoDetailDTO getMaterialDetailByCode(String code) {
		MaterialInfo materialInfo = baseMapper.selectOne(Condition.getQueryWrapper(new MaterialInfo())
				.lambda()
				.eq(MaterialInfo::getMaterialCode, code));
		if(ObjectUtil.isEmpty(materialInfo)){
			throw new ApiException(500,"没有和此编码"+code+"关联的物料数据");
		}

		MaterialInfoDetailDTO materialInfoDetailDTO = new MaterialInfoDetailDTO();
		materialInfoDetailDTO.setMaterialCode(materialInfo.getMaterialCode());
		materialInfoDetailDTO.setMaterialName(materialInfo.getMaterialName());
		materialInfoDetailDTO.setGenreCode(materialInfo.getGenreCode());
		materialInfoDetailDTO.setGenreName(materialInfo.getGenreName());
		materialInfoDetailDTO.setUnit(materialInfo.getUnit());
		materialInfoDetailDTO.setMaterialGroupId(materialInfo.getMaterialGroupId());
//		MaterialInfoDetailDTO materialInfoDTO = BeanUtil.copy(materialInfo, MaterialInfoDetailDTO.class);
		return materialInfoDetailDTO;
	}

}
