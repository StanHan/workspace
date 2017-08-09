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
	    StringBuffer sql = new StringBuffer(
                //被认领超过5分钟无处理的进件,可以给客服重新抓取
                "SELECT upas.id FROM s_user_product_audit_status upas  INNER JOIN s_admin_task sat " +
                        "   ON upas.product_audit_id = sat.product_audit_id " +
                        "   WHERE sat.claim_status = 1 " +
                        "   AND upas.audit_status in (:auditStatus) " +
                        "   AND upas.product_audit_id IS NOT NULL " +
                        "   AND timestampdiff(MINUTE,sat.claim_time,now()) > 5 " +
                        "UNION ALL " +
                        //正常无人认领的进件
                        "SELECT upas.id FROM s_user_product_audit_status upas " +
                        "WHERE upas.admin_id IS NULL " +
                        "   AND upas.audit_status in (:auditStatus) " +
                        "   AND upas.product_audit_id IS NOT NULL");
	    System.out.println(sql);
	    Double a = 1.0;
	   Object b = a;
	   System.out.println(b);
	   double c = (double)b;
	   System.out.println(c);
//	   Integer c = (Integer) b;
//	   System.out.println(c);
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
