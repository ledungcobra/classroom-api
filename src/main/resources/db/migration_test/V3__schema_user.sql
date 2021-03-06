use coursetest;

CREATE TABLE Users(
                      `Id` int AUTO_INCREMENT NOT NULL,
                      `FirstName` Longtext NULL,
                      `MiddleName` Longtext NULL,
                      `LastName` Longtext NULL,
                      `Gender` int NOT NULL,
                      `Address` Longtext NULL,
                      `NormalizedDisplayName` Longtext NULL,
                      `PersonalEmail` Longtext NULL,
                      `PersonalEmailConfirmed` Tinyint NOT NULL,
                      `NormalizedPersonalEmail` Longtext NULL,
                      `PersonalPhoneNumber` Longtext NULL,
                      `CreateBy` Longtext NULL,
                      `CreateOn` Datetime(6) NOT NULL,
                      `UpdateBy` Longtext NULL,
                      `UpdateOn` Datetime(6) NOT NULL,
                      `UserStatus` int NOT NULL,
                      `UserName` nvarchar(256) NULL,
                      `NormalizedUserName` nvarchar(256) NULL,
                      `Email` nvarchar(256) NULL,
                      `NormalizedEmail` nvarchar(256) NULL,
                      `EmailConfirmed` Tinyint NOT NULL,
                      `PasswordHash` Longtext NULL,
                      `SecurityStamp` Longtext NULL,
                      `ConcurrencyStamp` Longtext NULL,
                      `PhoneNumber` Longtext NULL,
                      `PhoneNumberConfirmed` Tinyint NOT NULL,
                      `TwoFactorEnabled` Tinyint NOT NULL,
                      `LockoutEnd` Datetime(6) NULL,
                      `LockoutEnabled` Tinyint NOT NULL,
                      `AccessFailedCount` int NOT NULL,
                      `StudentID` Longtext NULL,
                      `ProfileImageUrl` Longtext NULL,
                      `RoleAccount` int NOT NULL DEFAULT 0,
                      CONSTRAINT `PK_AspNetUsers` PRIMARY KEY
                          (
                           `Id` ASC
                              )
);