# Gravitee Management REST API

Administrative Rest API to manage the Gateway

[![Build Status](http://build.gravitee.io/jenkins/buildStatus/icon?job=gravitee-management-rest-api)](http://build.gravitee.io/jenkins/view/Tous/job/gravitee-management-rest-api/)

## Building

```
$ git clone https://github.com/gravitee-io/gravitee-management-rest-api.git
$ cd gravitee-management-rest-api/war
$ mvn -Djetty.port=8082 jetty:run
...
<app starts and listens on port 8082>
```
The application can be package and deploy as web application (war file)
```
$ git clone https://github.com/gravitee-io/gravitee-management-rest-api.git
$ cd gravitee-management-rest-api/war
$ mvn package
...
<app war file can be find inside target/ folder>
<deploy it inside web container to get ready>
```
and also as a executable jar (embedded web application)
```
$ git clone https://github.com/gravitee-io/gravitee-management-rest-api.git
$ cd gravitee-management-rest-api/standalone
$ mvn package
...
<app jar file can be find inside target/ folder>
<you can start the application with the following command line>
$ java -jar target/<app_name>-executable.jar
...
<app starts and listens on port 8082>
```

