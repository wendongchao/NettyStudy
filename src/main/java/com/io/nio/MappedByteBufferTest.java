package com.io.nio;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * NIO 还提供了 MappedByteBuffer，可以让文件直接在内存（堆外的内存）中进行修改，而如何同步到文件由 NIO 来完成
 * 说明 1.MappedByteBuffer 可让文件直接在内存（堆外内存）修改,操作系统不需要拷贝一次
 * @Author wendongchao
 * @Date 2021/10/8 22:19
 */
public class MappedByteBufferTest {
    public static void main(String[] args) throws Exception {

        RandomAccessFile randomAccessFile = new RandomAccessFile("/Users/wendongchao/code/idea/NettyStudy/doc/file01.txt", "rw");
        //获取对应的通道
        FileChannel channel = randomAccessFile.getChannel();

        /**
         * 参数 1:FileChannel.MapMode.READ_WRITE 使用的读写模式
         * 参数 2：0：可以直接修改的起始位置
         * 参数 3:5: 是映射到内存的大小（不是索引位置），即将 file01.txt 的多少个字节映射到内存
         * 可以直接修改的范围就是 0-5,包左不包右
         * 实际类型 DirectByteBuffer
         */
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);

        // put什么类型，get什么类型
        mappedByteBuffer.put(0, (byte) 'H');
        mappedByteBuffer.put(3, (byte) '9');
//        mappedByteBuffer.put(5, (byte) 'Y');//IndexOutOfBoundsException

        System.out.println((char) mappedByteBuffer.get(0));
        System.out.println((char) mappedByteBuffer.get(1));
        System.out.println((char) mappedByteBuffer.get(2));
        System.out.println((char) mappedByteBuffer.get(3));
        System.out.println((char) mappedByteBuffer.get(4));

        randomAccessFile.close();
        System.out.println("修改成功~~");
    }
}
