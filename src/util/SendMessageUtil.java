package util;

import message.HeartMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

import com.google.gson.Gson;
/**
 * ������Ϣ�� ������
 * @author zsg
 *
 */
public class SendMessageUtil {
	private Gson gson;
	String heartMessage;
	public SendMessageUtil() {
		gson=new Gson();
		heartMessage=gson.toJson(new HeartMessage())+"#";
	}
	/**
	 * ����������Ϣ
	 * @param ctx
	 */
	public void sendHeartMessage(ChannelHandlerContext ctx){
		ByteBuf msgbuf = Unpooled.copiedBuffer(heartMessage.getBytes());
		ctx.writeAndFlush(msgbuf);
	}
}
