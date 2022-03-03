use coursetest;

create table `ClassRole`
(
    Id   int primary key,
    Name varchar(255)
);

insert into `ClassRole`(Id, Name)
values (1, 'TEACHER')
        ,
       (2, 'STUDENT');


create table `Role`
(
    Id   int primary key,
    Name varchar(255)
);

insert into `Role`(Id, Name)
values (1, 'ADMIN')
        ,
       (0, 'USER');