package com.io.nio.GroupChat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/*
客户端：
    连接服务器
    发送消息
    接收消息
 */
public class GroupChatClient {
    // 定义属性
    private final String host = "127.0.0.1";//服务器IP
    private final int port = 6667;          //服务器端口
    private Selector selector;
    private SocketChannel socketChannel;
    private String username;

    // 构造器，完成初始化工作
    public GroupChatClient() throws IOException {
        // 获取Selector
        selector = Selector.open();
        // 连接服务器，获取SocketChannel
        socketChannel = SocketChannel.open(new InetSocketAddress(host, port));
        // 设置非阻塞
        socketChannel.configureBlocking(false);
        // socketChannel注册到selector上，并设置OP_READ事件
        socketChannel.register(selector, SelectionKey.OP_READ);
        username = socketChannel.getLocalAddress().toString().substring(1);
        System.out.println(username + "客户端准备就绪...");
    }

    /**
     * 向服务器发送消息
     *
     * @param info
     */
    public void SendInfo(String info) {
        info = username + ":    " + info;
        try {
            // buffer内容写入到通道
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从服务器读取消息
     */
    public void ReadInof() {
        try {
            int readChannel = selector.select();//如果没有事件就会阻塞再这里,所以当有事件发生时readchannel都不会小于0...
            if (readChannel > 0) { // 有事件发生的通道
                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                //有可能多个通道同时发消息
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    //如果是可读事件
                    if (key.isReadable()) { // 读事件
                        //得到相关通道
                        SocketChannel channel = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        // 从通道中读取数据到buffer中
                        channel.read(buffer);
                        String msg = new String(buffer.array());
                        System.out.println(msg.trim());
                    }
                }
                /*
                当前的事件处理完后，需要从selector中去掉，事件再次发生后
                ，selector中会有新的SelectionKey
                 */
                keyIterator.remove();
            } else {
                System.out.println("没有可用的通道...");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws IOException {
        //启动客户端
        GroupChatClient chatClient = new GroupChatClient();
        //启动一个线程来操作
        new Thread(() -> {
            while (true) {
                chatClient.ReadInof();
                //每隔三秒读取一次服务端数据
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String str = scanner.next();
            chatClient.SendInfo(str);
        }
    }
}
