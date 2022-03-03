use course;

alter table Users
    add column `role_id` int ;

alter table Users
    add constraint foreign key `FK_USER_ROLE`
        (`role_id`) references `Role`(`Id`);

update Users u set u.role_id = 0 where 1 = 1;
