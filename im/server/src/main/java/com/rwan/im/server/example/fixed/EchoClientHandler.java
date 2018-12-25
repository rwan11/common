package com.rwan.im.server.example.fixed;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author johnlog
 * @date 2018/12/24
 */
public class EchoClientHandler  extends ChannelInboundHandlerAdapter {


    private  int counter;
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {


          String data = " this a fixed msg";
          for (int i = 0 ;i < 10 ;i++){

              byte[] body = new byte[100];
               System.arraycopy(data.getBytes(),0,body,0,data.getBytes().length);
              ByteBuf buf = Unpooled.copiedBuffer(body);

              ctx.writeAndFlush(buf);

          }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {


         String body = (String)msg;

        System.out.println(" this is "+(++counter) + " echo server send msg: "+ body + "end");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {


         cause.printStackTrace();

         ctx.close();
    }
}
