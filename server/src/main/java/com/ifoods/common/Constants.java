package com.ifoods.common;

public class Constants {
    private Constants(){}
    
    public static final String PRIVILEGE = "privilege";
    
    /*语言设置 */
    /**获得语言类型的key*/
    public final static String LANGUAGE_KEY = "i-msg-type";
    
    /**英语*/
    public final static String EN_US = "en_us";
    /**简体中文*/
    public final static String DEAULT_LANGUAGE = "zh_cn";
    
    /** 数据记账的类型 */
    public final static String RECORDTYPE = "record";
    
    /** 数据记账类型 --> 地址 */
    public final static String RECORDTYPE_ADDRESS = "fromAddress";
    /** 数据记账类型 --> 内容 */
    public final static String RECORDTYPE_DATA = "data";
    
    /** 定时任务 */
    public final static String GET_BLOCK_TIMER = "0/9 * * * * ?";
    
}

