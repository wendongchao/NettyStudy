package com.io.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * 自定义一个handler需要继承 netty 规定好的适配器
 * 这样我们自定义的handler才能起作用
 */

/**
 * 入栈第一个必是解码decode（inbound）handler,之后才是其他的业务handler
 * 出栈第一个必是编码encode（outbound）handler,之后才是其他的业务handler
 * 入栈inbound解码，出栈outbound编码
 * 数据从socket出来到client或server就是入栈，从client或server到socket就是出栈
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    //读取数据，客户端发送的数据
    /*
    ChannelHandlerContext   上下文对象，含有管道，channel，地址
    Object msg              客户端发送的数据，需要转换
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

//        System.out.println("服务器读取线程："+Thread.currentThread().getName());
//        System.out.println("server ctx ="+ctx);
//        System.out.println("查看channel 和 pipeLine 的关系");
//        Channel channel = ctx.channel();
//        ChannelPipeline pipeline = ctx.pipeline();
//        //将msg转为ByteBuf
//        //ByteBuf是netty提供的，不是NIO的ByteBuffer
//        ByteBuf buf = (ByteBuf) msg;
//        System.out.println("客户端发送的数据：" + buf.toString(CharsetUtil.UTF_8));
//        System.out.println("客户端地址：" + ctx.channel().remoteAddress());

        // 有一个耗时任务 -> 异步执行 -> 提交该channel 对应的NIOEventLoop 的taskQueue中

//        Thread.sleep(10 * 1000);
//        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端2...",CharsetUtil.UTF_8));
//        System.out.println("go on...");

        // 解决方案1：用户程序自定义的普通任务

        //异步机制，阻塞的任务会放在taskQueue
//        ctx.channel().eventLoop().execute(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(10 * 1000);
//                ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端2...",CharsetUtil.UTF_8));//将数据写入到缓存，并刷新
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        System.out.println("go on...");

        // 解决方案2： 用户自定义定时任务 -> 该任务放到 scheduleTaskQueue中
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10 * 1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端2...",CharsetUtil.UTF_8));//将数据写入到缓存，并刷新
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 5, TimeUnit.SECONDS);

        System.out.println("go on...");

    }
    //数据读取完，
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //发送的数据要进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端1...",CharsetUtil.UTF_8));//将数据写入到缓存，并刷新
    }
    //处理异常，关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
