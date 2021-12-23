package com.io.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {
    public static void main(String[] args) throws Exception {
        //BossGroup and workerGroup
        //创建两个线程组，都是循环
        //bossgroup 和 workergroup 含有子线程（EventLoop）的个数
        EventLoopGroup bossgroup = new NioEventLoopGroup();     //处理连接请求，其余交给workergroup处理
        EventLoopGroup workergroup = new NioEventLoopGroup();   //
        try {
            //创建服务器端的启动对象，并配置启动参数
            ServerBootstrap bootstrap = new ServerBootstrap();
            //使用链式编程设置
            bootstrap.group(bossgroup,workergroup)                          //设置两个现场组
                    .channel(NioServerSocketChannel.class)                  //使用NioSocketChannel,作为服务器的通道实现
                    .option(ChannelOption.SO_BACKLOG ,128)              //设置线程等待的个数
                    .childOption(ChannelOption.SO_KEEPALIVE,true)       //设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        //给pipeLine设置处理器
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyServerHandler());
                        }
                    });     //给workergroup 的pipeline 设置处理器
            System.out.println("服务器准备好了...");
            //绑定一个端口并且同步
            //启动服务器(并绑定服务器)
            ChannelFuture cf = bootstrap.bind(6669).sync();
            //给cf注册监听器，监控关心 的事件
            cf.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if(cf.isSuccess()){
                        System.out.println("监听端口6669成功");
                    }else{
                        System.out.println("监听端口6669失败");
                    }
                }
            });

            //对关闭听到进行监听,有关闭就会监听到
            cf.channel().closeFuture().sync();
        }finally {
            //优雅的关闭
            bossgroup.shutdownGracefully();
            workergroup.shutdownGracefully();
        }
    }
}
