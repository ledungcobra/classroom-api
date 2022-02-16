use course;
alter table users add constraint unique (Email,PersonalEmail);