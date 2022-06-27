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
package net.bncloud.common.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;

/**
 * Jackson工具类
 *
 * @author Chill
 */
@Slf4j
public class JsonUtil {

	/**
	 * 将对象序列化成json字符串
	 *
	 * @param value javaBean
	 * @return jsonString json字符串
	 */
	public static <T> String toJson(T value) {
		try {
			return getInstance().writeValueAsString(value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 将对象序列化成 json byte 数组
	 *
	 * @param object javaBean
	 * @return jsonString json字符串
	 */
	public static byte[] toJsonAsBytes(Object object) {
		try {
			return getInstance().writeValueAsBytes(object);
		} catch (JsonProcessingException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * 将json反序列化成对象
	 *
	 * @param content   content
	 * @param valueType class
	 * @param <T>       T 泛型标记
	 * @return Bean
	 */
	public static <T> T parse(String content, Class<T> valueType) {
		try {
			return getInstance().readValue(content, valueType);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 将json反序列化成对象
	 *
	 * @param content       content
	 * @param typeReference 泛型类型
	 * @param <T>           T 泛型标记
	 * @return Bean
	 */
	public static <T> T parse(String content, TypeReference<?> typeReference) {
		try {
			return (T) getInstance().readValue(content, typeReference);
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * 将json byte 数组反序列化成对象
	 *
	 * @param bytes     json bytes
	 * @param valueType class
	 * @param <T>       T 泛型标记
	 * @return Bean
	 */
	public static <T> T parse(byte[] bytes, Class<T> valueType) {
		try {
			return getInstance().readValue(bytes, valueType);
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}


	/**
	 * 将json反序列化成对象
	 *
	 * @param bytes         bytes
	 * @param typeReference 泛型类型
	 * @param <T>           T 泛型标记
	 * @return Bean
	 */
	public static <T> T parse(byte[] bytes, TypeReference<?> typeReference) {
		try {
			return (T) getInstance().readValue(bytes, typeReference);
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * 将json反序列化成对象
	 *
	 * @param in        InputStream
	 * @param valueType class
	 * @param <T>       T 泛型标记
	 * @return Bean
	 */
	public static <T> T parse(InputStream in, Class<T> valueType) {
		try {
			return getInstance().readValue(in, valueType);
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * 将json反序列化成对象
	 *
	 * @param in            InputStream
	 * @param typeReference 泛型类型
	 * @param <T>           T 泛型标记
	 * @return Bean
	 */
	public static <T> T parse(InputStream in, TypeReference<?> typeReference) {
		try {
			return (T) getInstance().readValue(in, typeReference);
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * 将json反序列化成List对象
	 *
	 * @param content      content
	 * @param valueTypeRef class
	 * @param <T>          T 泛型标记
	 * @return
	 */
	public static <T> List<T> parseArray(String content, Class<T> valueTypeRef) {
		try {

			if (!StringUtil.startsWithIgnoreCase(content, StringPool.LEFT_SQ_BRACKET)) {
				content = StringPool.LEFT_SQ_BRACKET + content + StringPool.RIGHT_SQ_BRACKET;
			}

			List<Map<String, Object>> list = (List<Map<String, Object>>) getInstance().readValue(content, new TypeReference<List<T>>() {
			});
			List<T> result = new ArrayList<>();
			for (Map<String, Object> map : list) {
				result.add(toPojo(map, valueTypeRef));
			}
			return result;
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	public static Map<String, Object> toMap(String content) {
		try {
			return getInstance().readValue(content, Map.class);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	public static <T> Map<String, T> toMap(String content, Class<T> valueTypeRef) {
		try {
			Map<String, Map<String, Object>> map = (Map<String, Map<String, Object>>) getInstance().readValue(content, new TypeReference<Map<String, T>>() {
			});
			Map<String, T> result = new HashMap<>(16);
			for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
				result.put(entry.getKey(), toPojo(entry.getValue(), valueTypeRef));
			}
			return result;
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	public static <T> T toPojo(Map fromValue, Class<T> toValueType) {
		return getInstance().convertValue(fromValue, toValueType);
	}

	/**
	 * 将json字符串转成 JsonNode
	 *
	 * @param jsonString jsonString
	 * @return jsonString json字符串
	 */
	public static JsonNode readTree(String jsonString) {
		try {
			return getInstance().readTree(jsonString);
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * 将json字符串转成 JsonNode
	 *
	 * @param in InputStream
	 * @return jsonString json字符串
	 */
	public static JsonNode readTree(InputStream in) {
		try {
			return getInstance().readTree(in);
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * 将json字符串转成 JsonNode
	 *
	 * @param content content
	 * @return jsonString json字符串
	 */
	public static JsonNode readTree(byte[] content) {
		try {
			return getInstance().readTree(content);
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * 将json字符串转成 JsonNode
	 *
	 * @param jsonParser JsonParser
	 * @return jsonString json字符串
	 */
	public static JsonNode readTree(JsonParser jsonParser) {
		try {
			return getInstance().readTree(jsonParser);
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}


	/**
	 * 范型readValue json ==> Pager&lt;MyBean&gt;: readValue(json, Pager.class,
	 * MyBean.class)<br>
	 * json ==> List<Set<Integer>>: readValue(json, List.class, Integer.class)<br>
	 */
	public static <T> T readValue(String json, Class<?> parametrized, Class<?> parametersFor,
								  Class<?>... parameterClasses) {
		if (StringUtil.isBlank(json)) {
			return null;
		}

		JavaType type;
		if (parameterClasses == null || parameterClasses.length == 0) {
			type = getInstance().getTypeFactory().constructParametrizedType(parametrized, parametrized, parametersFor);
		} else {
			type = getInstance().getTypeFactory().constructParametrizedType(parametrized, parametersFor, parameterClasses);
		}

		try {
			return getInstance().readValue(json, type);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	@SuppressWarnings("rawtypes")
	public static <T> T readMap(String content, Class<? extends Map> mapClass, Class<?> keyClass, Class<?> valueClass) {
		if (StringUtil.isBlank(content)) {
			return null;
		}
		try {
			return getInstance().readValue(content, getInstance().getTypeFactory().constructMapType(mapClass, keyClass, valueClass));
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	public static <T> List<T> readList(String content, Class<?> collectionClass, Class<T> elementClass) {
		if (StringUtil.isBlank(content)) {
			return null;
		}
		try {
			return getInstance().readValue(content, getInstance().getTypeFactory()
				.constructCollectionLikeType(collectionClass == null ? List.class : collectionClass, elementClass));
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	public static <T> List<T> readList(String content, Class<T> elementClass) {
		return readList(content, null, elementClass);
	}

	public static ObjectMapper getInstance() {
		return JacksonHolder.INSTANCE;
	}

	private static class JacksonHolder {
		private static ObjectMapper INSTANCE = new JacksonObjectMapper();
	}

	private static class JacksonObjectMapper extends ObjectMapper {
		private static final long serialVersionUID = 4288193147502386170L;

		private static final Locale CHINA = Locale.CHINA;

		public JacksonObjectMapper() {
			super();
			//设置地点为中国
			super.setLocale(CHINA);
			//去掉默认的时间戳格式
			super.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
			//设置为中国上海时区
			super.setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
			//序列化时，日期的统一格式
			super.setDateFormat(new SimpleDateFormat(DateUtil.PATTERN_DATETIME, Locale.CHINA));
			// 单引号
			super.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
			// 允许JSON字符串包含非引号控制字符（值小于32的ASCII字符，包含制表符和换行符）
			super.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
			super.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
			super.findAndRegisterModules();
			//失败处理
			super.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
			super.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			//单引号处理
			super.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
			//反序列化时，属性不存在的兼容处理s
			super.getDeserializationConfig().withoutFeatures(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			//日期格式化
			super.registerModule(new BladeJavaTimeModule());
			super.findAndRegisterModules();
		}

		@Override
		public ObjectMapper copy() {
			return super.copy();
		}
	}

}
