package com.ledungcobra.course.entity;

import com.ledungcobra.common.Audit;
import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Table(name = "student")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student extends Audit {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Lob
    @Column(name = "middlename")
    private String middleName;

    @Lob
    @Column(name = "studentid")
    private String studentId;

    @Lob
    @Column(name = "firstname")
    private String firstName;

    @Lob
    @Column(name = "lastname")
    private String lastName;

    @Column(name = "dateofbird")
    private Instant dateOfBirth;

    @Lob
    @Column(name = "Phone")
    private String phone;

    @Lob
    @Column(name = "fullname")
    private String fullName;

    @Column(name = "userid")
    private Integer userId;

    @ManyToMany(mappedBy = "students")
    private List<Course> courses;

    @ManyToMany
    @JoinTable(name = "assignments_student",
            joinColumns = @JoinColumn(name = "studentid"), inverseJoinColumns = @JoinColumn(name = "assignmentid"))
    private List<Assignment> assignments;

    @OneToMany(mappedBy = "student")
    private List<StudentNotification> notifications;

}
