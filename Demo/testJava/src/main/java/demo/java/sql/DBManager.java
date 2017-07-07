package demo.java.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

import test.java.sql.ConnectionPool.PooledConnection;

public class DBManager {

	public static void main(String[] args) {
		 String sql = "....";
			ResultSet rs;
			PooledConnection conn = null;
			try {
				conn = DBManager.getConnection();
				rs = conn.executeQuery(sql);

				if (rs.next())
				return rs.getInt(1);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				conn.close();
			}
			return 0;

	}

	
	private static PooledConnection conn;
	private static ConnectionPool connectionPool;
	private static DBManager inst;

	public void close() {
		try {
			connectionPool.closeConnectionPool();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public DBManager() {
		if (inst != null)
			return;

		// TODO Auto-generated constructor stub

		String connStr = String.format("jdbc:mysql://%s:%d/%s", Config.getInstance().mysqlHost, Config.getInstance().mysqlPort,
				Config.getInstance().mysqlDB);
		connectionPool = new ConnectionPool("com.mysql.jdbc.Driver", connStr, Config.getInstance().mysqlUser, Config.getInstance().mysqlPassword);
		try {
			connectionPool.createPool();
			inst = this;
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static PooledConnection getConnection() {
		if (inst == null)
			new DBManager();

		try {
			
			conn = connectionPool.getConnection();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return conn;
	}
}
