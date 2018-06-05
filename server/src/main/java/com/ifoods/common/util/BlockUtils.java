package com.ifoods.common.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.ifoods.model.MeatInfo;

/**
 * 
 * @author zhenghui.li
 * @date 2018年6月1日
 */
public class BlockUtils {

    /**
     * 解析数据: 格式包含地址值
     * {"content": 
     *      {"data": MeatInfo or "dataInfo", 
     *       "fromAddress": "address",
     *       "signature": "signature"
     *      }
     * }
     * @param recordData
     * @return
     */
    public static Map<String, Object> toAddressAndMeatInfo(String data) {
        MeatInfo meatInfo = new MeatInfo();
        Map<String, Object> tmpMap = new HashMap<>(0);
        String tmpData = null;
        try {
            Map<String, Object> parseMap = JSON.parseObject(CommonUtils.decrypt(data), Map.class);
            parseMap = (Map)parseMap.get("content");
            tmpData = parseMap.get("data").toString();
            try {
                meatInfo = JSON.parseObject(tmpData, MeatInfo.class);
            }catch(Exception e) {
                meatInfo.setNoMeatInfo(tmpData);
            }
            parseMap.put("data", meatInfo);
            return parseMap;
        }catch(Exception e) {
            return null;
        }
        
    }
    
    /**
     * 原始的解析数据
     *      没有地址值;
     */
    public static String toPrimitiveData(String data) {
        if(StringUtils.isEmpty(data)) {
            return null;
        }
        return CommonUtils.decrypt(data);
    }
    
}
