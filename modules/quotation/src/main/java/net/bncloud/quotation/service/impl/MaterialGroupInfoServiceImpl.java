package net.bncloud.quotation.service.impl;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.common.util.DateUtil;
import net.bncloud.common.util.NumberUtil;
import net.bncloud.common.util.StringUtil;
import net.bncloud.quotation.entity.MaterialGroupInfo;
import net.bncloud.quotation.mapper.MaterialGroupInfoMapper;
import net.bncloud.quotation.param.GetLastGroupTreeParam;
import net.bncloud.quotation.service.MaterialGroupInfoService;
import net.bncloud.quotation.service.api.dto.MaterialGroupInfoDTO;
import net.bncloud.quotation.utils.tree.TreeFilterUtil;
import net.bncloud.quotation.vo.GetLastGroupTreeVo;
import net.bncloud.quotation.vo.GroupMaterCountReVo;
import net.bncloud.quotation.vo.MaterialGroupInfoVo;
import net.bncloud.support.Condition;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
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
public class MaterialGroupInfoServiceImpl extends BaseServiceImpl<MaterialGroupInfoMapper, MaterialGroupInfo> implements MaterialGroupInfoService {


	@Autowired
	TreeFilterUtil<Tree<Long>,Long> treeFilterUtil;
	@Resource
	private MaterialGroupInfoMapper materialGroupInfoMapper;

	/**
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void syncMaterialGroupDataSaveOrUpdate(List<MaterialGroupInfoDTO> MaterialGroupInfoDTOList) {
		//去重
		List<MaterialGroupInfoDTO> distinctDTOS = MaterialGroupInfoDTOList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(()
				-> new TreeSet<>(Comparator.comparing(MaterialGroupInfoDTO :: getErpId))), ArrayList::new));

		try (SqlSession batchSqlSession = sqlSessionBatch()) {
			for (MaterialGroupInfoDTO entityDTO : distinctDTOS) {
				if (!Objects.isNull(entityDTO.getErpId())) {
					MaterialGroupInfo materialGroupInfo = new MaterialGroupInfo();
					BeanUtil.copyProperties(entityDTO,materialGroupInfo);

					MaterialGroupInfo databaseMaterialGroupInfo = getByERPGroupId(entityDTO.getErpId());

					materialGroupInfo.setCreatedBy(-1L);
					materialGroupInfo.setLastModifiedBy(-1L);
					Date now = DateUtil.now();
					materialGroupInfo.setLastModifiedDate(now);
					if (Objects.isNull(databaseMaterialGroupInfo)) {
						materialGroupInfo.setCreatedDate(now);
						materialGroupInfo.setIsDeleted(0);
						batchSqlSession.insert(sqlStatement(SqlMethod.INSERT_ONE), materialGroupInfo);
					} else {
						materialGroupInfo.setId(databaseMaterialGroupInfo.getId());
						materialGroupInfo.setIsDeleted(databaseMaterialGroupInfo.getIsDeleted());
						MapperMethod.ParamMap<MaterialGroupInfo> param = new MapperMethod.ParamMap<>();
						param.put(Constants.ENTITY, materialGroupInfo);
						batchSqlSession.update(sqlStatement(SqlMethod.UPDATE_BY_ID), param);
					}
				}
			}
			batchSqlSession.flushStatements();
		}
	}

	@Override
	public MaterialGroupInfo getByERPGroupId(Long erpId) {
		return baseMapper.selectByERPGroupId(erpId);
	}

	@Override
	public PageImpl<Tree<Long>> getAllGroupTree(QueryParam<MaterialGroupInfo> param, IPage page){
		List<MaterialGroupInfo> materialGroupInfos=new ArrayList<>();
		IPage< Tree<Long> > pageResult=new Page<>();
		//直接查询全部
		if(StringUtil.isEmpty(param.getSearchValue())&&StringUtil.isEmpty(param.getParam().getErpName())){
//			materialGroupInfos = list();
			MaterialGroupInfo materialGroupInfo = new MaterialGroupInfo();
			LambdaQueryWrapper<MaterialGroupInfo> eq = Condition.getQueryWrapper(new MaterialGroupInfo())
					.lambda()
					.eq(MaterialGroupInfo::getErpParentId, 0L);
			IPage<MaterialGroupInfo> materialGroupInfosPage = page(page,eq);
			materialGroupInfos= materialGroupInfosPage.getRecords();

			//取出父节点下的子节点
			getChildrenList(materialGroupInfos,materialGroupInfos);
			BeanUtil.copy(materialGroupInfosPage,pageResult);
		//高级查询搜索
		}else{
			LambdaQueryWrapper<MaterialGroupInfo> materialGroupInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
			materialGroupInfoLambdaQueryWrapper.like(param.getSearchValue()!=null,MaterialGroupInfo::getErpName,param.getSearchValue())
					                           .or()
					                           .like( !StringUtil.isEmpty( param.getParam().getErpName() ),MaterialGroupInfo::getErpName,param.getParam().getErpName())
												.and(i->i.eq(MaterialGroupInfo::getErpParentId,0L));
//			                                   .eq(MaterialGroupInfo::getErpParentId,0L);

			//取出父节点
//			List<MaterialGroupInfo> materialGroupInfosParentList = this.baseMapper.selectList(materialGroupInfoLambdaQueryWrapper);
			IPage materialGroupInfosParentListPage = this.baseMapper.selectPage(page, materialGroupInfoLambdaQueryWrapper);
			List<MaterialGroupInfo> materialGroupInfosParentList = materialGroupInfosParentListPage.getRecords();

			//要判断是否为空，因为如果是直接搜索子节点的话，父节点是没有数据的
			if(materialGroupInfosParentList.size()>0){
				materialGroupInfos.addAll(materialGroupInfosParentList);

				/*List<Long> collectErpIds = materialGroupInfosParentList.stream().map(MaterialGroupInfo::getErpId).collect(Collectors.toList());
				//Long parentId = materialGroupInfosParentList.getErpId();

				for (Long collectErpId : collectErpIds) {
					//取出父节点下的所有的子节点
					LambdaQueryWrapper<MaterialGroupInfo> materialGroupInfoLambdaQueryGetParent = new LambdaQueryWrapper<>();
					materialGroupInfoLambdaQueryGetParent.eq(MaterialGroupInfo::getErpParentId,collectErpId);
					List<MaterialGroupInfo> materialGroupInfosChildren = this.baseMapper.selectList(materialGroupInfoLambdaQueryGetParent);
					materialGroupInfos.addAll(materialGroupInfosChildren);
				}*/

