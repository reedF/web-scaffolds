/**
 * JsonUtil.java
 * Copyright (c) 2013 by lashou.com
 */
package com.reed.common.util;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.map.ser.StdSerializerProvider;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JSON utility
 * 
 * @author reed
 * 
 */
public final class JsonUtil {

	/**
	 * log
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(JsonUtil.class);

	/**
	 * 
	 */
	private JsonUtil() {

	}

	/**
	 * 
	 */
	static final ObjectMapper OBJECT_MAPPER;

	static {
		StdSerializerProvider sp = new StdSerializerProvider();
		OBJECT_MAPPER = new ObjectMapper(null, sp, null);
		OBJECT_MAPPER.getSerializationConfig().withSerializationInclusion(
				Inclusion.NON_NULL);
		OBJECT_MAPPER.getSerializationConfig().with(
				Feature.SORT_PROPERTIES_ALPHABETICALLY);
		OBJECT_MAPPER
				.configure(
						DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
						false);
	}

	public static ObjectMapper getObjectMapper() {
		return OBJECT_MAPPER;
	}

	/**
	 * JSON串转换为Java泛型对象，可以是各种类型
	 * 
	 * @param <T>
	 *            return type
	 * @param jsonString
	 *            JSON
	 * @param tr
	 *            TypeReference,例如: new TypeReference< List<User> >(){}
	 * @return List对象列表
	 */
	@SuppressWarnings("unchecked")
	public static <T> T json2GenericObject(String jsonString,
			TypeReference<T> tr) {
		if (!StringUtils.isBlank(jsonString)) {
			try {
				return (T) OBJECT_MAPPER.readValue(jsonString, tr);
			} catch (Exception e) {
				LOGGER.warn("json error:" + e.getMessage());
			}
		}
		return null;
	}

	/**
	 * Java对象转JSON字符串
	 * 
	 * @param object
	 *            Java对象，可以是对象，数组，List,Map等
	 * @return JSON 字符串
	 */
	public static String toJson(Object object) {
		try {
			return OBJECT_MAPPER.writeValueAsString(object);
		} catch (Exception e) {
			LOGGER.warn("json error:" + e.getMessage());
		}
		return null;

	}

	/**
	 * JSON字符串转Java对象
	 * 
	 * @param jsonString
	 *            JSON
	 * @param c
	 *            class
	 * @return
	 */
	public static Object json2Object(String jsonString, Class<?> c) {
		if (!StringUtils.isBlank(jsonString)) {
			try {
				return OBJECT_MAPPER.readValue(jsonString, c);
			} catch (Exception e) {
				LOGGER.warn("json error:" + e.getMessage());
			}

		}
		return null;
	}
}
