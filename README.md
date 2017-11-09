# RESTAPIDemo
API with two end points which reads the content from repo using GitHub api and read contents from db
Demo of RESTAPI

It has 2 endpoints. First one is to get files from Github organizations and push into database and the second one is to read files from database with matching inputs.

This is done on Eclipse IDE using Jersey Framework, Tomcat Server 9.0 and MySQL database.

## Steps to execute:

*Import the code into any Java IDE

*Run the server

*Use below links to run first endpoint

It reads all the files from organizations given, crawls through the repositories and corresponding branches and loads files with given file extensions into database.

```
 list1: list of organizations(required)
 list2: FileExtensions(required)
 db: database name(required)
 Header:
 Content-Type : application/json;charset=UTF-8
 URL:
 http://localhost:8080/RESTAPIDemo/rest/homework/putintodatabase?list1=Rouxbe&list2=rb&db=mysql 
 ```

*Use below links to run second endpoint

It reads all the files from database with given parameters. 

```list1: list of organizations (optional)
list2: FileExtensions (required)
db: database name (required)
Header:
Content-Type : application/json;charset=UTF-8
URL:http://localhost:8080/RESTAPIDemo/rest/homework/getfromdatabase?list1=Rouxbe&list2=rb&db=mysql
```
