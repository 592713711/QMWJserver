package server;

import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Map;

public class NettyChannelMap {
	// 存放 用户id 和 连接客户端的通道
	public static Map<Integer, ChannelHandlerContext> userMap = new HashMap<Integer, ChannelHandlerContext>();
	// 存放 客户端ip地址 和 用户id 
	public static Map<String, Integer> clientMap = new HashMap<String, Integer>();
    
	
	public static void add(int userid,ChannelHandlerContext ctx){
		userMap.put(userid, ctx);
		clientMap.put(ctx.channel().remoteAddress().toString(), userid);
    }
    public static ChannelHandlerContext get(Integer clientId){
       return userMap.get(clientId);
    }
    public static void remove(ChannelHandlerContext ctx){
    	String clientIp=ctx.channel().remoteAddress().toString();
    	if(clientIp!=null){
		int id=clientMap.get(clientIp);
		
		userMap.remove(id);
		clientMap.remove(clientIp);
		System.out.println("map中删除了 用户："+id);
    	}
    }
 
}
