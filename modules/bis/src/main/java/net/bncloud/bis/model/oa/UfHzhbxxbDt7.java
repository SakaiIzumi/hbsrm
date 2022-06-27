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
 * 自然人-银行信息
 * </p>
 *
 * @author jabngit frame
 * @since 2022-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="UfHzhbxxbDt7对象", description="自然人-银行信息")
public class UfHzhbxxbDt7 implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;

    private Integer mainid;

    private String yhzhmc;

    private String yhzh;

    private String khhqc;

    private Integer hzxz;


}
