package message.response;

import message.Message;
import message.MessageTag;
import model.User;

/**
 * ��½��Ӧ
 * �����Զ���¼���½ʧ��  ����Ӧ��user��refreshListResΪ��
 * @author zsg
 *
 */
public class LoginResponse extends Response{
	public User user;		//��½�û�����Ϣ
	public boolean isSuccess;		//�Ƿ��½�ɹ�
	public RefreshListRes refreshListRes;	//ˢ���б���Ӧ
	
	public LoginResponse() {
		tag=MessageTag.LOGIN_RES;
	}
}
