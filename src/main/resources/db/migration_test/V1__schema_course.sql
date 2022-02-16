drop database if exists coursetest;
create database coursetest;

use coursetest;
CREATE TABLE `Assignments`
(
    `Id`          int AUTO_INCREMENT NOT NULL,
    `CourseId`    int                NOT NULL,
    `Name`        Longtext           NULL,
    `Description` Longtext           NULL,
    `MaxGrade`    int                NOT NULL,
    `Order`       int                NOT NULL,
    `CreateBy`    Longtext           NULL,
    `CreateOn`    Datetime           NOT NULL,
    `UpdateBy`    Longtext           NULL,
    `UpdateOn`    Datetime           NOT NULL,
    `GradeScale`  real               NOT NULL default 0,
    CONSTRAINT `PK_Assignments` PRIMARY KEY
        (
         `Id` ASC
            )
);



CREATE TABLE `Assignments_Student`
(
    `Id`           int AUTO_INCREMENT NOT NULL,
    `AssignmentId` int                NOT NULL,
    `StudentId`    int                NOT NULL,
    `UserId`       int                NOT NULL,
    `CreateBy`     Longtext           NULL,
    `CreateOn`     Datetime           NOT NULL,
    `UpdateBy`     Longtext           NULL,
    `UpdateOn`     Datetime           NOT NULL,
    CONSTRAINT `PK_Assignments_Student` PRIMARY KEY
        (
         `Id` ASC
            )
);



CREATE TABLE `Course`
(
    `Id`          int AUTO_INCREMENT NOT NULL,
    `SubjectId`   int                NOT NULL,
    `GradeId`     int                NOT NULL,
    `Title`       Longtext           NULL,
    `Description` Longtext           NULL,
    `Credits`     int                NOT NULL default 0,
    `Schedule`    Longtext           NULL,
    `CreateBy`    Longtext           NULL,
    `CreateOn`    Datetime           NOT NULL,
    `UpdateBy`    Longtext           NULL,
    `UpdateOn`    Datetime           NOT NULL,
    `CourseCode`  Longtext           NULL,
    CONSTRAINT `PK_Course` PRIMARY KEY
        (
         `Id` ASC
            )
);



CREATE TABLE `CourseStudent`
(
    `Id`          int AUTO_INCREMENT NOT NULL,
    `CourseId`    int                NOT NULL,
    `StudentId`   int                NOT NULL,
    `StudentCode` Longtext           NULL,
    `CreateBy`    Longtext           NULL,
    `CreateOn`    Datetime           NOT NULL,
    `UpdateBy`    Longtext           NULL,
    `UpdateOn`    Datetime           NOT NULL,
    CONSTRAINT `PK_CourseStudent` PRIMARY KEY
        (
         `Id` ASC
            )
);



CREATE TABLE CourseUser
(
    `Id`       int AUTO_INCREMENT NOT NULL,
    `UserId`   int                NOT NULL,
    `CourseId` int                NOT NULL,
    `Role`     int                NOT NULL,
    `CreateBy` Longtext           NULL,
    `CreateOn` Datetime           NOT NULL,
    `UpdateBy` Longtext           NULL,
    `UpdateOn` Datetime           NOT NULL,
    CONSTRAINT `PK_CourseUser` PRIMARY KEY
        (
         `Id` ASC
            )
);


CREATE TABLE Grade
(
    `Id`              int AUTO_INCREMENT NOT NULL,
    `Name`            Longtext           NULL,
    `Description`     Longtext           NULL,
    `CreateBy`        Longtext           NULL,
    `CreateOn`        Datetime           NOT NULL,
    `UpdateBy`        Longtext           NULL,
    `UpdateOn`        Datetime           NOT NULL,
    `AssignmentId`    int                NOT NULL DEFAULT 0,
    `GradeAssignment` real               NOT NULL default 0,
    `IsFinalized`     Tinyint            NOT NULL default 0,
    `MSSV`            Longtext           NULL,
    `StudentId`       int                NOT NULL DEFAULT 0,
    CONSTRAINT `PK_Grade` PRIMARY KEY
        (
         `Id` ASC
            )
);



