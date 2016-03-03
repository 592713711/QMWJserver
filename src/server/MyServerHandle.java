package server;

import java.io.RandomAccessFile;
import java.util.HashMap;

import message.HeartMessage;

import com.google.gson.Gson;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class MyServerHandle extends SimpleChannelInboundHandler<String> {

	public MyServerHandle() {
		// TODO Auto-generated constructor stub

	}

	/**
	 * 客户端连接服务器时执行
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelActive(ctx);
		System.out.println("客户端" + ctx.channel().remoteAddress().toString()
				+ "已连接服务器");
		// 将连接通道添加到通道管理中
		MsgHandleService.channelGroup.add(ctx.channel());

	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		// System.out.println("收到客户端发来的消息");
		String body = (String) msg;
		MsgHandleService.getInstance().handleMessage(body, ctx);

	}

	/**
	 * 服务端接受客户端发来的消息
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext arg0, String arg1)
			throws Exception {
		System.out.println("收到客户端发来的消息2");

	}

	/**
	 * 客户端断开连接时回调
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelInactive(ctx);
		System.out.println("channelInactive："
				+ ctx.channel().remoteAddress().toString() + "断开连接");
		NettyChannelMap.remove(ctx);
	}

	/**
	 * 一段时间未进行读写操作 回调
	 */
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		// TODO Auto-generated method stub
		super.userEventTriggered(ctx, evt);

		if (evt instanceof IdleStateEvent) {

			IdleStateEvent event = (IdleStateEvent) evt;

			if (event.state().equals(IdleState.READER_IDLE)) {
				// 未进行读操作
				System.out.println("READER_IDLE");
				// 超时关闭channel
				ctx.close();

			} else if (event.state().equals(IdleState.WRITER_IDLE)) {

			} else if (event.state().equals(IdleState.ALL_IDLE)) {
				// 未进行读写
				System.out.println("ALL_IDLE");
				// 发送心跳消息
				MsgHandleService.getInstance().sendMsgUtil
						.sendHeartMessage(ctx);

			}

		}
	}

}
