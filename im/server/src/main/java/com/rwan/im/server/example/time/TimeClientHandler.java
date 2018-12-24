package com.rwan.im.server.example.time;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author johnlog
 * @date 2018/12/24
 */
public class TimeClientHandler  extends ChannelInboundHandlerAdapter {


    private  int counter;
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {


        ByteBuf message = null;

        String data = "query time";
        for (int i = 0 ;i < 100 ;i++){

            message = Unpooled.buffer(data.length());
            message.writeBytes(data.getBytes());

            ctx.writeAndFlush(message);

//            try {
//                ctx.writeAndFlush(data);
//            }catch (Exception e){
//                e.printStackTrace();
//            }

        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        System.out.println("----------------------client----read----");
        ByteBuf buf = (ByteBuf)msg;

        byte[] req = new byte[buf.readableBytes()];

        buf.readBytes(req);

        String body = new String(req,"utf-8");

        System.out.println("the client receive msg:"+body +","+(++counter));
    }



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        cause.printStackTrace();

        ctx.close();
    }
}
