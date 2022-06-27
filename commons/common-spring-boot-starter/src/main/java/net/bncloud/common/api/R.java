/*
 *      Copyright (c) 2018-2028, Chill Zhuang All rights reserved.
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
package net.bncloud.common.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.bncloud.common.constant.BncloudConstant;
import net.bncloud.common.pageable.PageResultAdapter;
import net.bncloud.common.util.ObjectUtil;
import org.springframework.data.domain.Page;
import org.springframework.lang.Nullable;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

/**
 * 统一API响应结果封装
 *
 * @author lv
 */
@Getter
@Setter
@ToString
@ApiModel(description = "返回信息")
public class R<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "状态码", required = true)
	private int code;
	@ApiModelProperty(value = "是否成功", required = true)
	private boolean success;
	@ApiModelProperty(value = "承载数据")
	private T data;
	@ApiModelProperty(value = "返回消息", required = true)
	private String msg;

	/**
	 * 操作按钮：addition新增，delete删除，edit编辑
	 */
	private Map<String,Boolean> operationButtons;

	public R() {
		this(ResultCode.SUCCESS);
	}

	private R(IResultCode resultCode) {
		this(resultCode, null, resultCode.getMessage());
	}


	private R(IResultCode resultCode, String msg) {
		this(resultCode, null, msg);
	}



	private R(IResultCode resultCode, T data) {
		this(resultCode, data, resultCode.getMessage());
	}

	private R(IResultCode resultCode, T data, String msg) {
		this(resultCode.getCode(), data, msg);
	}
	private R(IResultCode resultCode, T data, String msg,boolean success) {
		this(resultCode.getCode(), data, msg,success);
	}


	private R(int code, T data, String msg) {
		this.code = code;
		this.data = data;
		this.msg = msg;
		this.success = ResultCode.SUCCESS.code == code;
	}

	private R(int code, T data, String msg,boolean success) {
		this.code = code;
		this.data = data;
		this.msg = msg;
		this.success = success;
	}


	/**
	 * 判断返回是否为成功
	 *
	 * @param result Result
	 * @return 是否成功
	 */
	public static boolean isSuccess(@Nullable R<?> result) {
		return Optional.ofNullable(result)
			.map(x -> ObjectUtil.nullSafeEquals(ResultCode.SUCCESS.code, x.code))
			.orElse(Boolean.FALSE);
	}

	/**
	 * 判断返回是否为成功
	 *
	 * @param result Result
	 * @return 是否成功
	 */
	public static boolean isNotSuccess(@Nullable R<?> result) {
		return !R.isSuccess(result);
	}

	/**
	 * 返回R
	 *
	 * @param data 数据
	 * @param <T>  T 泛型标记
	 * @return R
	 */
	public static <T> R<T> data(T data) {
		return data(data, BncloudConstant.DEFAULT_SUCCESS_MESSAGE);
	}

	/**
	 * 返回R
	 *
	 * @param data 数据
	 * @param msg  消息
	 * @param <T>  T 泛型标记
	 * @return R
	 */
	public static <T> R<T> data(T data, String msg) {
		return data(HttpServletResponse.SC_OK, data, msg);
	}

	/**
	 * 返回R
	 *
	 * @param code 状态码
	 * @param data 数据
	 * @param msg  消息
	 * @param <T>  T 泛型标记
	 * @return R
	 */
	public static <T> R<T> data(int code, T data, String msg) {
		if (data instanceof Page) {
			Page<?> page = (Page<?>) data;
			PageResultAdapter<?> r = new PageResultAdapter<>(page);
			return new R<>(code, (T) r, msg);
		}
		return new R<>(code, data, data == null ? BncloudConstant.DEFAULT_NULL_MESSAGE : msg);
	}

	/**
	 * 返回R
	 *
	 * @param msg 消息
	 * @param <T> T 泛型标记
	 * @return R
	 */
	public static <T> R<T> success(String msg) {
		return new R<>(ResultCode.SUCCESS, msg);
	}
	
	/**
	 * 返回R
	 *
	 * @param <T> T 泛型标记
	 * @return R
	 */
	public static <T> R<T> success() {
		return new R<>(ResultCode.SUCCESS, BncloudConstant.DEFAULT_SUCCESS_MESSAGE);
	}
	/**
	 * 返回R
	 *
	 * @param resultCode 业务代码
	 * @param <T>        T 泛型标记
	 * @return R
	 */
	public static <T> R<T> success(IResultCode resultCode) {
		return new R<>(resultCode);
	}

	/**
	 * 返回R
	 *
	 * @param resultCode 业务代码
	 * @param msg        消息
	 * @param <T>        T 泛型标记
	 * @return R
	 */
	public static <T> R<T> success(IResultCode resultCode, String msg) {
		return new R<>(resultCode, msg);
	}

	/**
	 * 返回R
	 *
	 * @param <T> T 泛型标记
	 * @return R
	 */
	public static <T> R<T> fail() {
		return new R<>(ResultCode.FAILURE, ResultCode.FAILURE.message);
	}

	/**
	 * 返回R
	 *
	 * @param <T> T 泛型标记
	 * @return R
	 */
	public static <T> R<T> remoteFail() {
		return new R<>(ResultCode.REMOTE_FAIL);
	}

	/**
	 * 返回R
	 *
	 * @param <T> T 泛型标记
	 * @return R
	 */
	public static <T> R<T> remoteFail(String serverName) {
		return new R<>(ResultCode.REMOTE_FAIL,String.format( "[%s]服务调用失败，请稍后重试！",serverName));
	}


	/**
	 * 返回R
	 *
	 * @param msg 消息
	 * @param <T> T 泛型标记
	 * @return R
	 */
	public static <T> R<T> fail(String msg) {
		return new R<>(ResultCode.FAILURE, msg);
	}


	/**
	 * 返回R
	 *
	 * @param code 状态码
	 * @param msg  消息
	 * @param <T>  T 泛型标记
	 * @return R
	 */
	public static <T> R<T> fail(int code, String msg) {
		return new R<>(code, null, msg);
	}
	/**
	 * 返回R
	 *
	 * @param code 状态码
	 * @param msg  消息
	 * @param <T>  T 泛型标记
	 * @return R
	 */
	public static <T> R<T> fail(int code, String msg,boolean success) {
		return new R<>(code, null, msg,success);
	}

	/**
	 * 返回R
	 *
	 * @param resultCode 业务代码
	 * @param <T>        T 泛型标记
	 * @return R
	 */
	public static <T> R<T> fail(IResultCode resultCode) {
		return new R<>(resultCode);
	}
	/**
	 * 返回R
	 *
	 * @param resultCode 业务代码
	 * @param <T>        T 泛型标记
	 * @return R
	 */
	public static <T> R<T> fail(IResultCode resultCode,boolean success) {
		return new R<>(resultCode,null,resultCode.getMessage(),success);
	}

	/**
	 * 返回R
	 *
	 * @param resultCode 业务代码
	 * @param msg        消息
	 * @param <T>        T 泛型标记
	 * @return R
	 */
	public static <T> R<T> fail(IResultCode resultCode, String msg) {
		return new R<>(resultCode, msg);
	}

	/**
	 * 返回R
	 *
	 * @param flag 成功状态
	 * @return R
	 */
	public static <T> R<T> status(boolean flag) {
		return flag ? success(BncloudConstant.DEFAULT_SUCCESS_MESSAGE) : fail(BncloudConstant.DEFAULT_FAILURE_MESSAGE);
	}

}
