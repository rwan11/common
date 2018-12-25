package com.rwan.im.server.example.protobuf;

import com.google.protobuf.InvalidProtocolBufferException;

/**
 * @author johnlog
 * @date 2018/12/25
 */
public class TestSubscribeReq {


    private static byte[] encode(SubscribeReq.subscribereq req){
        return req.toByteArray();
    }


    private static SubscribeReq.subscribereq decode(byte[] body) throws InvalidProtocolBufferException {
        return SubscribeReq.subscribereq.parseFrom(body);
    }

    private static SubscribeReq.subscribereq createSubscribeReq(){

        SubscribeReq.subscribereq.Builder builder = SubscribeReq.subscribereq.newBuilder();

        builder.setId(1);
        builder.setEmail("5356@qq.com");
        builder.setName("lining");

        return builder.build();
    }


    public static void main(String[] args) throws InvalidProtocolBufferException {


        SubscribeReq.subscribereq subscribereq = createSubscribeReq();

        System.out.println("------------"+ subscribereq.toString());


        SubscribeReq.subscribereq subscribereq2 = decode(encode(subscribereq));


        System.out.println("-----after-------"+ subscribereq2.toString());
    }
}
