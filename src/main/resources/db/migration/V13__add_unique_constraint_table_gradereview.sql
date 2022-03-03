use course;
set foreign_key_checks =0;

alter table GradeReview
    add constraint unique UK_GradeReview_Constraint (StudentId, GradeId);

set foreign_key_checks =1;