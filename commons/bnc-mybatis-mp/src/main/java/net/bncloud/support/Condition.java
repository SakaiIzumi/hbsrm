package net.bncloud.support;
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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.bncloud.common.constant.TokenConstant;
import net.bncloud.common.util.BeanUtil;

import java.util.Map;

/**
 * 分页工具
 *
 * @author lvxiangyi
 */
public class Condition {

	/**
	 * 获取mybatis plus中的QueryWrapper
	 *
	 * @param entity
	 * @param <T>
	 * @return
	 */
	public static <T> QueryWrapper<T> getQueryWrapper(T entity) {
		return new QueryWrapper<>(entity);
	}

	/**
	 * 获取mybatis plus中的QueryWrapper
	 *
	 * @param query
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public static <T> QueryWrapper<T> getQueryWrapper(Map<String, Object> query, Class<T> clazz) {
		query.remove(TokenConstant.HEADER);
		query.remove("current");
		query.remove("size");
		query.remove("ascs");
		query.remove("descs");
		QueryWrapper<T> qw = new QueryWrapper<>();
		qw.setEntity(BeanUtil.newInstance(clazz));
		SqlKeyword.buildCondition(query, qw);
		return qw;
	}
}
