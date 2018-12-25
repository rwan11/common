package com.rwan.im.server.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
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

                ByteBuffer readBuffer = ByteBuffer.allocate(1024);


                StringBuilder sb = new StringBuilder();

                int count;

                while ((count = sc.read(readBuffer)) > 0){
                    readBuffer.mark();
                   String message = decode(readBuffer);

                   sb.append(message);

                    if (message == null){
                        readBuffer.reset();
                    }
                }

                System.out.println(sb);
                if (!readBuffer.hasRemaining()){
                    readBuffer.clear();
                }

               /* int readBytes = sc.read(readBuffer);
                if (readBytes > 0){
                    readBuffer.flip();


                    byte[] bytes = new byte[readBuffer.remaining()];

                    readBuffer.get(bytes);

                    String body = new String(bytes,"UTF-8");

                    System.out.println("The Time server receive order:"+body);

                    String currenTime = "Query time server".equalsIgnoreCase(body) ? String.valueOf(System.currentTimeMillis()) : "bad order";

                    doWrite(sc,currenTime);
                }else if (readBytes < 0){
                    key.channel();
                    sc.close();
                }*/
            }
        }
    }

    private String decode(ByteBuffer readBuffer){

        try {
            byte[] bytes = new byte[readBuffer.remaining()];

            readBuffer.get(bytes);

            String body = new String(bytes,"UTF-8");

            return body;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
    private void doWrite(SocketChannel sc, String rsp) throws IOException {

            if (rsp != null){
                byte[] bytes = rsp.getBytes();

                ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
                writeBuffer.put(bytes);
                writeBuffer.flip();

                sc.write(writeBuffer);

            }
    }
}
