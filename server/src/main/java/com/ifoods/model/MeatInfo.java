package com.ifoods.model;

import java.io.Serializable;

import lombok.Data;

/**
 * 
 * @author zhenghui.li
 * @date 2018年6月3日
 */
@Data
public class MeatInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public String ver;                  //版本号
    public String ph;                   //ph值
    public String eCond;                //导电率
    public String res;                  //计算结论
    public String noMeatInfo;           //非牛肉信息, 如果是牛肉信息该值为空
    
}
