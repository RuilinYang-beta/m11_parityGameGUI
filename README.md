# About

A parity game is played on a colored directed graph, where each node has been colored by a priority – one of (usually) finitely many natural numbers. Two players, 0 and 1, move a (single, shared) token along the edges of the graph. The owner of the node that the token falls on selects the successor node, resulting in a (possibly infinite) path, called the play.

It has applications in software verification (model checking), and currently the research question "Is there algorithm(s) that can solve Parity Game in Polynomial time" is at the research frontier. 

For researchers, the layouts of a game is represented in a specific text format. However, having to work with text files for numerous day-to-day mini adjustment of a graph is tedious, and error-prone. Besides, with no visualization, it is not intuitive for a researcher/student how an algorithm runs. 

This app aims to provide the visualization to aid research/study. It is a web app developed with Jarkata EE (Java) in the backend, and HTML/CSS/JavaScript in the frontend. Specifically, we use [Cytoscape.js](https://js.cytoscape.org/) for the complex visualization of a graph of Parity Game. 

The [poster](https://ruilinyang-beta.github.io/pdf/m11_poster.pdf) and [presentation slides](https://ruilinyang-beta.github.io/pdf/m11_slides.pdf) is helpful to have a quick overview of this project.

# Instructions
Step 1. Install Tomcat and Maven:
  1. download Tomcat to a local directory.
    b. For Mac: apache-tomcat-9.0.21.zip
    c. For Windows-x64, apache-tomcat-9.0.21-windows-x64.zip
    d. For Windows-x86, apache-tomcat-9.0.21-windows-x86.zip
  2. install Maven. (if you already have Maven installed, skip this step)

Step 2. Create a user in Apache Tomcat:
  1. navigate to the root directory of Tomcat
  2. open the file /conf/tomcat-users.xml
  3. add the following code snippet to the file, inside the <tomcat-users> tag:
    <role rolename="manager"/>
    <role rolename="admin"/>
    <role rolename="manager-gui"/>
    <role rolename="manager-script"/>
    <user username="admin" password="admin" roles="admin,manager,manager-gui,manager-script"/>

Step 3. Create a user in Apache Maven:
  1. navigate to the root directory of Maven
  2. open the file /libexec/conf/settings.xml
  3. add the following code snippet to the file, inside the <servers> tag:
    <server>
       <id>TomcatServer</id>
       <username>admin</username>
       <password>admin</password>
    </server>


Step 4. Start Tomcat:
  1. open the terminal and navigate to the root directory of Tomcat
    a. For Mac, run chmod +x catalina.sh and chmod +x startup.sh to make the file catalina.sh and startup.sh executable, run bin/startup.sh to start Tomcat
    b. For Windows, we execute the following to start the Apache Tomcat: run bin\startup.bat


Step 5. Deploy the application:
1. open the terminal and navigate to the root directory of the application, run mvn tomcat:deploy 
2. check on http://localhost:8080/manager/html/ if the deployment was successful
3. go to http://localhost:8080/pandavis to use the application

(Note: Everytime a change is made to the backend classes, to ensure the application will be updated, remember to clear the cache in the browser and the application needs to be re-deployed: open the terminal at the root directory of the application and run 
mvn tomcat:redeploy)