CREATE TABLE GradeReview
(
    `Id`          int AUTO_INCREMENT NOT NULL,
    `GradeExpect` real               NOT NULL,
    `Message`     Longtext           NULL,
    `StudentId`   int                NOT NULL,
    `GradeId`     int                NOT NULL,
    `Status`      int                NOT NULL,
    `CreateBy`    Longtext           NULL,
    `CreateOn`    Datetime           NOT NULL,
    `UpdateBy`    Longtext           NULL,
    `UpdateOn`    Datetime           NOT NULL,
    CONSTRAINT `PK_GradeReview` PRIMARY KEY
        (
         `Id` ASC
            )
);



CREATE TABLE `Notification`
(
    `Id`               int AUTO_INCREMENT NOT NULL,
    `UserId`           int                NOT NULL,
    `IsSeen`           Tinyint            NOT NULL,
    `SenderName`       Longtext           NULL,
    `TypeNotification` int                NOT NULL,
    `Message`          Longtext           NULL,
    `CreateBy`         Longtext           NULL,
    `CreateOn`         Datetime           NOT NULL,
    `UpdateBy`         Longtext           NULL,
    `UpdateOn`         Datetime           NOT NULL,
    `CourseId`         int                NOT NULL DEFAULT 0,
    `GradeId`          int                NOT NULL DEFAULT 0,
    `GradeReviewId`    int                NOT NULL DEFAULT 0,
    CONSTRAINT `PK_Notification` PRIMARY KEY
        (
         `Id` ASC
            )
);



CREATE TABLE ReviewComment
(
    `Id`            int AUTO_INCREMENT NOT NULL,
    `Message`       Longtext           NULL,
    `StudentId`     int                NOT NULL,
    `TeacherId`     int                NOT NULL,
    `GradeReviewId` int                NOT NULL,
    `CreateBy`      Longtext           NULL,
    `CreateOn`      Datetime           NOT NULL,
    `UpdateBy`      Longtext           NULL,
    `UpdateOn`      Datetime           NOT NULL,
    CONSTRAINT `PK_ReviewComment` PRIMARY KEY
        (
         `Id` ASC
            )
);



CREATE TABLE Student
(
    `Id`         int AUTO_INCREMENT NOT NULL,
    `MiddleName` Longtext           NULL,
    `StudentID`  Longtext           NULL,
    `FirstName`  Longtext           NULL,
    `LastName`   Longtext           NULL,
    `DateOfBird` Datetime           NOT NULL,
    `Phone`      Longtext           NULL,
    `CreateBy`   Longtext           NULL,
    `CreateOn`   Datetime           NOT NULL,
    `UpdateBy`   Longtext           NULL,
    `UpdateOn`   Datetime           NOT NULL,
    `FullName`   Longtext           NULL,
    `UserId`     int                NOT NULL DEFAULT 0,
    CONSTRAINT `PK_Student` PRIMARY KEY
        (
         `Id` ASC
            )
);



CREATE TABLE StudentNotification
(
    `Id`               int AUTO_INCREMENT NOT NULL,
    `StudentId`        int                NOT NULL,
    `CreateBy`         Longtext           NULL,
    `CreateOn`         Datetime           NOT NULL,
    `UpdateBy`         Longtext           NULL,
    `UpdateOn`         Datetime           NOT NULL,
    `IsSeen`           Tinyint            NOT NULL,
    `SenderName`       Longtext           NULL,
    `TypeNotification` int                NOT NULL,
    `Message`          Longtext           NULL,
    `UserId`           int                NOT NULL DEFAULT 0,
    CONSTRAINT `PK_StudentNotification` PRIMARY KEY
        (
         `Id` ASC
            )
);



CREATE TABLE TeacherNotification
(
    `Id`               int AUTO_INCREMENT NOT NULL,
    `TeacherId`        int                NOT NULL,
    `CreateBy`         Longtext           NULL,
    `CreateOn`         Datetime           NOT NULL,
    `UpdateBy`         Longtext           NULL,
    `UpdateOn`         Datetime           NOT NULL,
    `IsSeen`           Tinyint            NOT NULL,
    `SenderName`       Longtext           NULL,
    `TypeNotification` int                NOT NULL,
    `Message`          Longtext           NULL,
    `UserId`           int                NOT NULL DEFAULT 0,
    CONSTRAINT `PK_TeacherNotification` PRIMARY KEY
        (
         `Id` ASC
            )
);



