-- IGNORE THIS FILE FOR NOW. DATABASE IS BUILT IN USING H2
-- use the create database command only once
create database seniorfever;
-- use these after you created the database
CREATE TABLE seniorfever.Locations (
       LocationID INT PRIMARY KEY AUTO_INCREMENT,
       LocationName VARCHAR(255) NOT NULL
);

CREATE TABLE seniorfever.Rooms (
       RoomID INT PRIMARY KEY AUTO_INCREMENT,
       LocationID INT NOT NULL,
       RoomName VARCHAR(255) NOT NULL,
       CONSTRAINT FK_LocationRoom FOREIGN KEY (LocationID) REFERENCES Locations(LocationID) ON DELETE CASCADE
);

CREATE TABLE seniorfever.Machines (
      MachineID INT PRIMARY KEY AUTO_INCREMENT,
      RoomID INT NOT NULL,
      MachineName VARCHAR(255) NOT NULL,
      CONSTRAINT FK_RoomMachine FOREIGN KEY (RoomID) REFERENCES Rooms(RoomID) ON DELETE CASCADE
);

CREATE TABLE seniorfever.UserInfo (
      UserID BINARY(16) DEFAULT (UUID_TO_BIN(UUID())) NOT NULL PRIMARY KEY,
      Username VARCHAR(255) NOT NULL,
      VCID VARCHAR(255),
      Password VARCHAR(255) NOT NULL,
      Email VARCHAR(255) NOT NULL,
      IsAdmin BOOL DEFAULT 0
);

CREATE TABLE seniorfever.DataEntries (
     DataEntriesID BINARY(16) DEFAULT (UUID_TO_BIN(UUID())) NOT NULL PRIMARY KEY,
     UserID BINARY(16) NOT NULL,
     MachineID INT NOT NULL,
     MachineTempF INT NOT NULL,
     RoomTempF INT NOT NULL,
     Date DATE NOT NULL,
     Time TIME NOT NULL,
     CONSTRAINT FK_UserEntry FOREIGN KEY (UserID) REFERENCES UserInfo(UserID) ON DELETE CASCADE,
     CONSTRAINT FK_MachineEntry FOREIGN KEY (MachineID) REFERENCES Machines(MachineID) ON DELETE CASCADE
);