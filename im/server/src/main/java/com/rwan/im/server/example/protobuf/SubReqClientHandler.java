package com.rwan.im.server.example.protobuf;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author johnlog
 * @date 2018/12/25
 */
//@ChannelHandler.Sharable
public class SubReqClientHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

         for (int i = 0 ;i < 10 ;i++){

             ctx.write(createReq(i));
         }

         ctx.flush();
    }

    private SubscribeReq.subscribereq createReq(int reqId){

        SubscribeReq.subscribereq.Builder builder = SubscribeReq.subscribereq.newBuilder();

        builder.setName("lining");

        builder.setId(reqId);

        builder.setEmail("5356@qq.com");

        return builder.build();
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

//        SubscribeRsp.subscribersp rsp = (SubscribeRsp.subscribersp)msg;

        System.out.println(" receive server return msg: "+ msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {


        cause.printStackTrace();

        ctx.close();
    }
}