CREATE INDEX IX_Assignments_CourseId ON Assignments
    (
     `CourseId` ASC
        );


CREATE INDEX IX_Assignments_Student_AssignmentId ON Assignments_Student
    (
     `AssignmentId` ASC
        );


CREATE INDEX IX_Assignments_Student_StudentId ON Assignments_Student
    (
     `StudentId` ASC
        );


CREATE INDEX IX_CourseStudent_CourseId ON CourseStudent
    (
     `CourseId` ASC
        );


CREATE INDEX IX_CourseStudent_StudentId ON CourseStudent
    (
     `StudentId` ASC
        );

CREATE INDEX IX_CourseUser_CourseId ON CourseUser
    (
     `CourseId` ASC
        );


CREATE INDEX IX_Grade_AssignmentId ON Grade
    (
     `AssignmentId` ASC
        );

CREATE INDEX IX_Grade_StudentId ON Grade
    (
     `StudentId` ASC
        );


CREATE INDEX IX_GradeReview_GradeId ON GradeReview
    (
     `GradeId` ASC
        );


CREATE INDEX IX_GradeReview_StudentId ON GradeReview
    (
     `StudentId` ASC
        );


CREATE INDEX IX_ReviewComment_GradeReviewId ON ReviewComment
    (
     `GradeReviewId` ASC
        );


CREATE INDEX IX_StudentNotification_StudentId ON StudentNotification
    (
     `StudentId` ASC
        );


ALTER TABLE Assignments
    ADD CONSTRAINT `FK_Assignments_Course_CourseId` FOREIGN KEY (`CourseId`)
        REFERENCES Course (`Id`);


ALTER TABLE Assignments_Student
    ADD CONSTRAINT `FK_Assignments_Student_Assignments_AssignmentId` FOREIGN KEY (`AssignmentId`)
        REFERENCES Assignments (`Id`);


ALTER TABLE Assignments_Student
    ADD CONSTRAINT `FK_Assignments_Student_Student_StudentId` FOREIGN KEY (`StudentId`)
        REFERENCES Student (`Id`);


ALTER TABLE CourseStudent
    ADD CONSTRAINT `FK_CourseStudent_Course_CourseId` FOREIGN KEY (`CourseId`)
        REFERENCES Course (`Id`);



ALTER TABLE CourseStudent
    ADD CONSTRAINT `FK_CourseStudent_Student_StudentId` FOREIGN KEY (`StudentId`)
        REFERENCES Student (`Id`);



ALTER TABLE CourseUser
    ADD CONSTRAINT `FK_CourseUser_Course_CourseId` FOREIGN KEY (`CourseId`)
        REFERENCES Course (`Id`);


ALTER TABLE Grade
    ADD CONSTRAINT `FK_Grade_Assignments_AssignmentId` FOREIGN KEY (`AssignmentId`)
        REFERENCES Assignments (`Id`);


ALTER TABLE Grade
    ADD CONSTRAINT `FK_Grade_Student_StudentId` FOREIGN KEY (`StudentId`)
        REFERENCES Student (`Id`);


ALTER TABLE GradeReview
    ADD CONSTRAINT `FK_GradeReview_Grade_GradeId` FOREIGN KEY (`GradeId`)
        REFERENCES Grade (`Id`);

ALTER TABLE GradeReview
    ADD CONSTRAINT `FK_GradeReview_Student_StudentId` FOREIGN KEY (`StudentId`)
        REFERENCES Student (`Id`);

ALTER TABLE ReviewComment
    ADD CONSTRAINT `FK_ReviewComment_GradeReview_GradeReviewId` FOREIGN KEY (`GradeReviewId`)
        REFERENCES GradeReview (`Id`);


ALTER TABLE StudentNotification
    ADD CONSTRAINT `FK_StudentNotification_Student_StudentId` FOREIGN KEY (`StudentId`)
        REFERENCES Student (`Id`);
