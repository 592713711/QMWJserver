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
	 * �ͻ������ӷ�����ʱִ��
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelActive(ctx);
		System.out.println("�ͻ���" + ctx.channel().remoteAddress().toString()
				+ "�����ӷ�����");
		// ������ͨ����ӵ�ͨ��������
		MsgHandleService.channelGroup.add(ctx.channel());

	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		// System.out.println("�յ��ͻ��˷�������Ϣ");
		String body = (String) msg;
		MsgHandleService.getInstance().handleMessage(body, ctx);

	}

	/**
	 * ����˽��ܿͻ��˷�������Ϣ
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext arg0, String arg1)
			throws Exception {
		System.out.println("�յ��ͻ��˷�������Ϣ2");

	}

	/**
	 * �ͻ��˶Ͽ�����ʱ�ص�
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelInactive(ctx);
		System.out.println("channelInactive��"
				+ ctx.channel().remoteAddress().toString() + "�Ͽ�����");
		NettyChannelMap.remove(ctx);
	}

	/**
	 * һ��ʱ��δ���ж�д���� �ص�
	 */
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		// TODO Auto-generated method stub
		super.userEventTriggered(ctx, evt);

		if (evt instanceof IdleStateEvent) {

			IdleStateEvent event = (IdleStateEvent) evt;

			if (event.state().equals(IdleState.READER_IDLE)) {
				// δ���ж�����
				System.out.println("READER_IDLE");
				// ��ʱ�ر�channel
				ctx.close();

			} else if (event.state().equals(IdleState.WRITER_IDLE)) {

			} else if (event.state().equals(IdleState.ALL_IDLE)) {
				// δ���ж�д
				System.out.println("ALL_IDLE");
				// ����������Ϣ
				MsgHandleService.getInstance().sendMsgUtil
						.sendHeartMessage(ctx);

			}

		}
	}

}
