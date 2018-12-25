package com.rwan.im.server.example.delimiter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author johnlog
 * @date 2018/12/24
 */
public class EchoServerHandler extends ChannelInboundHandlerAdapter {


    private int counter;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {


         String body = (String)msg;

        System.out.println("this is "+ (++counter) + " time receive client:["+body+"]");

        body = body + "$_";

        ByteBuf echo = Unpooled.copiedBuffer(body.getBytes());

        ctx.writeAndFlush(echo);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {


        cause.printStackTrace();

        ctx.close();
    }
}
