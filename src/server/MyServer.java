package server;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;

import message.response.Response;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

public class MyServer {
	// 启动服务器
	public void startServer(int port) {
		EventLoopGroup parentGroup = new NioEventLoopGroup();
		EventLoopGroup childrenGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(parentGroup, childrenGroup);
			serverBootstrap.channel(NioServerSocketChannel.class);
			// 添加工作线程
			serverBootstrap.childHandler(new ChannelInitializer<Channel>() {
				@Override
				protected void initChannel(Channel arg0) throws Exception {
									
					///读操作空闲时间   写操作空闲时间   读写全部空闲时间
					arg0.pipeline().addLast("ping", new IdleStateHandler(25, 15, 10,TimeUnit.SECONDS));
					
					// 创建分隔符缓冲对象
					ByteBuf delimiter = Unpooled.copiedBuffer("#".getBytes());
					// 当达到最大长度仍没找到分隔符 就抛出异常
					arg0.pipeline().addLast(
							new DelimiterBasedFrameDecoder(1000000, delimiter));
					// 将消息转化成字符串对象 下面得到的消息就不用转化了
					// 编码格式
					arg0.pipeline().addLast(
							new StringEncoder(Charset.forName("GBK")));
					// 解码格式
					arg0.pipeline().addLast(
							new StringDecoder(Charset.forName("UTF-8")));
					arg0.pipeline().addLast(new MyServerHandle());

				}
			});
			// 服务器绑定端口监听
			ChannelFuture cf = serverBootstrap.bind(port).sync();
			System.out.println("服务器启动");
			// 监听服务器关闭监听
			cf.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			parentGroup.shutdownGracefully();
			childrenGroup.shutdownGracefully();
			System.out.println("服务器优雅的释放了线程资源...");
		}
	}

	public static void main(String[] args) throws Exception {
		new MyServer().startServer(10245);

	}

}
