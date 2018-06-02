package com.ifoods.common.model;

import com.alibaba.fastjson.JSON;

/**
 * 
 * @author zhenghui.li
 * @date 2018年4月17日
 */
public class ResponseModel {
    /**
     * 返回码
     */
    private String code;

    /**
     * 返回描述
     */
    private String msg;

    /**
     * 返回请求数据
     */
    private Object data;

    public ResponseModel() {
        code = CodeMsg.SUCCESS.key();
        msg = CodeMsg.SUCCESS.value();
        data = "";
    }
    
    /**
     * @param CodeMsg
     */
    public ResponseModel(CodeMsg codeMsg) {
        code = codeMsg.key();
        msg = codeMsg.value();
        data = "";
    }

    public ResponseModel(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseModel(String code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    
    /**
     * @param CodeMsg
     */
    public ResponseModel(CodeMsg codeMsg, Object data) {
        this.code = codeMsg.key();
        this.msg = codeMsg.value();
        this.data = data;
    }

    /**
     * @param string
     */
    public ResponseModel(String msg) {
        this.code = CodeMsg.SUCCESS.key();
        this.msg = msg;
        this.data = "";
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
    
    public void setCode(CodeMsg codeMsg) {
        this.code = codeMsg.key();
        this.msg = codeMsg.value();
    }
}
