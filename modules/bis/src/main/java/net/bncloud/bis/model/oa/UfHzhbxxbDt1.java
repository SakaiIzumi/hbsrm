package net.bncloud.bis.model.oa;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 企业-法人
 * </p>
 *
 * @author jabngit frame
 * @since 2022-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="UfHzhbxxbDt1对象", description="企业-法人")
public class UfHzhbxxbDt1 implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;

    private Integer mainid;

    private String fddbrxm;

    private String sjh;

    private String yxdz;

    private String frsfz;

    private Integer hzxz;

    private String bz;


}
