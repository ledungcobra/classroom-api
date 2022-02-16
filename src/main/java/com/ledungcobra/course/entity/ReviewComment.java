package com.ledungcobra.course.entity;

import com.ledungcobra.common.Audit;
import com.ledungcobra.user.entity.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "reviewcomment")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewComment extends Audit {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Lob
    @Column(name = "message")
    private String message;

    @ManyToOne
    @JoinColumn(name = "studentid")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "teacherid")
    private User teacher;

    @ManyToOne
    @JoinColumn(name = "gradereviewid")
    private GradeReview gradeReview;

}
