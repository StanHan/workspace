package demo.java.sql.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import demo.java.sql.ResultSetUtils;
import demo.javax.sql.DataSourceDemo;

/**
 * 数据校验
 *
 */
public class DataVerify {
    DataSource xyj = DataSourceDemo.xyj;
    DataSource mycat = DataSourceDemo.mycat;

    public static void main(String[] args) {
        DataVerify dataVerify = new DataVerify();
        dataVerify.validAO(1046245);
    }

    void validAO(long userid) {
        StringBuilder xyjSQL = new StringBuilder("");
        xyjSQL.append(
                "(select 's_user_finance', id, `customer_id` as 'value' from s_user_finance where user_id=? ORDER BY id DESC)");
        xyjSQL.append(" union all ");
        xyjSQL.append(
                " (select 's_user_product_audit_status', id, audit_status as 'value' from s_user_product_audit_status where user_id=? ORDER BY id DESC) ");
        xyjSQL.append(" union all ");
        xyjSQL.append(" (select 's_user_sign', id, `status` from s_user_sign where user_id=? ORDER BY id DESC) ");

        StringBuilder aoSQL = new StringBuilder();
        aoSQL.append(
                " (select 's_user_finance', id, `customer_id` as 'value' from AO.s_user_finance where user_id=? ORDER BY id DESC)  ");
        aoSQL.append(" union all ");
        aoSQL.append(
                " (select 's_user_product_audit_status', id, audit_status as 'value' from AO.s_user_product_audit_status where user_id=? ORDER BY id DESC) ");
        aoSQL.append(" union all ");
        aoSQL.append(" (select 's_user_sign', id, `status` from AO.s_user_sign where user_id=? ORDER BY id DESC) ");

        try (Connection xyjConn = xyj.getConnection();
                Connection mycatConn = mycat.getConnection();
                PreparedStatement xyjPStatement = xyjConn.prepareStatement(xyjSQL.toString());
                PreparedStatement aoPStatement = mycatConn.prepareStatement(aoSQL.toString());) {

            xyjPStatement.setLong(1, userid);
            xyjPStatement.setLong(2, userid);
            xyjPStatement.setLong(3, userid);

            System.out.println(xyjSQL.toString());
            ResultSet xyjRS = xyjPStatement.executeQuery();
            List<Object[]> xyjList = ResultSetUtils.buildResultSetWithLabel(xyjRS);
            xyjRS.close();

            aoPStatement.setLong(1, userid);
            aoPStatement.setLong(2, userid);
            aoPStatement.setLong(3, userid);
            System.out.println(aoSQL.toString());
            ResultSet aoRS = aoPStatement.executeQuery();
            List<Object[]> aoList = ResultSetUtils.buildResultSetWithLabel(aoRS);
            aoRS.close();

            verifyData(xyjList, aoList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 校验list
     * 
     * @param srcList
     * @param tarList
     */
    public static void verifyData(List<Object[]> srcList, List<Object[]> tarList) {
        if (srcList.size() != tarList.size()) {
            System.err.println("src.size=" + srcList.size() + ",tar.size=" + tarList.size());
        }

        String[] srcLabels = (String[]) srcList.get(0);
        String[] tarLabels = (String[]) tarList.get(0);

        for (int i = 1; i < srcList.size() && i < tarList.size(); i++) {
            Object[] src = srcList.get(i);
            Object[] tar = tarList.get(i);
            Object srcID = src[0];
            StringBuilder sb = new StringBuilder(srcID.toString() + ": ");
            boolean isOk = true;
            for (int j = 0; j < src.length; j++) {
                if (!String.valueOf(src[j]).equals(String.valueOf(tar[j]))) {
                    sb.append(" src.").append(srcLabels[j]).append("=").append(src[j]);
                    sb.append(" tar.").append(tarLabels[j]).append("=").append(tar[j]);
                    isOk = false;
                }
            }
            if (!isOk) {
                System.err.println(sb.toString());
            }
        }
    }

}
