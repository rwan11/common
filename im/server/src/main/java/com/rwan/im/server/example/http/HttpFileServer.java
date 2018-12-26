package com.rwan.im.server.example.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author johnlog
 * @date 2018/12/25
 */
public class HttpFileServer {


    public static final String DEFAULT_URL = "com/rwan/im/server/example/http";

    public static void main(String[] args) {
        
        new HttpFileServer().run(8888,DEFAULT_URL);

    }

    public void run(final int port,final String url){
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

                            ch.pipeline().addLast("http-decoder", new HttpRequestDecoder());

                            ch.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65536));

                            ch.pipeline().addLast("http-encoder", new HttpResponseEncoder());

                            ch.pipeline().addLast("http-chunked", new ChunkedWriteHandler());

                            ch.pipeline().addLast("fileServerHandler", new HttpFileServerHandler(url));
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG,1024);

            ChannelFuture future = bootstrap.bind(port).sync();

            future.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            worker.shutdownGracefully();
            boss.shutdownGracefully();
        }
    }
}
