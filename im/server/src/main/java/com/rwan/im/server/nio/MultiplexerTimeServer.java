package com.rwan.im.server.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**

 * @author johnlog
 * @date 2018/12/21
 */
public class MultiplexerTimeServer implements Runnable {


    private Selector selector;

    private ServerSocketChannel serverSocketChannel;

    private volatile boolean stop;

    public MultiplexerTimeServer(int port) {

        try {
            selector = Selector.open();

            serverSocketChannel = ServerSocketChannel.open();

            serverSocketChannel.configureBlocking(false);

            serverSocketChannel.socket().bind(new InetSocketAddress(port),1024);

            serverSocketChannel.register(selector,SelectionKey.OP_ACCEPT);

            System.out.println("The time server start port:"+ port);

        } catch (IOException e) {
            e.printStackTrace();

            System.exit(1);
        }

    }


    public void setStop(boolean stop) {
        this.stop = stop;
    }

    public void run() {


        while (!stop){

            try {

                selector.select(1000);

                Set<SelectionKey>  selectionKeys = selector.selectedKeys();

                Iterator<SelectionKey> it = selectionKeys.iterator();

                SelectionKey key = null;

                while (it.hasNext()){

                    key = it.next();
                    it.remove();
                    try {
                        handleInput(key);
                    }catch (Exception e){
                        if (key != null){
                            key.cancel();
                            if (key.channel() != null){
                                key.channel().close();
                            }
                        }
                    }

                }

            }catch (Exception e){
                e.printStackTrace();

            }
        }

        if (selector != null){
            try {
                selector.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    public void handleInput(SelectionKey key) throws IOException {

        if (key.isValid()){

            if (key.isAcceptable()){

                  ServerSocketChannel ssc = (ServerSocketChannel) key.channel();

                  SocketChannel sc = ssc.accept();

                  sc.configureBlocking(false);

                  sc.register(selector,SelectionKey.OP_READ);


            }


            if (key.isReadable()){
                SocketChannel sc = (SocketChannel) key.channel();


            }
        }
    }
}
