package net.bncloud.common.base.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 类名称:    查询参数
 * 类描述:    分页公共类
 * 创建人:    lvxiangyi
 * 创建时间:  2021/3/10 11:19 上午
 */
@Data
public class QueryParam<T> implements Serializable {
	private static final long serialVersionUID = 1L;


	/**
	 * 公共高级查询参数
	 */
	private String searchValue;

	@ApiModelProperty("开始日期")
	private String startDate;

	@ApiModelProperty("结束日期")
	private String endDate;
	
	//请求参数
	@Valid
	private T param;
}
