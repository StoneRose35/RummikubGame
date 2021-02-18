# RummikubGame

## What is it?
A Web-Application for playing Rummikub against human or computer opponents. Does not require registration or login of any sorts, but need cookies (as we all do... at least from time to time).

## Main Building Blocks
* Frontend: Webapp built using Angular using Rest and STOMP over WebSocket calls for Backend communication.
* Backend: Spring-boot based Webserver communicating over Rest and STOMP over WebSocket. ASP-Sovler "clingo" is integrated through system calls.

## Installation

* Install Java Version >= 10.0
* Download "clingo" from https://github.com/potassco/clingo/releases/ and add the main binaries to the respective PATH environment variables. 
* Build the project using "maven install".
* run the project: "java -jar -Dserver.port=8080 RummikubGame-<VERSION>.jar" on Linux or "java.exe -jar -Dserver.port=8080 RummikubGame-VERSION.jar" on Windows
