package message.response;

import java.util.ArrayList;

import message.MessageTag;
import message.request.Request;
import model.Monitor;

/**
 * ˢ���б���Ӧ
 * @author zzc
 *
 */
public class RefreshListRes extends Response{
	public ArrayList<Monitor> monitorList;		//	�û��໤/���໤���б�
	public RefreshListRes(){
		tag=MessageTag.REFRESHLIST_RES;
	}
}
