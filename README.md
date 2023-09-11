# Meeting Booker

Example Java Spring Boot project.
- Embedded H2 Database Engine (in memory)
- Client and server side validations
- WEB Graphical User Interface

![Screenshot from 2023-09-11 12-27-16](https://github.com/gery/meetingbooker/assets/1073386/175c0b79-41fd-4c0d-ae33-2d828f1bbba3)

Colors: 
  - Green: free
  - Gray: reserved by others (Just hover the mouse on the cells and you can see who reserved it) 
  - Purple: actual selected time interval

Requirements:
- Java 18
- Maven

Build and Run on localhost:
Just run ./buildAndRun.sh

Enter in your browser: http://localhost:8080/

Database admin:
http://localhost:8080/h2-console

spring.datasource.username=sa

spring.datasource.password=
