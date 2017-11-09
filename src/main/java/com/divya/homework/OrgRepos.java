package com.divya.homework;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

//Read Organization Repositories and corresponding branch files
public class OrgRepos {
	
	MySqlReadWrite mapper=new MySqlReadWrite();
	int counter=0;
	List<String> messages = new ArrayList<String>();

	public List<String> readOrganizationRepos(String orgnztn, String authToken, List<String> FileExtensionsList, Connection myconn) throws SQLException 
	{
		try {
			String orgname = orgnztn;
			String page = "&per_page=100&page="; //pagination
			int j = 1, k = 1;
			
			//Read each page having list of organizations
			while (j != 0) {
				String link = "https://api.github.com/orgs/" + orgname + "/repos?" + authToken + page + k;
				k++;
				//establish connection to url
				URL url = new URL(link);
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.connect();
				InputStream is = con.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				String line;
				StringBuilder sb = new StringBuilder();
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
				JSONArray jArr = (JSONArray) JSONValue.parse(sb.toString());
				j = jArr.size(); //number of repos in that page
				
				//No Repos available in that page
				if (j == 0) 
					break;
				else 
				{
					for (int i = 0; i < jArr.size(); i++) {
						JSONObject innerObj = (JSONObject) jArr.get(i);
						String nexturl = (String) innerObj.get("url");
						String repoName = (String) innerObj.get("name");
						System.out.println("Repository Name: "+repoName);
						
						//get branches for each repository and iterate through them to get file names
						for (String s:getRepoBranches(nexturl,authToken)) 
						{
							System.out.println(" branch: "+s );
							pushfiles(nexturl + "/contents?ref=" + s + "&", authToken, FileExtensionsList, myconn, orgname);//get and push filenames into database for each branch	
						}
					}
				}	
			}
			messages.add("All files related to Organization : "+ orgname +" are added to database. Count : "+counter);
			return messages;
		} 
		catch (IOException ex) {
			ex.printStackTrace();
			messages.add("Organization Name invalid, Error in establishing API Connection");			
		}
		return messages;
	}
	
	
	//get branches list from given organization page
	public static HashSet<String> getRepoBranches(String branchUrl, String authToken) throws IOException 
	{
		HashSet<String> hs = new HashSet<String>();
		//establish connection to branch list url
		URL url = new URL(branchUrl + "/branches?" + authToken);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.connect();
		InputStream is = con.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		String line;
		StringBuilder sb = new StringBuilder();
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		JSONArray jArr = (JSONArray) JSONValue.parse(sb.toString());
		//make a list of branch names and return
		if (jArr.size() != 0) {
			for (int i = 0; i < jArr.size(); i++) {
				JSONObject innerObj = (JSONObject) jArr.get(i);
				String branchName = (String) innerObj.get("name");
				hs.add(branchName);
			}
		}
		return hs;
	}
	
	
	//get all filenames and push into database with matching file names
	public  void pushfiles(String nexturl, String authToken, List<String> FileExtensionsList, Connection myconn, String orgname) throws IOException, SQLException {
		try {
			String link = nexturl + authToken;
			URL url = new URL(link);
			//establish connection to branch contents url
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.connect();
			InputStream is = con.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			JSONArray jArr = (JSONArray) JSONValue.parse(sb.toString());
			
			if (jArr.size() != 0) 
			{
				//for each file type in branch url, execute below block
				for (int i = 0; i < jArr.size(); i++) 
				{
					JSONObject innerObj = (JSONObject) jArr.get(i);
					String nexturl1 = (String) innerObj.get("url");
					String typefile = (String) innerObj.get("type");
					
					if (typefile.equalsIgnoreCase("file")) 
					{
						String fn = (String) innerObj.get("name");
						String fileExt=FilenameUtils.getExtension(fn);
						//if type of file is given as input, load into database
						if (FileExtensionsList.contains(fileExt)) 
						{
							mapper.pushOrgFiles(orgname, fn, (String) innerObj.get("url"), fileExt, myconn);
							counter++;
						}
					}
					//If it is directory, check if it has any files with matching file extensions
					else if (typefile.equalsIgnoreCase("dir")) {
						pushfiles(nexturl1 + "&", authToken, FileExtensionsList, myconn, orgname);
					}
				}
			}
		} catch (IOException ex) {
			System.out.println("Maximum Limit reached. Cannot call more than 5000 API calls in an hour!");
		}
	}


}
