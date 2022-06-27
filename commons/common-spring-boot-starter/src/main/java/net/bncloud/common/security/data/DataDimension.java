package net.bncloud.common.security.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据维度
 *
 * @author 武书静 wusj4 shujing.wu@meicloud.com
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DataDimension {

    private String code;
    private String alias;
    private String type;
    private boolean useDefault;

//    public String getCode() {
//        return code;
//    }
//
//    public void setCode(String code) {
//        this.code = code;
//    }
//
//    public String getAlias() {
//        return alias;
//    }
//
//    public void setAlias(String alias) {
//        this.alias = alias;
//    }
//
//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }
//
//    public boolean isUseDefault() {
//        return useDefault;
//    }
//
//    public void setUseDefault(boolean useDefault) {
//        this.useDefault = useDefault;
//    }
}
