package com.io.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;


/**
 * 使用一个 Buffer 完成文件读取、写入
 * @author wendongchao
 * @ClassName NIOFileChannel03
 * @Date 2021/10/8 18:08
 */
public class NIOFileChannel03 {
    public static void main(String[] args) throws Exception {

        FileInputStream fileInputStream = new FileInputStream("/Users/wendongchao/code/idea/NettyStudy/doc/file01.txt");
        FileChannel fileChannel01 = fileInputStream.getChannel();
        // 没有文件会自动创建文件
        FileOutputStream fileOutputStream = new FileOutputStream("/Users/wendongchao/code/idea/NettyStudy/doc/file02.txt");
        FileChannel fileChannel02 = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(512);

        while (true) { //循环读取

            //这里有一个重要的操作，一定不要忘了
            /*
            public final Buffer clear() {
                position = 0;
                limit = capacity;
                mark = -1;
                return this;
            }
            */
            byteBuffer.clear(); //清空 buffer，重要
            int read = fileChannel01.read(byteBuffer);// 从通道中读取数据到缓冲区
            System.out.println("read = " + read);
            if (read == -1) { //表示读完
                break;
            }

            //将 buffer 中的数据写入到 fileChannel02--2.txt
            byteBuffer.flip();
            fileChannel02.write(byteBuffer);// 从缓存去写数据到通道
        }

        //关闭相关的流
        fileInputStream.close();
        fileOutputStream.close();
    }
}
