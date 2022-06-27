package net.bncloud.common.security.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 武书静 wusj4 shujing.wu@meicloud.com
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DataDimensionGrant {

    /**
     * 数据主题编码 KEY，bd_sec_data_subject.subject_id
     */
    private String subjectId;
    /**
     * 维度编码，bd_sec_data_dimension.dimension_code
     */
    private String dimensionCode;
    /**
     * 别名，bd_sec_data_subject_dimension_rel.alias
     */
    private String alias;
    /**
     * 是否特权，bd_sec_data_grant.is_special Y 是，N 否
     */
    private boolean special;
    /**
     * 数据维度值
     */
    private List<String> dataIds;

}
