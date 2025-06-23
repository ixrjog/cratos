package com.baiyi.cratos.eds.aliyun.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/6/23 13:37
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AliyunTagUtils {

   public static String convertMapToTagsJsonStream(Map<String, String> tags) throws JsonProcessingException {
       ObjectMapper objectMapper = new ObjectMapper();
       return objectMapper.writeValueAsString(
               tags.entrySet()
                   .stream()
                   .filter(entry -> entry.getKey() != null && entry.getValue() != null && !entry.getKey().isEmpty() && !entry.getValue().isEmpty())
                   .map(entry -> Map.of("TagKey", entry.getKey(), "TagValue", entry.getValue()))
                   .toList()
       );
   }

}
