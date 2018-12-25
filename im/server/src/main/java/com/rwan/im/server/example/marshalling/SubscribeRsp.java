package com.rwan.im.server.example.marshalling;

import java.io.Serializable;

/**
 * @author johnlog
 * @date 2018/12/25
 */
public class SubscribeRsp implements Serializable {
    private static final long serialVersionUID = 1299119043680062977L;


    private int subReqId;

    private int rspCode;

    private String desc;

    @Override
    public String toString() {
        return "SubscribeRsp{" +
                "subReqId=" + subReqId +
                ", rspCode=" + rspCode +
                ", desc='" + desc + '\'' +
                '}';
    }

    public int getSubReqId() {
        return subReqId;
    }

    public void setSubReqId(int subReqId) {
        this.subReqId = subReqId;
    }

    public int getRspCode() {
        return rspCode;
    }

    public void setRspCode(int rspCode) {
        this.rspCode = rspCode;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