				//取出父节点下的子节点
				getChildrenList(materialGroupInfosParentList,materialGroupInfos);


				BeanUtil.copy(materialGroupInfosParentListPage,pageResult);
			}else{
				//可能是查询子节点
				LambdaQueryWrapper<MaterialGroupInfo> materialGroupInfoLambdaQueryWrapperForChildren = new LambdaQueryWrapper<>();
				materialGroupInfoLambdaQueryWrapperForChildren.like(param.getSearchValue()!=null&&!param.getSearchValue().equals(""),MaterialGroupInfo::getErpName,param.getSearchValue())
						.or().like(param.getParam().getErpName()!=null&&!param.getParam().getErpName().equals(""),MaterialGroupInfo::getErpName,param.getParam().getErpName());
				List<MaterialGroupInfo> materialGroupInfosChildrenList = this.baseMapper.selectList(materialGroupInfoLambdaQueryWrapperForChildren);
				if(materialGroupInfosChildrenList.size()>0){
					materialGroupInfos.addAll(materialGroupInfosChildrenList);

					//然后把子节点所属的父节点查询出来
					List<Long> collectErpParentId = materialGroupInfosChildrenList.stream().map(item -> item.getErpParentId()).distinct().collect(Collectors.toList());

					for (Long erpParentId : collectErpParentId) {
						LambdaQueryWrapper<MaterialGroupInfo> materialGroupInfoLambdaQueryWrapperForChildrenSelectParent = new LambdaQueryWrapper<>();
						materialGroupInfoLambdaQueryWrapperForChildrenSelectParent.like(MaterialGroupInfo::getErpId,erpParentId);


						//分页查询,因为是分页,所以要再把只有分页的父节点下的字节点才进行显示
						//List<MaterialGroupInfo> materialGroupInfos1 = this.baseMapper.selectList(materialGroupInfoLambdaQueryWrapperForChildrenSelectParent);
						IPage materialGroupInfos1Page = this.baseMapper.selectPage(page, materialGroupInfoLambdaQueryWrapperForChildrenSelectParent);
						List<MaterialGroupInfo> materialGroupInfos1  = materialGroupInfos1Page.getRecords();

						materialGroupInfos.addAll(materialGroupInfos1);
						BeanUtil.copy(materialGroupInfos1Page,pageResult);

						//取出父节点的erpid
						List<Long> collectErpIds = materialGroupInfosParentList.stream().map(MaterialGroupInfo::getErpId).collect(Collectors.toList());

						for (Long collectErpId : collectErpIds) {
							//取出父节点下的所有的子节点
							for (MaterialGroupInfo materialGroupInfo : materialGroupInfosChildrenList) {
								if( materialGroupInfo.getErpParentId().equals(collectErpId) ){
									materialGroupInfos.add(materialGroupInfo);
								}
							}

//							LambdaQueryWrapper<MaterialGroupInfo> materialGroupInfoLambdaQueryGetParent = new LambdaQueryWrapper<>();
//							materialGroupInfoLambdaQueryGetParent.eq(MaterialGroupInfo::getErpParentId,collectErpId);
//							List<MaterialGroupInfo> materialGroupInfosChildren = this.baseMapper.selectList(materialGroupInfoLambdaQueryGetParent);
//							materialGroupInfos.addAll(materialGroupInfosChildren);
						}

					}
				}
			}
		}

		List<MaterialGroupInfoVo> materialGroupInfoVos = new ArrayList<>();
		List<Long> hitIds = new ArrayList<>();

		//取出所有分组的数量，准备统计
		List<GroupMaterCountReVo> groupMaterCountRe = getGroupMaterCountRe();
		for (MaterialGroupInfo materialGroupInfo : materialGroupInfos) {
			MaterialGroupInfoVo materialGroupInfoVo = new MaterialGroupInfoVo();
			BeanUtil.copyProperties(materialGroupInfo,materialGroupInfoVo);


			if(materialGroupInfoVo.getErpParentId()!=0L){
				int mCount = groupMaterCountRe.stream()
						.filter(item -> {

							return item.getMaterialGroupId() .equals(materialGroupInfoVo.getErpId());
						})
						.map(item -> {
							return item.getMaterialCount();
						}).mapToInt(item -> item).sum();

				materialGroupInfoVo.setMaterCountNumber(mCount);
			}else{
				materialGroupInfoVo.setMaterCountNumber(0);
			}


			//查询统计每个分组下所拥有的物料数据数量
//			materialGroupInfoVo.setMaterCountNumber(getGroupMaterCount(materialGroupInfoVo.getErpId()));

			materialGroupInfoVos.add(materialGroupInfoVo);

			if(param!=null&&param.getSearchValue()!=null&&materialGroupInfoVo.getErpName().contains(param.getSearchValue())
			                       || !StringUtil.isEmpty(param.getParam().getErpName()) && materialGroupInfoVo.getErpName().contains(param.getParam().getErpName()) ){
				hitIds.add(materialGroupInfoVo.getErpId());
			}
		}

		for (MaterialGroupInfoVo materialGroupInfoVo : materialGroupInfoVos) {
			if(materialGroupInfoVo.getErpParentId()==0L){//父节点
				Integer parentCount = materialGroupInfoVos.stream()
						.filter(item -> item.getErpParentId() .equals( materialGroupInfoVo.getErpId()))
						.collect(Collectors.summingInt(MaterialGroupInfoVo::getMaterCountNumber));

				materialGroupInfoVo.setMaterCountNumber(parentCount);

			}
		}

		List<Tree<Long>> resultTree = analyzeGroupTree(materialGroupInfoVos);
		if(StringUtils.isNotEmpty(param.getSearchValue())){

			return PageUtils.result(pageResult.setRecords(treeFilterUtil.searchNode(resultTree,hitIds)));

//			return new PageImpl<>(treeFilterUtil.searchNode(resultTree,hitIds));
		}else{

			return PageUtils.result(pageResult.setRecords(resultTree));
//			return new PageImpl<>(resultTree);
		}
	}

	@Override
	public Integer getGroupMaterCount(long erpId) {
		return baseMapper.getGroupMaterCount(erpId);
	}

	@Override
	public List<GroupMaterCountReVo> getGroupMaterCountRe() {
		return baseMapper.getGroupMaterCountRe();
	}

	private List<Tree<Long>> analyzeGroupTree(List<MaterialGroupInfoVo> materialGroupInfos){
		TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
		treeNodeConfig.setIdKey("erpId").setParentIdKey("erpParentId").setChildrenKey("children");
		List<Tree<Long>> trees = TreeUtil.build(materialGroupInfos,0L,treeNodeConfig,(object, treeNode) -> {
			treeNode.setId(object.getErpId());
			treeNode.setParentId(object.getErpParentId());
			treeNode.putExtra("erpName",object.getErpName());
			treeNode.putExtra("erpCode",object.getErpNumber());
			treeNode.putExtra("materCountNumber",object.getMaterCountNumber());
			treeNode.putExtra("createdDate",object.getCreatedDate());
		});
		return trees;
	}

	private void getChildrenList(List<MaterialGroupInfo> materialGroupInfosParam,List<MaterialGroupInfo> materialGroupInfos) {
		List<Long> collectErpIds = materialGroupInfosParam.stream().map(MaterialGroupInfo::getErpId).collect(Collectors.toList());
		//Long parentId = materialGroupInfosParentList.getErpId();

		for (Long collectErpId : collectErpIds) {
			//取出父节点下的所有的子节点
			LambdaQueryWrapper<MaterialGroupInfo> materialGroupInfoLambdaQueryGetParent = new LambdaQueryWrapper<>();
			materialGroupInfoLambdaQueryGetParent.eq(MaterialGroupInfo::getErpParentId,collectErpId);
			List<MaterialGroupInfo> materialGroupInfosChildren = this.baseMapper.selectList(materialGroupInfoLambdaQueryGetParent);
			materialGroupInfos.addAll(materialGroupInfosChildren);
		}
	}

	@Override
	public IPage<GetLastGroupTreeParam> getLastGroupTree(QueryParam<GetLastGroupTreeParam> param, IPage<GetLastGroupTreeParam> page) {
		//分页查询
		List<GetLastGroupTreeParam> lastGroupTree = materialGroupInfoMapper.getLastGroupTree(page, param);
		page.setRecords(lastGroupTree);
		return page;
	}

}
