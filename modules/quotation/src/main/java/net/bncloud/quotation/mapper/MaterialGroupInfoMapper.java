package net.bncloud.quotation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.quotation.entity.MaterialGroupInfo;
import net.bncloud.quotation.param.GetLastGroupTreeParam;
import net.bncloud.quotation.vo.GetLastGroupTreeVo;
import net.bncloud.quotation.vo.GroupMaterCountReVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lijiaju
 * @date 2022/2/24 18:29
 */
public interface MaterialGroupInfoMapper extends BaseMapper<MaterialGroupInfo> {
    MaterialGroupInfo selectByERPGroupId(@Param("erpGroupId") Long erpGroupId);
    Integer getGroupMaterCount(Long erpId);

    List<GroupMaterCountReVo> getGroupMaterCountRe();

    List<GetLastGroupTreeParam> getLastGroupTree(IPage<GetLastGroupTreeParam> toPage, @Param("pageParam") QueryParam<GetLastGroupTreeParam> pageParam);
}
