package com.rwan.im.server.example.marshalling;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author johnlog
 * @date 2018/12/25
 */
public class SubscribeReqClientHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {


         for (int i = 0 ;i < 10 ;i++){
             ctx.write(createReq(i));
         }

         ctx.flush();
    }

    private SubscribeReq createReq(int reqId){

        SubscribeReq req = new SubscribeReq();
        req.setEmail("5356@qq.com");
        req.setSubReqId(reqId);
        req.setUserName("lining");

        return req;
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {


        System.out.println("receive server return msg: "+msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        cause.printStackTrace();
        ctx.close();
    }
}
