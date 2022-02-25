use course;
update users
set StudentID='3'
where UserName = '677SG';
update users
set StudentID= '18120345'
where UserName = 'panhhuu';

alter table users
    add constraint unique UK_STUDENT_ID (StudentID);
