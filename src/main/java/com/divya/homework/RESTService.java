package com.divya.homework;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import com.sun.istack.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.SQLException;

//Service to call API's
@Path("/homework")
public class RESTService {

	@POST
	@Path("/putintodatabase")
	@Produces(MediaType.APPLICATION_JSON)
	public List<String> firstapi(@NotNull @QueryParam("list1") List<String> list1,
			@NotNull @QueryParam("list2") List<String> list2,
			@NotNull @QueryParam("db") String dbtype) throws SQLException  
	{
		
		List<String> orglist = list1;
		List<String> FileExtensionsList = list2;
		List<String> msg = new ArrayList<String>(); //output messages
		
		//check if parameters are correct
		if(dbtype == null || list1 == null || list2 == null) {
			msg.add("Enter valid parameters! List1 represents list of organizations, list2 represents list of file extensions and db represents database");
			return msg;}
		if(list1.size()==0 || list2.size()==0 ||(!dbtype.equalsIgnoreCase("mysql"))) {
			msg.add("Enter valid parameters! List1 represents list of organizations, list2 represents list of file extensions and db represents database");
			return msg;}
		
		//Connect to Github API through AuthToken  to increase rate limit per hour
		String authToken="client_id=1ef302eadfa88512902c&client_secret=18c1c602d6cb39cce6a965bab72b250d4118a23e";
		
		//Connect to database
		DatabaseConnection db = new DatabaseConnection();
		Connection myconn ;
		
		//Load files into database
		if(db.databaseconnection()!=null) 
		{
			myconn = db.databaseconnection();
			System.out.println("MYSQL Connection Established!");
			OrgRepos gbf = new OrgRepos();  //get Org Repos
			for(String orgname: orglist) {
				System.out.println("Dealing with Organization: "+orgname);
				msg = gbf.readOrganizationRepos(orgname, authToken,FileExtensionsList,myconn); //Read files from all Repos and load to database
				System.out.println(msg);
			}
			myconn.close();
			return msg;
		}
		else 
		{
			msg.add("Could not connect to Database");
			return msg;
		}		
			
	}

	
	@GET
	@Path("/getfromdatabase")
	@Produces(MediaType.APPLICATION_JSON)
	public List<String> secondapi(
			@QueryParam("list1") List<String> list1,
			@QueryParam("list2") List<String> list2,
			@QueryParam("db") String dbtype) {
		
		List<String> orgnames = list1;
		List<String> fileext = list2;
		
		//Connect to database
		DatabaseConnection db = new DatabaseConnection();
		Connection myconn ;

		//check if parameters are correct
		if(list2==null ||dbtype==null ||(!dbtype.equalsIgnoreCase("mysql")))
		{
			List<String> message = new ArrayList<String>();
			message.add("Enter valid parameters! List1 represents list of organizations, list2 represents list of file extensions and db represents database");
			return message; }
		if(fileext.size()==0)
		{
			List<String> message = new ArrayList<String>();
			message.add("Enter valid file extensions list!!");
			return message; }
		
		//Read files from database
		if(db.databaseconnection()!=null) 
		{
			myconn = db.databaseconnection();
			System.out.println("MYSQL Connection Established!");
			MySqlReadWrite mapper=new MySqlReadWrite();
			ArrayList<String> listoffiles = mapper.getresults(orgnames,fileext,myconn);//Read files from database with given parameters
			return listoffiles;			
		}
		else {
			List<String> message = new ArrayList<String>();
			message.add("Could not connect to Database!!");
			return message;
			}

	}
	
}