package com.rwan.im.server.example.protobuf;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author johnlog
 * @date 2018/12/25
 */
@ChannelHandler.Sharable
public class SubReqServerHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        SubscribeReq.subscribereq req = (SubscribeReq.subscribereq)msg;

        if ("lining".equalsIgnoreCase(req.getName())){

            System.out.println("the time server receive req: "+ req.toString());
            ctx.writeAndFlush(resp(req.getId()));
        }
    }


    private SubscribeRsp.subscribersp resp(int reqId){

        SubscribeRsp.subscribersp.Builder  builder = SubscribeRsp.subscribersp.newBuilder();
        builder.setSubReqId(reqId);
        builder.setRspCode(0);
        builder.setDesc("hello kit!");

        return builder.build();

    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        cause.printStackTrace();

        ctx.close();
    }
}
