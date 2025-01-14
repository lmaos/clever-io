package org.clever.core.json;

import com.alibaba.fastjson.JSON;

import java.nio.charset.Charset;

public class JsonUtils {

    public static final String JSON_CONTENT_TYPE = "application/json";
    public static final String JSON_UTF8_CONTENT_TYPE = "application/json;charset=UTF-8";

    public static final Charset JSON_CHARSET = Charset.forName("UTF-8");

    // zero
    public static final byte[] ZERO_BYTES = new byte[0];

    // json 转对象
    public static <T> T jsonToObject(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }

    public static <T> T byteUTF8JsonToObject(byte[] json, Class<T> clazz) {
        return JSON.parseObject(json, 0, json.length, JSON_CHARSET, clazz);
    }

    // 对象转 json
    public static String objectToJson(Object obj) {
        return JSON.toJSONString(obj);
    }
    public static byte[] objectToJsonByteUTF8(Object obj) {
        if (obj == null) {
            return ZERO_BYTES;
        }
        if (obj instanceof String) {
            return ((String) obj).getBytes(JSON_CHARSET);
        }
        return JSON.toJSONBytes(obj);
    }
}
