package com.rwan.im.server.example.marshalling;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author johnlog
 * @date 2018/12/25
 */
@ChannelHandler.Sharable
public class SubscribeReqServerHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {


        SubscribeReq req = (SubscribeReq)msg;

        if ("lining".equalsIgnoreCase(req.getUserName())){

            System.out.println("the client send req msg: " + req);
            ctx.writeAndFlush(rsp(req.getSubReqId()));
        }
    }

    private SubscribeRsp rsp(int reqId){

        SubscribeRsp rsp = new SubscribeRsp();
        rsp.setSubReqId(reqId);
        rsp.setRspCode(0);
        rsp.setDesc("hello kit!");

        return rsp;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        cause.printStackTrace();

        ctx.close();
    }
}
