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
 * 用来处理交互消息的服务类
 * 
 * @author zzc
 * 
 */
public class MsgHandleService {

	private static MsgHandleService service;
	// channel组 用来管理客户端连接的channel
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
	 * 根据客户端请求类型响应消息
	 */
	public void handleMessage(String msgJson, ChannelHandlerContext ctx) {
		Request message = gson.fromJson(msgJson, Request.class);
		System.out.println("收到消息" + msgJson);
		switch (message.tag) {
		case MessageTag.LOGIN_REQ:
			// 登陆请求
			doLogin(msgJson, ctx);
			break;
		}
	}

	/**
	 * 处理登陆请求
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
					// 登陆成功
					user.id = rs.getInt("id");
					user.username = rs.getString("username");
					user.status = rs.getInt("status");
					RefreshListRes refreshListRes = new RefreshListRes();
					refreshListRes.monitorList = userDao
							.getMonitorsById(user.id);
					// 初始化响应中的变量
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
			// 自动登录不发送用户信息和好友信息
			loginResponse.isSuccess = true;
			user.id = loginRequest.from_id;
		}

		// 向客户端发送登陆响应
		String msg = gson.toJson(loginResponse) + "#";
		ByteBuf msgbuf = Unpooled.copiedBuffer(msg.getBytes());
		ctx.writeAndFlush(msgbuf);

		// 如果登陆成功 则将连接通道放入userMap中（即 在线） 并发送该用户的缓存信息
		if (loginResponse.isSuccess && user.id != 0) {
			System.out.println("用户 " + user.id + "登陆成功" + "	客户端:"
					+ ctx.channel().remoteAddress().toString());
			NettyChannelMap.add(user.id, ctx);
			// 发送缓存信息

		}

	}

}
