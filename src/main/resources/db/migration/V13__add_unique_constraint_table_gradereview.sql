use course;
alter table gradereview
    add constraint unique UK_GradeReview_Constraint (StudentId, GradeId);