use coursetest;

alter table users
    add column `role_id` int ;

alter table users
    add constraint foreign key `FK_USER_ROLE`
        (`role_id`) references `role`(`Id`);

update users u set u.role_id = 0 where 1 = 1;
