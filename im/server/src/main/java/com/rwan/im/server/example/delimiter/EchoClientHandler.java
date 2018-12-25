package com.rwan.im.server.example.delimiter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author johnlog
 * @date 2018/12/24
 */
public class EchoClientHandler extends ChannelInboundHandlerAdapter {


    private int counter;


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

       for (int i = 0 ;i < 100 ;i++){

           ByteBuf msg = Unpooled.copiedBuffer("hello,I'm a stuent $_".getBytes());

           ctx.writeAndFlush(msg);
       }

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {


        System.out.println("this is "+(++counter)+ " server return msg : " + (String)msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        cause.printStackTrace();
        ctx.close();
    }
}
