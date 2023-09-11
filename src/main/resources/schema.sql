create table MEETING(
  ID int not null AUTO_INCREMENT,
  NAME varchar(100) not null,
  TIME_FROM timestamp not null,
  TIME_TO timestamp not null,    
  PRIMARY KEY ( ID )
);