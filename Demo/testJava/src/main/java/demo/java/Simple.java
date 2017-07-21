package demo.java;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
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
	    System.out.println(querySql());
	}
	
	public static Map<String,String> map = new HashMap<String,String>(){
        {
            put("id", "id");
            put("user_id", "user_id");
            put("product_mst_id", "product_id");
            put("product_audit_id", "apply_id");
            put("approve_time", "open_at");
            put("create_at", "create_at");
            put("update_at", "update_at");
        }
    };
	
	static String generateInsertQuerySQL() {

        StringBuilder sb = new StringBuilder();
        sb.append("select uud.*, '' as invite_code, ifnull(cm.id, 19) as channel_id, ifnull(cm.channel_code, '9003') as channel_code \n");
        sb.append("from ( \n");
        sb.append("select u.id, u.register_date, ud.device_type, ud.device_no, ud.device_token, upper(ud.ifa) as idfa, upper(ud.imei) as imei, ud.android_id, ud.create_at, ud.update_at \n");
        sb.append("from s_user u left join s_user_device ud on u.id=ud.user_id \n");
        sb.append("where u.id > ? \n");
        sb.append("group by u.id \n");
        sb.append("order by u.id \n");
        sb.append("limit ? ) uud left join s_user_channel uc on uud.id=uc.user_id left join s_channel_master cm on uc.channel_id=cm.id \n");

        return sb.toString();

    }
	
	static String querySql2() {
        String sql = "SELECT temp.*, cmt.channel_code as parent_channel_code " +
                " from (SELECT " +
                " pt.id,pt.mobilephone," +
                " pt.channel_id, cm.channel_code, " +
                " pt.orgin_id as parent_channel_id,pt.password, " +
                " pt.password_salt, pt.register_date as register_at," +
                " pt.status,pt.create_at,pt.update_at " +
                " from s_promoter pt " +
                " LEFT JOIN s_channel_master cm on pt.channel_id = cm.id) temp " +
                " left join s_channel_master cmt on temp.parent_channel_id = cmt.id" +
                " where temp.id > ? and temp.id <= ? ORDER BY temp.id limit ?;";
        return sql;
    }
	
	static String querySql() {
        String sql = "select * from (" +
                " select pp.*, spp.payment_stat, p.mobilephone " +
                " from (select temp.* from  (select MAX(salary), id from s_promoter_point GROUP BY date,channel_code) temp2  " +
                " LEFT JOIN s_promoter_point temp  on temp.id = temp2.id) pp " +
                " left join s_channel_master cmc on pp.channel_code=cmc.channel_code and cmc.`status`=1 " +
                " left join (select * from s_promoter GROUP BY channel_id, orgin_id) p on p.channel_id=cmc.id " +
                " left join s_channel_master cmp on p.orgin_id=cmp.id and cmp.`status`=1" +
                " left join " +
                " (" +
                " select max(payment_date) as payment_date, parent_channel_code, max(payment_stat) as payment_stat " +
                " from s_promoter_payment " +
                " where `status`=1 " +
                " group by date_format(payment_date, '%Y%m'), parent_channel_code" +
                " ) spp on cmp.channel_code=spp.parent_channel_code and DATE_FORMAT(pp.date,'%Y%m')=DATE_FORMAT(spp.payment_date,'%Y%m') " +
                "  order by pp.date, pp.channel_code ) t " +
                " where t.id > ? and t.id < ? ORDER BY t.id " +
                " limit ? " +
                " ;";
        return sql;
    }
	
	
}
