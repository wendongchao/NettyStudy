package com.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

/**
 * 客户端
 *
 * @Author wendongchao
 * @Date 2021/11/10 22:58
 */
public class NIOClient {
    public static void main(String[] args) throws IOException {
        //1、得到一个网络通道
        SocketChannel socketChannel = SocketChannel.open();
        //2、设置非阻塞
        socketChannel.configureBlocking(false);
        //3、提供服务器的ip和端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 6666);
        //4、链接服务器
        if (!socketChannel.connect(inetSocketAddress)) {
            while (!socketChannel.finishConnect()) {
                System.out.println("链接需要时间，客户端不会阻塞");
            }
        }
//        String str = "hello world!";
        // 输入内容
        Scanner scanner = new Scanner(System.in);
        String str;
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while (scanner.hasNext()) {
            str = scanner.next();
            System.out.println(str);
            buffer.put(str.getBytes());
            buffer.flip();
            socketChannel.write(buffer);
            buffer.clear();
        }
        //生成一个和数组一样大的buffer
//        ByteBuffer byteBuffer = ByteBuffer.wrap(str.getBytes());
//        //发送数据，将buffer写入channel
//        socketChannel.write(byteBuffer);

        //代码会停在这
        System.in.read();
    }
}

