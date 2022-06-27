package net.bncloud.service.impl;/*
 *      Copyright (c) 2018-2028, DreamLu All rights reserved.
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
 *  Author: DreamLu 卢春梦 (596392912@qq.com)
 */


import net.bncloud.base.BaseEntity;
import net.bncloud.base.BaseServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import net.bncloud.injector.BladeSqlMethod;
import net.bncloud.mapper.BladeMapper;
import org.apache.ibatis.session.SqlSession;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import net.bncloud.service.BladeService;

import java.util.Collection;

/**
 * BladeService 实现类（ 泛型：M 是 mapper 对象，T 是实体 ， PK 是主键泛型 ）
 *
 * @author L.cm, lvxiangyi
 */
@Validated
public class BladeServiceImpl<M extends BladeMapper<T>, T extends BaseEntity> extends BaseServiceImpl<M, T> implements BladeService<T> {

	@Override
	public boolean saveIgnore(T entity) {
		return retBool(baseMapper.insertIgnore(entity));
	}

	@Override
	public boolean saveReplace(T entity) {
		return retBool(baseMapper.replace(entity));
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean saveIgnoreBatch(Collection<T> entityList, int batchSize) {
		return saveBatch(entityList, batchSize, BladeSqlMethod.INSERT_IGNORE_ONE);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean saveReplaceBatch(Collection<T> entityList, int batchSize) {
		return saveBatch(entityList, batchSize, BladeSqlMethod.REPLACE_ONE);
	}

	private boolean saveBatch(Collection<T> entityList, int batchSize, BladeSqlMethod sqlMethod) {
		String sqlStatement = bladeSqlStatement(sqlMethod);
		try (SqlSession batchSqlSession = sqlSessionBatch()) {
			int i = 0;
			for (T anEntityList : entityList) {
				batchSqlSession.insert(sqlStatement, anEntityList);
				if (i >= 1 && i % batchSize == 0) {
					batchSqlSession.flushStatements();
				}
				i++;
			}
			batchSqlSession.flushStatements();
		}
		return true;
	}

	/**
	 * 获取 bladeSqlStatement
	 *
	 * @param sqlMethod ignore
	 * @return sql
	 */
	protected String bladeSqlStatement(BladeSqlMethod sqlMethod) {
		return SqlHelper.table(currentModelClass()).getSqlStatement(sqlMethod.getMethod());
	}
}
