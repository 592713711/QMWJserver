package message.request;

import message.MessageTag;

/**
 * ��½����
 * @author zsg
 *
 */
public class LoginRequest extends Request{
	public String username;
	public String password;
	public boolean  isAuto=false;	//�Ƿ����Զ���¼  �Զ���¼������������
	public LoginRequest() {
		tag=MessageTag.LOGIN_REQ;
	}
	
	public LoginRequest(String username,String password){
		this.username=username;
		this.password=password;
		tag=MessageTag.LOGIN_REQ;
	}
	
	public LoginRequest(String username){
		this.username=username;
		tag=MessageTag.LOGIN_REQ;
		isAuto=true;
	}
}
