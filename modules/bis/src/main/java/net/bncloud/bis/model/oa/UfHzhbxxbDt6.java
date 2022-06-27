package net.bncloud.bis.model.oa;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 自然人-其他联系人
 * </p>
 *
 * @author jabngit frame
 * @since 2022-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="UfHzhbxxbDt6对象", description="自然人-其他联系人")
public class UfHzhbxxbDt6 implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;

    private Integer mainid;

    private String xm;

    private String zw;

    private String wxh;

    private String yxdz;

    private String sfzhz;

    private Integer hzxz;

    private String sjhm;


}
