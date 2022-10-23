package com.klalit.utils;

import java.sql.*;

public class JdbcUtils {

	static {

		try {

			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() throws SQLException {
		Connection connection = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/klalitdb?autoReconnect=true&useSSL=false&serverTimezone=UTC", "root",
				"9980912351");
		return connection;
	}

	public static void closeResources(Connection connection, PreparedStatement preparedStatement) {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

//		
//		public static Connection getConnection() throws SQLException {
//			Connection connection = DriverManager.getConnection("jdbc:sqlite:klalit.db");
//			System.out.println("Opened database successfully");
//			return connection;
//		}
//		
//		public static void createCustomersTable(Connection c) throws SQLException{
//			Statement stmt = c.createStatement();
//			String sql = "CREATE TABLE if not exists CUSTOMERS " +
//                    "(ID INT PRIMARY KEY     NOT NULL," +
//                    " FIRST_NAME           TEXT    NOT NULL, " + 
//                    " LAST_NAME           TEXT    NOT NULL, "+
//                    " DATE_OF_BIRTH            DATE     NOT NULL, " + 
//                    " PHONE                 TEXT ," + 
//                    " MOBILE_PHONE         TEXT    NOT NULL)"; 
//     stmt.executeUpdate(sql);
////     stmt.close();
////     c.close();
//     System.out.println("customers table was created successfully");
//     
//		}
//		
//		public static void insertCustomer (Connection c) throws SQLException{
//			Statement stmt = c.createStatement();
//			String sql = "INSERT INTO CUSTOMERs (ID,FIRST_NAME,LAST_NAME,DATE_OF_BIRTH,PHONE,MOBILE_PHONE) " +
//                    "VALUES (1, 'David','Cohen', 1993-01-01, '036971111', '0541234567' );"; 
//     stmt.executeUpdate(sql);
//     	
//		}
//	
//		public static void getCustomer (Connection c) throws SQLException{
//			Statement stmt = c.createStatement();
//			ResultSet rs = stmt.executeQuery( "SELECT * FROM Customers WHERE Id=1" );
//			if (!rs.next()) {
//				System.out.println("result set is empty");
//			} else {
//				System.out.println(rs.getString("last_name"));
//			}
//		}
//	
//  public static void main( String args[] ) {
//      try {
//    	  Connection c = getConnection();
//    	  Statement stmt = c.createStatement();
////			String sql = "DROP TABLE CUSTOMERS "; 
////          stmt.executeUpdate(sql);
//    	  createCustomersTable(c);
//    	  insertCustomer(c);
//    	  getCustomer(c);
//		
//	} catch (SQLException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//   }
//}