package net.bncloud.quotation.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import net.bncloud.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import net.bncloud.common.util.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;
import java.util.Date;

/**
 * <p>
 * 短信验证码信息表
 * </p>
 *
 * @author Auto-generator
 * @since 2022-03-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_rfq_verification_code")

public class TRfqVerificationCode extends BaseEntity {

	private static final long serialVersionUID = 1L;


    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String mobile;

    /**
     * 验证码
     */
    @ApiModelProperty(value = "验证码")
    private String verificationCode;

    /**
     * 生效时间
     */
//    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
//    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
//    @ApiModelProperty(value = "生效时间")
//    private OffsetDateTime effectiveTime;

    /**
     * 生效时间
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    @ApiModelProperty(value = "生效时间")
    private Date effectiveTime;

    /**
     * 失效时间
     */
//    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
//    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
//    @ApiModelProperty(value = "失效时间")
//    private OffsetDateTime expirationTime;

    /**
     * 失效时间
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    @ApiModelProperty(value = "失效时间")
    private Date expirationTime;


    /**
     * 手机号对应的用户的uid
     */
    @ApiModelProperty(value = "uid")
    private Long uid;

    /**
     * 每条短信验证码记录也保存询价单id，这样可以知道该条短信信息是属于哪一张询价单的
     */
    @ApiModelProperty(value = "quotationId")
    private String quotationId;

    /**
     * 区分是哪一种验证人员的验证码
     */
    @ApiModelProperty(value = "memberType")
    private String memberType;
}
