package com.rwan.im.server.example.serial;

import java.io.Serializable;

/**
 * @author johnlog
 * @date 2018/12/24
 */
public class SubscribeRsp implements Serializable {
    private static final long serialVersionUID = 4019085189065948253L;

    private int subReqId;

    private int respCode;


    private String desc;


    public int getSubReqId() {
        return subReqId;
    }

    public void setSubReqId(int subReqId) {
        this.subReqId = subReqId;
    }

    public int getRespCode() {
        return respCode;
    }

    public void setRespCode(int respCode) {
        this.respCode = respCode;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "SubscribeRsp{" +
                "subReqId=" + subReqId +
                ", respCode=" + respCode +
                ", desc='" + desc + '\'' +
                '}';
    }
}
