package com.rwan.im.server.example.serial;

import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * @author johnlog
 * @date 2018/12/24
 */
public class UserInfo implements Serializable {


    private static final long serialVersionUID = 998054391541366993L;


    private int userId;

    private String username;


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public byte[] codec(){

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        byte[] value = this.username.getBytes();

        buffer.putInt(value.length);

        buffer.put(value);

        buffer.putInt(this.userId);

        buffer.flip();

        value = null;

        byte[] result = new byte[buffer.remaining()];

        buffer.get(result);

        return result;

    }
}
