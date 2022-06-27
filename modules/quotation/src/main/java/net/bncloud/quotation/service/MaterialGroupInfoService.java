package net.bncloud.quotation.service;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseService;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.quotation.entity.MaterialGroupInfo;
import net.bncloud.quotation.entity.MaterialInfo;
import net.bncloud.quotation.param.GetLastGroupTreeParam;
import net.bncloud.quotation.param.MaterialInfoParam;
import net.bncloud.quotation.service.api.dto.MaterialGroupInfoDTO;
import net.bncloud.quotation.service.api.dto.MaterialInfoDTO;
import net.bncloud.quotation.vo.GetLastGroupTreeVo;
import net.bncloud.quotation.vo.GroupMaterCountReVo;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * <p>
 * 物料信息表 服务类
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
public interface MaterialGroupInfoService extends BaseService<MaterialGroupInfo> {
    void syncMaterialGroupDataSaveOrUpdate(List<MaterialGroupInfoDTO> MaterialGroupInfoList);
    MaterialGroupInfo getByERPGroupId(Long erpId);
    PageImpl<Tree<Long>> getAllGroupTree(QueryParam<MaterialGroupInfo> param, IPage page);

    /**
     * 获取分组下有多少个物料
     * @param erpId
     * @return
     */
    Integer getGroupMaterCount(long erpId);

    List<GroupMaterCountReVo> getGroupMaterCountRe();

    IPage<GetLastGroupTreeParam> getLastGroupTree(QueryParam<GetLastGroupTreeParam> param, IPage<GetLastGroupTreeParam> toPage);
}
