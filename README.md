# cloud-file-manager
Cloud File Manager is Spring boot application which will interact with AWS S3 and Azure Blob Storage for file related operation, you can reuse this piece of code in your system for fast development.
Below are the list of supported operation
- Upload file to cloud like aws s3 or azure blob storage based on spring profile
- Get uploaded file list
- Download uploaded file
- Delete file by name
- Delete all uploaded files


[![Build Status](https://travis-ci.org/sandip1805/cloud-file-manager.svg?branch=main)](https://travis-ci.org/codecentric/cloud-file-manager)
[![Coverage Status](https://coveralls.io/repos/github/sandip1805/cloud-file-manager/badge.svg?branch=master)](https://coveralls.io/github/sandip1805/cloud-file-manager?branch=main)
[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

Minimal [Spring Boot](http://projects.spring.io/spring-boot/) app for managing file on cloud.

## Requirements

For building and running the application you need:

- [JDK 11](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven 3](https://maven.apache.org)

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `com.inventiverhino.cloudfilemanager.CloudFileManagerApplication` class from your IDE.

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
mvn spring-boot:run
```

## Copyright

Released under the Apache License 2.0. See the [LICENSE](https://github.com/codecentric/springboot-sample-app/blob/master/LICENSE) file.