package demo.java.sql;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import javax.sql.DataSource;

import demo.javax.sql.DataSourceDemo;

public class JdbcDemo {
	public static String driver = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
	public static String url = "jdbc:microsoft:sqlserver://127.0.0.1:1433;DatabaseName=MyDB";
	public static String username = "sa";
	public static String password = "";

	public static Connection connection;
	public static Statement statement;
	public static PreparedStatement preparedStatement;
	public static ResultSet resultSet;
	public static ResultSetMetaData resultSetMetaData;

	public static void main(String s[]) {
//		init();
		// f2();
//		f9();
//		close();
	    demo();
	}
	
	static void demo(){
	    String select = "SELECT id, user_id, product_id, apply_id, send_at, create_at, update_at FROM cp_send";
	    String insert = "INSERT INTO cp_send_bak (id, user_id, product_id, apply_id, send_at, create_at, update_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
	    DataSource local = DataSourceDemo.local;
	    try(Connection connection = local.getConnection();
	            Statement statement = connection.createStatement();
	            PreparedStatement pStatement = connection.prepareStatement(insert);){
	        ResultSet rs = statement.executeQuery(select + " limit 1");
	        if(rs.next()){
	            pStatement.setObject(1, rs.getDouble(1));
	            pStatement.setObject(2, rs.getDouble(2));
	            pStatement.setObject(3, rs.getDouble(3));
	            pStatement.setObject(4, rs.getDouble(4));
	            pStatement.setObject(5, rs.getObject(5));
	            pStatement.setObject(6, rs.getObject(6));
                pStatement.setObject(7, rs.getObject(7));
                pStatement.executeUpdate();
	        }
	        rs.close();
	    } catch (SQLException e) {
            e.printStackTrace();
        }
	}

	/**
	 * 返回列表. 由于oracle存储过程没有返回值，它的所有返回值都是通过out参数来替代的，列表同样也不例外，但由于是集合，所以不能用一般的参数，
	 * 必须要用pagkage了.所以要分两部分，
	 * <p>
	 * CREATE OR REPLACE PACKAGE TESTPACKAGE AS TYPE Test_CURSOR IS REF CURSOR;
	 * end TESTPACKAGE;
	 * <p>
	 * CREATE OR REPLACE PROCEDURE TESTC(p_CURSOR out TESTPACKAGE.Test_CURSOR)
	 * IS BEGIN OPEN p_CURSOR FOR SELECT * FROM HYQ.TESTTB; END TESTC;
	 * 
	 * @throws SQLException
	 */
	public static void testProcedureC() throws SQLException {
		CallableStatement proc = null;
		proc = connection.prepareCall("{ call hyq.testc(?) }");
//		proc.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
		proc.execute();
		resultSet = (ResultSet) proc.getObject(1);

		while (resultSet.next()) {
			System.out.println("<tr><td>" + resultSet.getString(1) + "</td><td>" + resultSet.getString(2) + "</td></tr>");
		}
	}

	/**
	 * 有返回值的存储过程（非列表）
	 * <p>
	 * CREATE OR REPLACE PROCEDURE TESTB(PARA1 IN VARCHAR2,PARA2 OUT VARCHAR2)
	 * AS BEGIN SELECT INTO PARA2 FROM TESTTB WHERE I_ID= PARA1; END TESTB;
	 * 
	 * @throws SQLException
	 */
	public static void testProcedureB() throws SQLException {
		CallableStatement proc = null;
		proc = connection.prepareCall("{ call HYQ.TESTB(?,?) }");
		proc.setString(1, "100");
		proc.registerOutParameter(2, Types.VARCHAR);
		proc.execute();
		String testPrint = proc.getString(2);
		System.out.println("=testPrint=is=" + testPrint);
	}

	/**
	 * 无返回值的存储过程
	 * <p>
	 * CREATE OR REPLACE PROCEDURE TESTA(PARA1 IN VARCHAR2,PARA2 IN VARCHAR2) AS
	 * BEGIN INSERT INTO HYQ.B_ID (I_ID,I_NAME) S (PARA1, PARA2); END TESTA;
	 * </p>
	 * 
	 * @throws SQLException
	 */
	public static void testProcedureA() throws SQLException {
		CallableStatement proc = null;
		proc = connection.prepareCall("{ call HYQ.TESTA(?,?) }");
		proc.setString(1, "100");
		proc.setString(2, "TestOne");
		proc.execute();
	}

	public static void init() {
		try {
			Class.forName(driver);
			connection = DriverManager.getConnection(url, username, password);
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public static void createTable() {
		try {
			String strSql = "CREATE TABLE mytable (low int,high int,myavg AS (low + high)/2)";
			statement.execute(strSql);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public static void batchInsertData() {
		try {
			connection.setAutoCommit(false);
			statement.addBatch("insert into mytable values(11,1)");
			statement.addBatch("insert into mytable values(12,2)");
			statement.addBatch("insert into mytable values(13,3)");
			statement.addBatch("insert into mytable values(14,4)");
			statement.addBatch("insert into mytable values('a','z5')");
			statement.executeBatch();
			connection.commit();
			connection.setAutoCommit(true);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public static void insertData() {
		try {
			preparedStatement = connection.prepareStatement("insert into mytable values(33,33)");
			preparedStatement.execute();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public static void selectMetaData() {
		try {
			resultSet = statement.executeQuery("select * from mytable");
			resultSetMetaData = resultSet.getMetaData();

			System.out.println(resultSetMetaData.getColumnCount());
			for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
				System.out.print(resultSetMetaData.getColumnName(i) + "   ");
			}
			System.out.println();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public static void f6() {
		try {
			resultSet = statement.executeQuery("select * from mytable");
			while (resultSet.next()) {
				System.out.println(
						resultSet.getObject(1) + "  " + resultSet.getObject(2) + "   " + resultSet.getObject(3));
			}
			System.out.println();
			while (resultSet.previous()) {
				System.out.println(
						resultSet.getObject(1) + "  " + resultSet.getObject(2) + "   " + resultSet.getObject(3));
			}
			System.out.println();
			resultSet.absolute(3);
			System.out.println(resultSet.getObject(1) + "  " + resultSet.getObject(2) + "   " + resultSet.getObject(3));

		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public static void f7() {
		try {
			resultSet = statement.executeQuery("select * from mytable");

			resultSet.absolute(3);
			resultSet.deleteRow();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public static void f8() {
		try {
			resultSet = statement.executeQuery("select * from mytable");

			resultSet.absolute(3);

			resultSet.updateInt(1, 111);
			resultSet.updateInt(2, 222);
			resultSet.updateRow();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public static void f9() {
		try {
			resultSet = statement.executeQuery("select * from mytable");
			resultSet.moveToInsertRow();
			resultSet.updateInt(1, 1433);
			resultSet.updateInt(2, 1433);
			resultSet.insertRow();

			resultSet.moveToCurrentRow();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	void f10() {
		try {
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	void f11() {
		try {
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public static void close() {
		try {
			if (resultSet != null)
				resultSet.close();
			if (statement != null)
				statement.close();
			if (preparedStatement != null)
				preparedStatement.close();
			if (connection != null)
				connection.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

}
