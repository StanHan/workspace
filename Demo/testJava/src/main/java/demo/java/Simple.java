package demo.java;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Simple {
	private int a;// 实例变量

	public int method() {
		int b = 0;// 局部变量
		a++;
		b = a;
		return b;
	}

	public static void main(String[] args) throws InterruptedException {
	   System.out.println(generateInsertQuerySQL());
	}
	
	static String generateInsertQuerySQL() {

        StringBuilder sb = new StringBuilder();
        sb.append("select uud.*, '' as invite_code, ifnull(cm.id, 19) as channel_id, ifnull(cm.channel_code, '9003') as channel_code \n");
        sb.append("from ( \n");
        sb.append("select u.id, u.register_date, ud.device_type, ud.device_no, ud.device_token, upper(ud.ifa) as idfa, upper(ud.imei) as imei, ud.android_id, ud.create_at, ud.update_at \n");
        sb.append("from s_user u left join s_user_device ud on u.id=ud.user_id \n");
        sb.append("where u.id >= ? and u.id < ? and u.id > ? \n");
        sb.append("group by u.id \n");
        sb.append("order by u.id \n");
        sb.append("limit ? ) uud left join s_user_channel uc on uud.id=uc.user_id left join s_channel_master cm on uc.channel_id=cm.id \n");

        return sb.toString();

    }
	
	
}


class SubClass extends SurperClass{
    @Override
    public void test(){
        System.out.println("SubClass.test()");
    }

    @Override
    public void test1() {
        // TODO Auto-generated method stub
        
    }
}

class SubClass2 extends SurperClass{

    @Override
    public void test1() {
        // TODO Auto-generated method stub
        
    }
}

abstract class SurperClass{
    public void test(){
        System.out.println("SurperClass.test()");
    }
    
    public abstract void test1();
}
