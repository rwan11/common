package com.rwan.im.server.nio;

/**
 *
 * @author johnlog
 * @date 2018/12/21
 */
public class TimeServer {


    public static void main(String[] args) {


        MultiplexerTimeServer server = new MultiplexerTimeServer(8099);

        server.run();

        System.out.println("-------------------");
    }
}
