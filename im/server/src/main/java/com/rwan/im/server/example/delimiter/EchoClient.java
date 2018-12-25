package com.rwan.im.server.example.delimiter;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;


/**
 * @author johnlog
 * @date 2018/12/24
 */
public class EchoClient {


    public static void main(String[] args) {


        EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();

        try {

            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {

                            ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());

//                            ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024,delimiter));

                            ch.pipeline().addLast(new StringDecoder());

                            ch.pipeline().addLast(new EchoClientHandler());
                        }
                    })
                    .option(ChannelOption.TCP_NODELAY,true);

            ChannelFuture future = bootstrap.connect("127.0.0.1",8888).sync();

            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}
