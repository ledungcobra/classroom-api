use course;
update Users
set StudentID='3'
where UserName = '677SG';
update Users
set StudentID= '18120345'
where UserName = 'panhhuu';

alter table Users
    add constraint unique UK_STUDENT_ID (StudentID(100));
