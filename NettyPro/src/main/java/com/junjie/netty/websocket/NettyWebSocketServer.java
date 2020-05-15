package com.junjie.netty.websocket;

import com.junjie.netty.simple.NettyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

public class NettyWebSocketServer {
    public static void main(String[] args) {
        /**
         * 1、创建两个线程组，bossGroup/workerGroup
         * 2、bossGroup只是处理连接请求，真正和客户业务处理，会交给workerGroup
         * 3、两个都是无限循环
         */
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(8);
        try {
            //创建服务端的启动对象，配置参数
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //使用链式编程
            serverBootstrap.group(bossGroup, workerGroup)//设置两个线成组
                    .channel(NioServerSocketChannel.class)//使用NioServerSocketChannel作为服务端通道实现
                    .option(ChannelOption.SO_BACKLOG, 128)//设置线程队列等待连接的个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true)//设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() {//创建一个通道初始化对象
                        //向通道pipeline设置处理器
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //基于http协议，使用http编解码
                            ch.pipeline().addLast(new HttpServerCodec())
                                    .addLast(new HttpObjectAggregator(8192))
                                    /**
                                     * 对应websocket，他的数据是以帧的形式传递
                                     * 可以看到，WebSocketFrame下面有6个子类
                                     * 浏览器请求时：ws://localhhost:7000/hello 表示请求的uri
                                     * WebSocketServerProtocolHandler 核心功能是将http协议升级为ws协议，保持长连接
                                     */
                                    .addLast(new WebSocketServerProtocolHandler("/hello"))
                                    //自定义处理器
                                    .addLast(new TextWebSocketFrameHandler());

                        }
                    });//给WorkerGroup的EventLoop对应的管道设置处理器

            //绑定一个端口，并且同步启动服务
            ChannelFuture channelFuture = serverBootstrap.bind(7000).sync();
            //对关闭通道进行监听，当有关闭事件时进行关闭
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
