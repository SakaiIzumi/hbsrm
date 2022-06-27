package net.bncloud.base;/*
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


import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.bncloud.IsDeleted;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.common.util.DateUtil;
import net.bncloud.utils.AuthUtil;
import org.apache.ibatis.session.SqlSession;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * 业务封装基础类
 *
 * @param <M> mapper
 * @param <T> model
 * @author lvxiangyi
 */
@Validated
public class BaseServiceImpl<M extends BaseMapper<T>, T extends BaseEntity> extends ServiceImpl<M, T> implements BaseService<T>, ApplicationEventPublisherAware {

	private Class<T> modelClass;

	@SuppressWarnings("unchecked")
	public BaseServiceImpl() {
		Type type = this.getClass().getGenericSuperclass();
		this.modelClass = (Class<T>) ((ParameterizedType) type).getActualTypeArguments()[1];
	}

	protected ApplicationEventPublisher applicationEventPublisher;

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}

	@Override
	public boolean save(T entity) {
		//获取当前登录信息
		BaseUserEntity user = AuthUtil.getUser();
		entity.setCreatedBy(user.getUserId());
		entity.setLastModifiedBy(user.getUserId());
		Date now = DateUtil.now();
		entity.setCreatedDate(now);
		entity.setLastModifiedDate(now);
		entity.setIsDeleted(0);
		return super.save(entity);
	}

	@Override
	@Transactional(
			rollbackFor = {Exception.class}
	)
	public boolean saveBatch(Collection<T> entityList, int batchSize) {
		String sqlStatement = this.sqlStatement(SqlMethod.INSERT_ONE);
		SqlSession batchSqlSession = this.sqlSessionBatch();
		Throwable var5 = null;
		//获取当前登录信息
		BaseUserEntity user = AuthUtil.getUser();

		try {
			int i = 0;

			for(Iterator var7 = entityList.iterator(); var7.hasNext(); ++i) {
				T anEntityList = (T) var7.next();
				anEntityList.setCreatedBy(user.getUserId());
				anEntityList.setLastModifiedBy(user.getUserId());
				anEntityList.setCreatedDate(new Date());
				anEntityList.setLastModifiedDate(new Date());
				anEntityList.setIsDeleted(IsDeleted.N.getCode());
				batchSqlSession.insert(sqlStatement, anEntityList);
				if (i >= 1 && i % batchSize == 0) {
					batchSqlSession.flushStatements();
				}
			}

			batchSqlSession.flushStatements();
			return true;
		} catch (Throwable var16) {
			var5 = var16;
			throw var16;
		} finally {
			if (batchSqlSession != null) {
				if (var5 != null) {
					try {
						batchSqlSession.close();
					} catch (Throwable var15) {
						var5.addSuppressed(var15);
					}
				} else {
					batchSqlSession.close();
				}
			}

		}
	}

	@Override
	public boolean updateById(T entity) {
		BaseUserEntity user = AuthUtil.getUser();
		entity.setLastModifiedBy(user.getUserId());
		entity.setLastModifiedDate(DateUtil.now());
		return super.updateById(entity);
	}

	@Override
	public boolean saveOrUpdate(T entity) {
		if (entity.getId() == null) {
			return this.save(entity);
		} else {
			return this.updateById(entity);
		}
	}

	@Override
	public boolean deleteLogic(@NotEmpty List<Long> ids) {
		BaseUserEntity user = AuthUtil.getUser();
		List<T> list = new ArrayList<>();
		ids.forEach(id -> {
			T entity = BeanUtil.newInstance(modelClass);
			entity.setLastModifiedBy(user.getUserId());
			entity.setLastModifiedDate(DateUtil.now());
			entity.setId(id);
			list.add(entity);
		});
		return super.updateBatchById(list) && super.removeByIds(ids);
	}

	@Override
	public boolean changeStatus(@NotEmpty List<Long> ids, Integer status) {
		BaseUserEntity user = AuthUtil.getUser();
		List<T> list = new ArrayList<>();
		ids.forEach(id -> {
			T entity = BeanUtil.newInstance(modelClass);
			entity.setLastModifiedBy(user.getUserId());
			entity.setLastModifiedDate(DateUtil.now());
			entity.setId(id);
			//entity.setStatus(status);
			list.add(entity);
		});
		return super.updateBatchById(list);

	}

}
