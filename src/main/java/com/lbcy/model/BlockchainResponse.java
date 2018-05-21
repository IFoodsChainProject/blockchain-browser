package com.lbcy.model;

/**
 * 
 */
public class BlockchainResponse
{
    private String Action;

    private String Desc;

    private Integer Error;

    private String Version;

    private Block Result;

    public String getAction()
    {
        return Action;
    }

    public void setAction(String action)
    {
        Action = action;
    }

    public String getDesc()
    {
        return Desc;
    }

    public void setDesc(String desc)
    {
        Desc = desc;
    }

    public Integer getError()
    {
        return Error;
    }

    public void setError(Integer error)
    {
        Error = error;
    }

    public String getVersion()
    {
        return Version;
    }

    public void setVersion(String version)
    {
        Version = version;
    }

    public Block getResult()
    {
        return Result;
    }

    public void setResult(Block result)
    {
        Result = result;
    }
}
