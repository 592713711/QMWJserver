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
		//若整型未赋值  gson转换后为0   字符串未赋值 转换后为null 
	}
}


