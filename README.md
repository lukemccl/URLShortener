# URLShortener 

A website that will take in a URL and host a shortened redirect 

## Structure
This project runs using a MySQL Database, with backend hosting using Jetty and frontend via React.

## Build & Run
This assumes the MySQL DB has already been created and is running as stated in database/readme

### Back end
Navigate to /backend

run "mvn package" to build

run "java -jar backend-1.0-SNAPSHOT-jar-with-dependencies.jar" to start server

The backend server runs on localhost:8080

### Front end
Navigate to /ui

run "npm start" to start server

The frontend server runs on localhost:3000
