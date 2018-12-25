package com.rwan.im.server.example.fixed;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author johnlog
 * @date 2018/12/24
 */
public class EchoServerHandler  extends ChannelInboundHandlerAdapter {


    private  int counter;
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

         String body = (String)msg;

        System.out.println(" this is "+ (++counter) +" echo client send msg: "+ body +"end");

        byte[] data = new byte[100];

        System.arraycopy(body.getBytes(),0,data,0,body.length());
        ByteBuf buf = Unpooled.copiedBuffer(data);

        ctx.writeAndFlush(buf);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {


        cause.printStackTrace();

        ctx.close();
    }
}
