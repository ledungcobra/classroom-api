package com.ledungcobra.course.entity;

import com.ledungcobra.common.Audit;
import lombok.*;

import javax.persistence.*;

@Table(name = "coursestudent")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseStudent extends Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "courseid")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "studentid")
    private Student student;

    @Lob
    @Column(name = "studentcode")
    private String studentCode;

}
