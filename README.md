# Sprout Server

![Unit Tests](https://github.com/Adel-Wehbi/capstone-server/actions/workflows/unit-tests.yml/badge.svg)

This is the server (backend) repository for the Local Produce Capstone Project.

Team members are as follows: 
Eleanor Rumsey 8274066
Adel Wehbi 300032086
Abbey Kerkmann 300007054
Mikeli Habash 300006570

## Setting Up the Server

### 1. Creating the Database Instance:
Run PostgreSQL pgAdmin 4 and create a new database called capstonedb.\
\
Create your own login role with a username and password to own the database:\
Username: capstonedb-user\
Password: capstonedb-password

### 2. Cloning the Repo
Clone this repository onto your local machine.\
Be sure to create and add the following to the config/application.yml file:
```
spring:
  jpa:
    hibernate:
      ddl-auto: validate
    database-platform: org.hibernate.dialect.PostgreSQLDialect
  datasource:
    url: "jdbc:postgresql://localhost:5432/capstonedb"
    username: capstonedb-user
    password: capstonedb-password
```

### 3. Updating Database with Liquibase
Once the repo is cloned and the application.yml file is created, gradle build the application and then run a liquibase update command to populate the database with the necesary tables.\
To do this, open a terminal in the repo directory and run `gradlew update`.

### 4. Running the Server
Once all of the above actions are completed, you may run the server application using gradle.
