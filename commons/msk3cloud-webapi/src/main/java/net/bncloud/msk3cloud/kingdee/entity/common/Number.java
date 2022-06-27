package net.bncloud.msk3cloud.kingdee.entity.common;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 编码实体
 * @author Rao
 * @Date 2022/01/11
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Number implements Serializable {
    private static final long serialVersionUID = 6306735496810802579L;

    /**
     * 编号
     */
    @SerializedName("FNumber")
    @JSONField(name = "FNumber")
    private String fNumber;

}
