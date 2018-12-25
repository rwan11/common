package com.rwan.im.server.example.marshalling;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;


/**
 * @author johnlog
 * @date 2018/12/25
 */
public class SubscribeReqClient {


    public static void main(String[] args) {


        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {


                            ch.pipeline().addLast(MarshallingCodeFactory.createDecoder());
                            ch.pipeline().addLast(MarshallingCodeFactory.createEncoder());
                            ch.pipeline().addLast(new SubscribeReqClientHandler());
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
