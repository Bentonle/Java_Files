package jac444.wk6;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
//import java.util.logging.Level;
//import java.util.logging.Logger;

/**
 * This class is used to set up a database connection.
 * @author Benton
 *
 */
public class DbConnection {

	private String url_ = "";
	private String user_ = "";
	private String pass_ = "";
	
	/**
	 * 3 Parameter constructor used to contain database connection data.
	 * @param url - server url connection.
	 * @param user - Server user name.
	 * @param pass - Server password.
	 */
	public DbConnection(String url, String user, String pass) {
		url_ = "jdbc:mysql://" + url; user_ = user; pass_ = pass;
	}
	
	/**
	 * This function connects to database using information passed.
	 * @return con - Connected database.
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException {
		
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			
			Connection con = DriverManager.getConnection(url_, user_, pass_);
			return con;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
