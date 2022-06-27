package net.bncloud.common.security.data;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DataDesensitizeField {

    /**
     * 数据主题编码 KEY，bd_sec_data_subject.subject_id
     */
    private String subjectId;
    /**
     * 维度编码，bd_sec_data_dimension.dimension_code
     */
    private String dimensionCode;

    private String pattern;
}
