import model.User;

import com.google.gson.Gson;


public class Temp {
	public static void main(String[] args) {
		Gson gson=new Gson();
		User user=new User();
		String useString=gson.toJson(user);
		System.out.println(useString);
		User a=gson.fromJson(useString,User.class);
		System.out.println(a.id+"  "+a.status+"  "+a.username);
		a.username.endsWith("g");
		//������δ��ֵ  gsonת����Ϊ0   �ַ���δ��ֵ ת����Ϊnull 
	}
}


