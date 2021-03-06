package chinapub.cf.recommander.utils;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//import com.mysql.jdbc.Connection;
import java.sql.Connection;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
public class DBUtil {
	private static DataSource ds;
	public static String driverClassName = "com.mysql.jdbc.Driver";
	public static String url = "jdbc:mysql://localhost:3306/chinapubcf";
	public static String username = "root";     //修改为你自己用户名
	public static String password = "";      //修改为你自己密码
	public static  Connection getJDBCConnection()
	{
	    Connection conn = null;
	    try {
	        Class.forName(driverClassName);
	        conn = DriverManager.getConnection(url, username, password);
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return conn;
	}

	public static void createDataSource() throws Exception {
		try {
			Context context = new InitialContext();
			if (context == null) {
				throw new Exception("create context failed!");
			}
		     Context envCtx = (Context) context.lookup("java:comp/env");          
	          ds = (DataSource)envCtx.lookup("jdbc/chinapubcf");	
	            //conn = (Connection)ds.getConnection();
			//ds = (DataSource) context.lookup("java:comp/env/jdbc/chinapubcf");
			if (ds == null) {
				Thread.sleep(2000);
				ds = (DataSource) envCtx.lookup("jdbc/chinapubcf");
				if (ds == null) {
					throw new Exception("get datasource failed!");
				}
			}
		} catch (NamingException ne) {
			throw ne;
		} catch (Exception e) {
			throw e;
		}
	}

	public static DataSource getDataSource() {
		try {
			if (ds == null) {
				createDataSource();
			}
			return ds;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Connection getConnection(){
		DataSource dataSource = getDataSource();
		try {
			return dataSource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Connection getConnectionFromDataSource() {
		try {
			Connection con = null;
			if (ds == null) {
				createDataSource();
			}
			con = ds.getConnection();
			return con;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static ResultSet execuateQuery(String sql){
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = getConnectionFromDataSource();
			pstmt = conn.prepareStatement(sql);
			return pstmt.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
				try {
					pstmt.close();
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return null;
		}

	public static void closeConnection(Connection conn)
	{
	    if(conn!=null)
	    {
	        try {
	            conn.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}

}
