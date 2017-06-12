/**
* Setup commands to create table and databse 
* for executing the database file
*
*/
#Create Database
CREATE DATABASE adsDB;
#USe the created Database
USE adsDB;

#DROP Existing table
DROP TABLE AdPartners;
DROP TABLE AdContent;
#Create table AdPartners to store list of partners
CREATE TABLE AdPartners (
    partnerID VARCHAR(250) NOT NULL UNIQUE,
    PRIMARY KEY (partnerID)
);
#Create table AdContent Advertise campaigns
CREATE TABLE AdContent (
   adPartnerID VARCHAR(250) NOT NULL,
   adID INT NOT NULL UNIQUE AUTO_INCREMENT,
   creationTime TIMESTAMP ,
   duration INT,
   isActive BOOL,
   adContent VARCHAR(250),
   PRIMARY KEY (adID),
   FOREIGN KEY (adPartnerID) REFERENCES AdPartners(partnerID)
);
#Select Statement
SELECT * FROM AdContent;
SELECT * FROM AdPartners;

#Stored procedure call test statements
CALL SaveAdCampaign('1', TIMESTAMP('2017-06-11 01:46:56'),'Second Ad 6',90, @out_value);
CALL SaveAdCampaign('4', CURRENT_TIMESTAMP,'Fourth Ad 9',1200, @out_value, @ID);
CALL SaveAdCampaign('5', CURRENT_TIMESTAMP,'Fifth Ad 4',1200, @out_value, @ID);
CALL SaveAdCampaign('6', CURRENT_TIMESTAMP,'Sixth Ad 4',1200, @out_value, @ID);
SELECT @out_value;
SELECT @ID;
SELECT * FROM AdContent WHERE  adPartnerID = 2 and isActive = 0;

# event_scheduler to ON to activate our trigger
SET GLOBAL event_scheduler = ON;
