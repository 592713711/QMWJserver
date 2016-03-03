package server;

import java.io.File;
import java.io.RandomAccessFile;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import util.SendMessageUtil;

import message.MessageTag;
import message.request.LoginRequest;
import message.request.Request;
import message.response.LoginResponse;
import message.response.RefreshListRes;

import model.User;

import com.google.gson.Gson;

import dao.DataBase;
import dao.UserDao;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * ������������Ϣ�ķ�����
 * 
 * @author zzc
 * 
 */
public class MsgHandleService {

	private static MsgHandleService service;
	// channel�� ��������ͻ������ӵ�channel
	public static ChannelGroup channelGroup = new DefaultChannelGroup(
			GlobalEventExecutor.INSTANCE);
	public SendMessageUtil  sendMsgUtil;

	Gson gson;
	Connection conn;
	UserDao userDao;

	private MsgHandleService() {
		gson = new Gson();
		conn = DataBase.getConnect();
		userDao = new UserDao(conn);
		sendMsgUtil=new SendMessageUtil();
	}

	public static MsgHandleService getInstance() {
		if (service == null)
			service = new MsgHandleService();
		return service;
	}

	/**
	 * ���ݿͻ�������������Ӧ��Ϣ
	 */
	public void handleMessage(String msgJson, ChannelHandlerContext ctx) {
		Request message = gson.fromJson(msgJson, Request.class);
		System.out.println("�յ���Ϣ" + msgJson);
		switch (message.tag) {
		case MessageTag.LOGIN_REQ:
			// ��½����
			doLogin(msgJson, ctx);
			break;
		}
	}

	/**
	 * �����½����
	 * 
	 * @param msgJson
	 * @param ctx
	 */
	private void doLogin(String msgJson, ChannelHandlerContext ctx) {
		LoginRequest loginRequest = gson.fromJson(msgJson, LoginRequest.class);
		LoginResponse loginResponse = new LoginResponse();
		User user = new User();
		if (!loginRequest.isAuto) {
			ResultSet rs = userDao.getLogin(loginRequest.username,
					loginRequest.password);
			try {
				if (rs.next()) {
					// ��½�ɹ�
					user.id = rs.getInt("id");
					user.username = rs.getString("username");
					user.status = rs.getInt("status");
					RefreshListRes refreshListRes = new RefreshListRes();
					refreshListRes.monitorList = userDao
							.getMonitorsById(user.id);
					// ��ʼ����Ӧ�еı���
					loginResponse.isSuccess = true;
					loginResponse.user = user;
					loginResponse.refreshListRes = refreshListRes;

				}else 
					loginResponse.isSuccess=false;
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			// �Զ���¼�������û���Ϣ�ͺ�����Ϣ
			loginResponse.isSuccess = true;
			user.id = loginRequest.from_id;
		}

		// ��ͻ��˷��͵�½��Ӧ
		String msg = gson.toJson(loginResponse) + "#";
		ByteBuf msgbuf = Unpooled.copiedBuffer(msg.getBytes());
		ctx.writeAndFlush(msgbuf);

		// �����½�ɹ� ������ͨ������userMap�У��� ���ߣ� �����͸��û��Ļ�����Ϣ
		if (loginResponse.isSuccess && user.id != 0) {
			System.out.println("�û� " + user.id + "��½�ɹ�" + "	�ͻ���:"
					+ ctx.channel().remoteAddress().toString());
			NettyChannelMap.add(user.id, ctx);
			// ���ͻ�����Ϣ

		}

	}

}
