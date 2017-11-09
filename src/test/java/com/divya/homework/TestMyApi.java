package com.divya.homework;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestMyApi {
	DatabaseConnection db=new DatabaseConnection();
	Connection con ;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		
		
		con=db.databaseconnection();
	}

	@Test
	public void testRead() {	
		MySqlReadWrite mapper=new MySqlReadWrite();
		mapper.pushOrgFiles("org", "abc.rb", "Rouxbe", "rb", con);
		//test cases for post api
		
		//test cases for get api
		
		//valid org name and file extensions
		List<String> orgnames = new ArrayList<String>();
		List<String> fileext = new ArrayList<String>();
		orgnames.add("Rouxbe");
		fileext.add("js");
		mapper.getresults(orgnames,fileext,con);
		assertNotNull(mapper.getresults(orgnames,fileext,con).size()); 
		
		//empty orgnames list
		List<String> orgnames1 = new ArrayList<String>();
		List<String> fileext1 = new ArrayList<String>();
		fileext1.add("js");
		assertNotNull(mapper.getresults(orgnames1,fileext1,con).size()); 
		
		//No results with given inputs
		List<String> orgnames2 = new ArrayList<String>();
		List<String> fileext2 = new ArrayList<String>();
		orgnames2.add("abc");
		fileext2.add("xyz");
		assertTrue(mapper.getresults(orgnames2,fileext2,con).contains("No results avilable for the given inputs or give valid inputs!!"));
		
		
	}
	
	@After
	public void tearDown() throws SQLException {
		con.close();
	}

}
