package com.ledungcobra.course.entity;

import com.ledungcobra.common.Audit;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "course")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Course extends Audit {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "gradeid")
    private Integer gradeId;

    @Lob
    @Column(name = "title")
    private String title;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "credits")
    private Integer credits = 0;

    @Lob
    @Column(name = "schedule")
    private String schedule;

    @Lob
    @Column(name = "coursecode")
    private String courseCode;

    @OneToMany(mappedBy = "course")
    private List<Assignment> assignments;

    @ManyToMany
    @JoinTable(
            name = "coursestudent",
            joinColumns = @JoinColumn(name = "courseid"),
            inverseJoinColumns = @JoinColumn(name = "studentid"))
    private List<Student> students;


}
