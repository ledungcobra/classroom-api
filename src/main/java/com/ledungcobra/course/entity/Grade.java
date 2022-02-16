package com.ledungcobra.course.entity;

import com.ledungcobra.common.Audit;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "grade")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Grade extends Audit {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Lob
    @Column(name = "name")
    private String name;

    @Lob
    @Column(name = "description")
    private String description;


    @Column(name = "gradeassignment")
    private Float gradeAssignment;

    @Column(name = "isfinalized")
    private Byte isFinalized;

    @Lob
    @Column(name = "mssv")
    private String mssv;

    @ManyToOne
    @JoinColumn(name = "assignmentid")
    private Assignment assigment;

    @ManyToOne
    @JoinColumn(name = "studentid")
    private Student student;

    @OneToMany
    @JoinColumn(name = "gradeid")
    private List<GradeReview> gradeReviews;

}
