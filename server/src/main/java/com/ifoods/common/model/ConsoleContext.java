package com.ifoods.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import lombok.Data;

/**
 *
 */
@Data
public class ConsoleContext implements Serializable {

    private static final long serialVersionUID = 1L;

    List<Long> accessableAppIdList = new ArrayList<Long>();

    /*
	public void access(Long targetAppId) throws DataAuthAccessException{
        if( !accessableAppIdList.contains(targetAppId)){
            throw new DataAuthAccessException();
        }
    }*/

    public List<Long> getAccessableAppIdList() {
        return accessableAppIdList;
    }

    public void setAccessableAppIdList(List<Long> accessableAppIdList) {
        this.accessableAppIdList = accessableAppIdList;
    }

    public ConsoleContext(){ }

    private String traceId;
    //private String reqId;

    private String languageType;

    private HttpServletRequest request;
}
