package com.rwan.im.server.example.serial;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author johnlog
 * @date 2018/12/24
 */
public class SubReqServerHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        SubscribeReq req = (SubscribeReq)msg;

        if ("lining".equalsIgnoreCase(req.getUserName())){

            System.out.println(" receive client msg:"+ req.toString());

            ctx.writeAndFlush(resp(req.getSubReqId()));
        }
    }


    private SubscribeRsp resp(int subReqId){

        SubscribeRsp rsp = new SubscribeRsp();
        rsp.setSubReqId(subReqId);
        rsp.setRespCode(0);
        rsp.setDesc("play netty");

        return rsp;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        cause.printStackTrace();

        ctx.close();
    }
}
