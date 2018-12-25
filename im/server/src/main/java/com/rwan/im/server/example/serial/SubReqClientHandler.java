package com.rwan.im.server.example.serial;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author johnlog
 * @date 2018/12/24
 */
public class SubReqClientHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

         for (int i = 0 ;i < 10 ;i++){

                ctx.write(subReq(i));
         }

         ctx.flush();
    }

    private SubscribeReq subReq(int i){

        SubscribeReq req = new SubscribeReq();
        req.setSubReqId(i);
        req.setUserName("lining");
        req.setPhoneName("123456");

        return req;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        System.out.println(" the server return msg: "+ msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        cause.printStackTrace();

        ctx.close();
    }
}
