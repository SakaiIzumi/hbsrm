package net.bncloud.oem.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.oem.domain.entity.OperationLog;
import net.bncloud.oem.domain.param.OperationLogParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ddh
 * @since 2022/4/24
 * @description
 */
public interface OperationLogMapper extends BaseMapper<OperationLog> {

    List<OperationLog> selectPageList(IPage<OperationLog> page, @Param("queryParam")QueryParam<OperationLogParam> queryParam,
                                      @Param("createContent")String createContent,
                                      @Param("editContent")String editContent,
                                      @Param("importContent")String importContent);
}