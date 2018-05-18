package com.lbcy.common.util;

/**
 * 
 * @author zhenghui.li
 * @date 2018年5月17日
 */
public class CommonUtils {

    /**
     * 16进制转utf-8
     * 
     * @return
     */
    public static String decrypt(String data) {
        try {
            byte[] decryptFrom = parseHexStr2Byte(data);
            return new String(decryptFrom, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
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
    
    public static void main(String[] args) {
        String str = "7b2268656865223a22546875204d617920313020323031382031383a32313a333720474d542b303830302028e4b8ade59bbde6a087e58786e697b6e997b429227d";
        System.out.println(decrypt(str));
    }
}
