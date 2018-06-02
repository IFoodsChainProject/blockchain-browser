package com.ifoods.model;

/**
 * 
 * @author zhenghui.li
 * @date 2018年5月28日
 */
public enum TxType {

    KEEP_ACCOUNT("0", "记账交易"),
    ASSET_ISSUANCE("1", "资产发行交易"),
    ASSET_REGISTRATION("64", "资产注册交易"),
    TRANSFER("128", "转账交易"),
    DATA_RECORD("129", "数据记录"),
    
    ;
    
    private String key;
    private String value;

    private TxType(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String value() {
        return value;
    }

    public String key() {
        return key;
    }

    public static TxType getErrorTypeEnumByKey(String key) {
        for (TxType Status : TxType.values()) {
            if (Status.key.equals(key)) {
                return Status;
            }
        }
        return null;
    }

    public static String getValueByKey(String key) {
        String value = "";
        TxType errorTypeEnum = getErrorTypeEnumByKey(key);
        if (null != errorTypeEnum) {
            value = errorTypeEnum.value();
        }
        return value;
    }
}
