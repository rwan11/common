package com.rwan.im.server.example.delimiter;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.Inet6Address;
import java.net.InetAddress;

/**
 * @author johnlog
 * @date 2018/12/24
 */
public class EchoServer {


    public static void main(String[] args) {


        EventLoopGroup boss = new NioEventLoopGroup();

        EventLoopGroup worker = new NioEventLoopGroup();


        try {
            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(boss,worker)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {

                            ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());

//                            ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024,delimiter));
                            ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(new EchoServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG,1024)
                    .childOption(ChannelOption.SO_KEEPALIVE,true);


            ChannelFuture future = bootstrap.bind(8888).sync();

            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {

            worker.shutdownGracefully();
            boss.shutdownGracefully();

        }


    }
}
