package net.$51zhiyuan.development.kidbridge.module.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * json处理扩展
 */
public class KidbridgeObjectMapper extends ObjectMapper {

    public KidbridgeObjectMapper() {
        // NULL字段不参与json序列化
        this.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 禁用数据检验
        this.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

}