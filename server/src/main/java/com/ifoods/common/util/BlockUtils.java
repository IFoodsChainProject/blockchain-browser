package com.ifoods.common.util;

import java.util.Map;

import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;

/**
 * 
 * @author zhenghui.li
 * @date 2018年6月1日
 */
public class BlockUtils {

    /**
     * 解析数据
     */
    /**
     * @param recordData
     * @return
     */
    public static Map<String, String> toAddressAndData(String data) {
        try {
            Map<String, Object> parseMap = JSON.parseObject(CommonUtils.decrypt(data), Map.class);
            return (Map)parseMap.get("content");
        }catch(Exception e) {
            return null;
        }
        
    }
    
    /**
     * 原始的解析数据
     */
    public static String toPrimitiveData(String data) {
        if(StringUtils.isEmpty(data)) {
            return null;
        }
        return CommonUtils.decrypt(data);
    }
    
}
