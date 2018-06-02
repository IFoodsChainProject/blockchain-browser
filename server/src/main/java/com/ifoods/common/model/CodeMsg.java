package com.ifoods.common.model;

/**
 */
public enum CodeMsg {

    // --
    SUCCESS("0000", "SUCCESS"),
    /** 参数不能为空*/
    PARAMS_ISEMPTY("0001", "PARAMS_ISEMPTY"),
    /** 系统异常 */
    SYSTEM_ERROR("0002", "SYSTEM_ERROR"),
    
    /** 没有搜索到记录 */
    NO_SEARCH_RECORD("0003", "NO_SEARCH_RECORD"),
    
    ;

    private String key;
    private String value;

    private CodeMsg(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String value() {
        return value;
    }

    public String key() {
        return key;
    }

    public static CodeMsg getErrorTypeEnumByKey(String key) {
        for (CodeMsg status : CodeMsg.values()) {
            if (status.key.equals(key)) {
                return status;
            }
        }
        return null;
    }

    public static String getValueByKey(String key) {
        String value = "";
        CodeMsg errorTypeEnum = getErrorTypeEnumByKey(key);
        if (null != errorTypeEnum) {
            value = errorTypeEnum.value();
        }
        return value;
    }
}
