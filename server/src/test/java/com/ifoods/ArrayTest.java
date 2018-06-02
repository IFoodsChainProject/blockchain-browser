package com.ifoods;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.ifoods.common.util.CommonUtils;
import com.ifoods.model.SimpleBlock;
import com.ifoods.model.TxType;

/**
 * 
 * @author zhenghui.li
 * @date 2018年5月22日
 */
public class ArrayTest {

    public static void main(String[] args) {
        SimpleBlock[] resultList = new SimpleBlock[10];
        System.out.println(resultList[0]);
        
        System.out.println(System.currentTimeMillis());
        
        String str = "7b22636f6e74656e74223a22547565204d617920323220323031382031353a32303a323120474d542b303830302028e4b8ade59bbde6a087e58786e697b6e997b429222c227369676e6174757265223a226633333165646339383761343538373538663735646261373432393638626534303637616665653833396237303039326331613562336237316433663338656332363662623539313562646139623662613861353536363439663130616331303831376165373633366137373730613835323539643965656161333566383536227d";
        String resultData = CommonUtils.decrypt(str);
        Map result = (Map) JSON.parse(resultData);
        resultData = result.get("content").toString();
        if(resultData!=null) {
            
        }
        System.out.println(resultData);
        
    }

}
