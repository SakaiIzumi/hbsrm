package net.bncloud.config;/*
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


import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import net.bncloud.common.yml.YmlPropertyLoaderFactory;
import net.bncloud.injector.BladeSqlInjector;
import net.bncloud.plugins.SqlLogInterceptor;
import net.bncloud.security.data.DataSecurityMybatisInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * mybatis-plus 配置
 *
 * @author lvxiangyi
 */
@Configuration
@PropertySource(
	value = "classpath:/blade-mybatis.yml",
	factory = YmlPropertyLoaderFactory.class
)
@MapperScan("net.bncloud.**.mapper.**")
public class MybatisPlusConfiguration {

	/**
	 * 分页拦截器
	 */
	@Bean
	@ConditionalOnMissingBean(PaginationInterceptor.class)
	public PaginationInterceptor paginationInterceptor() {
		return new PaginationInterceptor();
	}

	/**
	 * sql 日志
	 */
	@Bean
	@ConditionalOnProperty(value = "blade.mybatis-plus.sql-log.enable", matchIfMissing = true)
	public SqlLogInterceptor sqlLogInterceptor() {
		return new SqlLogInterceptor();
	}


	/**
	 * sql 注入
	 */
	@Bean
	public ISqlInjector sqlInjector() {
		return new BladeSqlInjector();
	}

}

