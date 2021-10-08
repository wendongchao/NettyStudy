package com.io.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 * 使用 FileChannel（通道）和方法 transferFrom，完成文件的拷贝
 * public long transferFrom(ReadableByteChannel src, long position, long count)，从目标通道中复制数据到当前通道
 * @Author wendongchao
 * @Date 2021/10/8 22:09
 */
public class NIOFileChannel04 {
    public static void main(String[] args) throws Exception {

        //创建相关流
        FileInputStream fileInputStream = new FileInputStream("d:\\clipboard.png");
        FileOutputStream fileOutputStream = new FileOutputStream("d:\\a2.png");

        //获取各个流对应的 FileChannel
        FileChannel sourceCh = fileInputStream.getChannel();
        FileChannel destCh = fileOutputStream.getChannel();

        //使用 transferForm 完成拷贝
        destCh.transferFrom(sourceCh, 0, sourceCh.size());

        //关闭相关通道和流
        sourceCh.close();
        destCh.close();
        fileInputStream.close();
        fileOutputStream.close();
    }
}
