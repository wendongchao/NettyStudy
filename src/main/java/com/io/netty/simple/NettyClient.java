package com.io.netty.simple;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {
    public static void main(String[] args) throws Exception {
        //客户端需要一个循环组
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            //创建一个客户端的启动对象 和服务器的不同
            Bootstrap bootstrap = new Bootstrap();
            //设置相关参数，链式编程
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)    //设置客户端通道实现类（发射）
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //加入自定义的处理器
                            ch.pipeline().addLast(new NettyClientHandler());
                        }
                    });
            System.out.println("客户端准备好了...");
            //启动客户端去连接服务器端(bind)
            //设计到netty的异步模型,sync(非阻塞)
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 6669).sync();
            //给关闭通道添加一个监听
            channelFuture.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully();
        }

    }
}
