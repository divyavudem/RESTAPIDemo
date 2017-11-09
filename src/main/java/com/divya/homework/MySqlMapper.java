package com.divya.homework;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

//Mapper for database operations
public interface MySqlMapper {
	//push files into database
	public void pushOrgFiles(String Organization,String FileName, String reference, String FileExtension, Connection myConn);
	//read files from database
	public ArrayList<String> getresults(List<String> orgnames, List<String> fileExt, Connection myConn);
	

}
