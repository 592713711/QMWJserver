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
	// ����������
	public void startServer(int port) {
		EventLoopGroup parentGroup = new NioEventLoopGroup();
		EventLoopGroup childrenGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(parentGroup, childrenGroup);
			serverBootstrap.channel(NioServerSocketChannel.class);
			// ��ӹ����߳�
			serverBootstrap.childHandler(new ChannelInitializer<Channel>() {
				@Override
				protected void initChannel(Channel arg0) throws Exception {
									
					///����������ʱ��   д��������ʱ��   ��дȫ������ʱ��
					arg0.pipeline().addLast("ping", new IdleStateHandler(25, 15, 10,TimeUnit.SECONDS));
					
					// �����ָ����������
					ByteBuf delimiter = Unpooled.copiedBuffer("#".getBytes());
					// ���ﵽ��󳤶���û�ҵ��ָ��� ���׳��쳣
					arg0.pipeline().addLast(
							new DelimiterBasedFrameDecoder(1000000, delimiter));
					// ����Ϣת�����ַ������� ����õ�����Ϣ�Ͳ���ת����
					// �����ʽ
					arg0.pipeline().addLast(
							new StringEncoder(Charset.forName("GBK")));
					// �����ʽ
					arg0.pipeline().addLast(
							new StringDecoder(Charset.forName("UTF-8")));
					arg0.pipeline().addLast(new MyServerHandle());

				}
			});
			// �������󶨶˿ڼ���
			ChannelFuture cf = serverBootstrap.bind(port).sync();
			System.out.println("����������");
			// �����������رռ���
			cf.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			parentGroup.shutdownGracefully();
			childrenGroup.shutdownGracefully();
			System.out.println("���������ŵ��ͷ����߳���Դ...");
		}
	}

	public static void main(String[] args) throws Exception {
		new MyServer().startServer(10245);

	}

}
