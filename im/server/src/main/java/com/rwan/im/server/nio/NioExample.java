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
 * @Author: apple
 * @Date: 2020/9/15
 * @Version 2.0
 */
public class NioExample {


    public static void main(String[] args) throws IOException {

        /*Selector selector = Selector.open();

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        serverSocketChannel.configureBlocking(false);

        serverSocketChannel.socket().bind(new InetSocketAddress(8888));

        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true){


            selector.select(1000);

            Set<SelectionKey> keys = selector.selectedKeys();

            Iterator<SelectionKey> it = keys.iterator();

            while (it.hasNext()){

                SelectionKey key = it.next();

                it.remove();
                if (key.isAcceptable()){

                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                }
                if (key.isReadable()){

                }
            }
            SocketChannel socketChannel = serverSocketChannel.accept();

        }*/

        printSize();
    }



    public static void printSize(){

        String msg = "{\"alertLevelCode\":\"p1\",\"applicationLabel\":\"ec-riskcenter\",\"configName\":\"风控中心dubbo最大耗时监控\",\"id\":21,\"inhibitTime\":300,\"latestTimestamp\":1600137026,\"notifyObjectId\":1,\"ruleList\":[{\"alertConfigId\":21,\"expression\":\"max by (instance,interfaceName,method) (rpc_provider_response_seconds_max{application=\\\"ec-riskcenter\\\"})\",\"logicalSymbol\":\"or\",\"metricId\":16,\"metricName\":\"dubbo接口耗时(S)\",\"relationOperator\":\"ge\",\"ruleId\":33,\"sourceType\":1,\"status\":0,\"threshold\":0.2000,\"timePeriod\":2,\"timePeriodUnit\":\"m\"}],\"status\":0}";

        byte[] data = msg.getBytes();

        System.out.println(data.length);
    }
}
