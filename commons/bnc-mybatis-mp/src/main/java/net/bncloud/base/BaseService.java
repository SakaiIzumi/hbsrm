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


import com.baomidou.mybatisplus.extension.service.IService;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 基础业务接口
 *
 * @param <T>
 * @author lvxiangyi
 */
public interface BaseService<T> extends IService<T> {

	/**
	 * 逻辑删除
	 *
	 * @param ids id集合
	 * @return
	 */
	boolean deleteLogic(@NotEmpty List<Long> ids);

	/**
	 * 变更状态
	 *
	 * @param ids    id集合
	 * @param status 状态值
	 * @return
	 */
	boolean changeStatus(@NotEmpty List<Long> ids, Integer status);
	
	
	

}
