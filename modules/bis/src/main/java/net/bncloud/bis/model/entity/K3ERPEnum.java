package net.bncloud.bis.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 同步金蝶需要枚举字段保存表
 * @author lijiaju
 * @date 2022/2/23 14:40
 */
@TableName("k3_erp_enum")
@Data
@ToString
public class K3ERPEnum implements Serializable {
    /**
     * 主键ID
     */
    @TableId(value = "id",type = IdType.NONE)
    private Long id;

    /**
     * 字段名称
     */
    private String	fieldKey;

    /**
     * 枚举值
     */
    private String	code;
    /**
     * 枚举名称
     */
    private String	value;

    /**
     * 禁用状态 0-非禁用 1是禁用
     */
    private Integer status;
}
