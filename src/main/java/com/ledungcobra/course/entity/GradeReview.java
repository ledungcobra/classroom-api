package com.ledungcobra.course.entity;


import com.ledungcobra.common.Audit;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "gradereview")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GradeReview extends Audit {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "gradeexpect")
    private Float gradeExpect;

    @Lob
    @Column(name = "message")
    private String message;

    @OneToOne
    @JoinColumn(name = "gradeid")
    private Grade grade;

    @ManyToOne
    @JoinColumn(name = "studentid")
    private Student student;

    @Column(name = "status")
    private Integer status;


}
