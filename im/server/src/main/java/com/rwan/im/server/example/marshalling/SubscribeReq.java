package com.rwan.im.server.example.marshalling;

import java.io.Serializable;

/**
 * @author johnlog
 * @date 2018/12/25
 */
public class SubscribeReq  implements Serializable {
    private static final long serialVersionUID = -4002472807380918574L;


    private int subReqId;

    private String userName;

    private String email;

    @Override
    public String toString() {
        return "SubscribeReq{" +
                "subReqId=" + subReqId +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public int getSubReqId() {
        return subReqId;
    }

    public void setSubReqId(int subReqId) {
        this.subReqId = subReqId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
