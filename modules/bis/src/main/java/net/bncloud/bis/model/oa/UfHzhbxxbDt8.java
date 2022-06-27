package net.bncloud.bis.model.oa;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 自然人-发票信息
 * </p>
 *
 * @author jabngit frame
 * @since 2022-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="UfHzhbxxbDt8对象", description="自然人-发票信息")
public class UfHzhbxxbDt8 implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;

    private Integer mainid;

    private Integer sfnkp;

    private String qxx;

    private Integer hzxz;

    private String fptt;

    private String nsdjh;

    private String khyh;

    private String yhzh;

    private String kplxdh;

    private String kptxdz;

    private String fplx;

    private String sysl;

    @TableField("MODEUUID")
    private String modeuuid;

    private Integer nsrlx;


}
