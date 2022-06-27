package net.bncloud.base;
/*
 *  Copyright (c) 2018-2028, Chill Zhuang All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: Chill 庄骞 (smallchill@163.com)
 */

import com.alibaba.excel.annotation.ExcelIgnore;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.bncloud.common.util.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 基础实体类
 *
 * @author lvxiangyi
 */
@Data
public class BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 主键id
	 */
	@ExcelIgnore
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "主键id")
	@TableId(value = "id", type = IdType.ID_WORKER)
	@NotNull(groups = Update.class,message = "主键ID不能为空")
	private Long id;

	/**
	 * 创建人
	 */
	@ExcelIgnore
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "创建人")
	private Long createdBy;


	/**
	 * 创建时间
	 */
	@ExcelIgnore
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@ApiModelProperty(value = "创建时间")
	private Date createdDate;

	/**
	 * 更新人
	 */
	@ExcelIgnore
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "更新人")
	private Long lastModifiedBy;

	/**
	 * 更新时间
	 */
	@ExcelIgnore
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@ApiModelProperty(value = "更新时间")
	private Date lastModifiedDate;


	/**
	 * 状态[0:未删除,1:删除]
	 */
	@ExcelIgnore
	@TableLogic
	@ApiModelProperty(value = "是否已删除")
	@JsonIgnore
	private Integer isDeleted;

	public interface Update {
	}

	public interface Create{

	}
}
