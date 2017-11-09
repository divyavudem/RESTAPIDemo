package com.divya.homework;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

//Read and write operations to database
public class MySqlReadWrite  implements MySqlMapper{

	//Read files from database
	@Override
	public ArrayList<String> getresults(List<String> orgnames, List<String> fileext, Connection myconn) {
			try {
				Statement stmt = null;
				String tableName = "gitfiles.GitFileNames";
				ResultSet rs=null;
				//if no organizations are given
				if(orgnames.size()==0)
				{
					String fexts = fileext.isEmpty() ? "" : "\"" + String.join("\", \"", fileext) + "\"";
				    String query = "select Organization,FileName, reference, FileExtension from " + tableName + " where FileExtension in ("+fexts+")" ;
				    System.out.println(query);
					stmt = myconn.createStatement();
					rs = stmt.executeQuery(query);
				}
				//if fileextensions are not given
				else if(fileext.size()==0) {
					String orgs = orgnames.isEmpty() ? "" : "\"" + String.join("\", \"", orgnames) + "\"";
				    String query ="select Organization,FileName, reference, FileExtension from " + tableName + " where Organization in ("+orgs+")" ;
				    System.out.println(query);
					stmt = myconn.createStatement();
					rs = stmt.executeQuery(query);
				}
				//both organizations and file extensions are available
				else 
				{
					String fnames = orgnames.isEmpty() ? "" : "\"" + String.join("\", \"", orgnames) + "\"";
					String fexts = fileext.isEmpty() ? "" : "\"" + String.join("\", \"", fileext) + "\"";
					String query ="select Organization,FileName, reference, FileExtension from " + tableName + " where Organization in ("+fnames+") and FileExtension in ("+fexts+")";
					System.out.println(query);
					stmt = myconn.createStatement();
					rs = stmt.executeQuery(query);
			    }
			   
			   //No results available for given inputs
			   if(!rs.next())
			   {
				   ArrayList<String> message = new ArrayList<String>();
				   System.out.println("No results avilable for the given inputs or give valid inputs");
				   message.add("No results avilable for the given inputs or give valid inputs!!");
				   return message;
			   }
			   
			   //return results obtained   
			   ArrayList<String> results1 = new ArrayList<String>();
			   while (rs.next()) 
			   {
				   String org = rs.getString("Organization");
			       String fn = rs.getString("FileName");
			       String ref = rs.getString("reference");
			       String fe = rs.getString("FileExtension");
			       results1.add(fn);
			       System.out.println(org + "\t" + fn + "\t" + ref + "\t" + fe );
			   }
			   return results1;	  
			   }
			
			    catch (Exception e ) {
			        e.printStackTrace();
			        return null;
			    }
			}
	

	//Insert into Database: execute SQL script with given parameters
	@Override
	public void pushOrgFiles(String Organization, String FileName, String reference, String FileExtension, Connection myConn) {
		
		try {
			String query = " insert into GitFileNames (Organization, FileName, reference, FileExtension)"
					+ " values (?, ?, ?, ?)";
			PreparedStatement preparedStmt = myConn.prepareStatement(query);
			preparedStmt.setString(1, Organization);
			preparedStmt.setString(2, FileName);
			preparedStmt.setString(3, reference);
			preparedStmt.setString(4, FileExtension);
			preparedStmt.execute();
			
		} catch(Exception e){
			e.printStackTrace();
		}
		
	}

}
