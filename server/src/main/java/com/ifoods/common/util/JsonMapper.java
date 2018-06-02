package com.ifoods.common.util;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.JSONPObject;


public class JsonMapper {
    private static Logger log = LoggerFactory.getLogger(JsonMapper.class);
    private static ObjectMapper mapper = new ObjectMapper();
    static {
        // 不序列化空元素，包括null,""以及空Collection和Array
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        // 枚举类序列化的时候值转为order值
        mapper.enable(SerializationFeature.WRITE_ENUMS_USING_INDEX);
        //设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public static final String toJsonString(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            log.warn("object to json exception", e);
            throw new RuntimeException(e);
        }
    }
    public static final <T> T toObject(String src, Class<T> cls) {
        try {
            return mapper.readValue(src, cls);
        } catch (Exception e) {
            log.warn("json to object exception", e);
            throw new RuntimeException(e);
        }
    }
    /**
     * 将obj对象转换成 class类型的对象
     * @param obj
     * @param clazz
     * @return
     */
    public static <T> T parseObject(Object obj, Class<T> clazz){
        return JSON.parseObject(JSON.toJSONString(obj), clazz);
    }
    /**
     * 更新object的部分属性
     * @param jsonString
     * @param object
     * @return
     */
    @SuppressWarnings("unchecked")
    public static final <T> T patchObject(String jsonString, T object) {
        try {
            return (T) mapper.readerForUpdating(object).readValue(jsonString);
        } catch (JsonProcessingException e) {
            log.warn("update json string:" + jsonString + " to object:" + object + " error.", e);
        } catch (IOException e) {
            log.warn("update json string:" + jsonString + " to object:" + object + " error.", e);
        }
        return null;
    }
    /**
     * 输出jsonp数据格式
     */
    public static final String toJsonpString(String functionName, Object object) {
        return toJsonString(new JSONPObject(functionName, object));
    }
    public static final ObjectMapper getObjectMapper() {
        return mapper;
    }
}
