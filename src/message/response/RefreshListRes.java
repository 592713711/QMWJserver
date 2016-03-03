package message.response;

import java.util.ArrayList;

import message.MessageTag;
import message.request.Request;
import model.Monitor;

/**
 * 刷新列表响应
 * @author zzc
 *
 */
public class RefreshListRes extends Response{
	public ArrayList<Monitor> monitorList;		//	用户监护/被监护的列表
	public RefreshListRes(){
		tag=MessageTag.REFRESHLIST_RES;
	}
}
