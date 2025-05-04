package com.example.socket.first.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JacksonUtil {

    private static final Logger logger = LoggerFactory.getLogger(JacksonUtil.class);
    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
    }

    public static <T> T get(String content, Class clazz) throws JsonParseException, JsonMappingException, IOException, ClassNotFoundException {
        return (T) objectMapper.readValue(content, clazz);
    }

    public static <T> T getObject(String content, Class clazz) {
        try {
            return (T) objectMapper.readValue(content, clazz);
        } catch (Exception e) {
            logger.error("Error : ", e);
            return null;
        }
    }

    public static <T> T get(String content, Class wrapperClazz, Class clazz) throws JsonParseException, JsonMappingException, IOException, ClassNotFoundException {
        JavaType type = objectMapper.getTypeFactory().constructParametricType(wrapperClazz, clazz);
        return (T) objectMapper.readValue(content, type);
    }

    public static <T> T getWrappedList(String content, Class wrapperClazz, Class clazz) throws JsonParseException, JsonMappingException, IOException, ClassNotFoundException {
        JavaType type = objectMapper.getTypeFactory().constructParametricType(wrapperClazz, objectMapper.getTypeFactory().constructCollectionType(List.class, Class.forName(clazz.getName())));
        return (T) objectMapper.readValue(content, type);
    }

    public static <T> T getList(String content, Class<?> target) throws JsonParseException, JsonMappingException, IOException, ClassNotFoundException {
        return (T) objectMapper.readValue(content, objectMapper.getTypeFactory().constructCollectionType(List.class, Class.forName(target.getName())));
    }

    public static Map<?, ?> getMap(Object target) {
        try {
            String json = objectMapper.writeValueAsString(target);
            logger.debug("json : " + json);
            return objectMapper.readValue(json, Map.class);
        } catch (IOException e) {
            logger.error("Exception : ", e);
            return new HashMap<>();
        }
    }

    public static String getString(Object vendorResult) {
        try {
            String rawString = objectMapper.writeValueAsString(vendorResult);
            return rawString;
        } catch (JsonProcessingException ex) {
            logger.error("Exception ", ex);
        }
        return "";
    }

    public static String getStringAscIgnoreNull(Object vendorResult) {
        try {
            objectMapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            String rawString = objectMapper.writeValueAsString(vendorResult);
            return rawString;
        } catch (JsonProcessingException ex) {
            logger.error("Exception ", ex);
        }
        return "";
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static String convertPojotoString(Object obj) {
        try {
            return JacksonUtil.getObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException ex) {
            logger.error("Json Processing exception {}", ex);
            return "Json Processing Exception";
        }
    }

    public static <T> T convertValue(Object object, Class<T> clazzName) {
        try {
            return objectMapper.convertValue(object, clazzName);
        } catch (Exception e) {
            logger.info("Error : {}" + e.getMessage());
        }
        return null;
    }
}
