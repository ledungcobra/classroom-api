use coursetest;
create table `Classrole`
(
    `id`   int primary key,
    `name` varchar(255)
);

insert into `Classrole`(`id`, `name`)
values (1, 'TEACHER')
        ,
       (2, 'STUDENT');


create table `Role`
(
    `id`   int primary key,
    `name` varchar(255)
);

insert into `Role`(`id`, `name`)
values (1, 'ADMIN')
        ,
       (0, 'USER');