package com.ifoods.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.ifoods.controller.IndexController;

/**
 * 
 * @author zhenghui.li
 * @date 2018年5月17日
 */
public class CommonUtils {
    
    private final static Logger logger = LoggerFactory.getLogger(CommonUtils.class);

    /**
     * 16进制转utf-8
     * 
     * @return
     */
    public static String decrypt(String data) {
        if(StringUtils.isEmpty(data)) {
            return data;
        }
        try {
            byte[] decryptFrom = parseHexStr2Byte(data);
            return new String(decryptFrom, "utf-8");
        } catch (Exception e) {
            logger.error("decrypt error"+e);
        }
        return null;
    }
    
    /**
     * 将16进制转换为二进制
     * 
     * @param hexStr
     * @return
     */
    private static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }
    
    /**
     * 小数                    -> 百分比
     * 122      整数无效 返回 null
     * .23      无效  返回 null
     * 0.23     -> 23%
     * 0.235    -> 23.5%
     * 1.235    -> 123.5%
     * 0.02     -> 2%
     * 0.002    -> 0.2%
     */
    public static String toPercentage(String number) {
        if(!number.matches("[0-9]+\\.[0-9]+")) {
            return number;
        }
        String result = null;
        String tmp = null;
        try {
            String[] split = number.split("\\.");
            tmp = split[1];
            if(split[0].startsWith("0") && tmp.length()>2) {
                result = tmp.substring(0, 2) + "." + tmp.substring(2, tmp.length()) + "%";
            }
            if(split[0].startsWith("0") && tmp.length()<=2) {
                result = tmp.substring(0, 2) + "%";
            }
            if(!split[0].startsWith("0") && tmp.length()>2) {
                result = split[0] + tmp.substring(0, 2) + "." + tmp.substring(2, tmp.length()) + "%";
            }
            if(!split[0].startsWith("0") && tmp.length()<=2) {
                result = split[0] + tmp.substring(0, 2) + "%";
            }
            if(result.matches("0[0-9]+\\.[0-9]+%")) {
                result = result.substring(1, result.length());
            }
            
        }catch(Exception e) {
            result = number;
        }
        return result;
    }
    
    public static void main(String[] args) {
        //String str = "7b22636f6e74656e74223a7b2264617461223a224d6f6e204d617920323820323031382032303a30303a313820474d542b303830302028e4b8ade59bbde6a087e58786e697b6e997b429222c2266726f6d41646472657373223a22464345724e6f59317a414447724a38567972474a775041484a7a6469487665713837222c227369676e6174757265223a226536373734636361613833363035656537336365303165343536653735666665356264623031393265613362643431386433353737323263386534666439653231636437666431363838646366633164646334333531633938666365313039376666643465623865333133383435353532356466356663366533656662346337227d7d";
        String str = "7b22636f6e74656e74223a7b2264617461223a22467269204a756e20303120323031382032313a33323a343820474d542b303830302028e4b8ade59bbde6a087e58786e697b6e997b429222c2266726f6d41646472657373223a22464345724e6f59317a414447724a38567972474a775041484a7a6469487665713837222c227369676e6174757265223a223739636365376632336238333232326163316538663063616137643537343837326437386162616334316331326236616565613039366135633662303266303230613633643138626364356664386261656336643162323034343365326133313736306563636661383033313730616561333230373538643034646239326631227d7d";
        System.out.println(decrypt(str));
    }
    
}
