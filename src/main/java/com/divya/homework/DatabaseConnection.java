package com.divya.homework;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//Connect to MySQL database
public class DatabaseConnection {
	public Connection databaseconnection() 
	{
		try {
		Class.forName("com.mysql.jdbc.Driver");
		//database: gitfiles 
		String url = "jdbc:mysql://localhost:3306/gitfiles";
		Connection myconn = DriverManager.getConnection(url, "root", "Chandra@4");
		return myconn;
		}
		catch (Exception e) {
		    System.out.println("Exception Raised while connecting to Database");
		    e.printStackTrace();
		    return null;
		}
	}
	
	public void closeConnection(Connection con) throws SQLException {
		con.close();
	}
}
