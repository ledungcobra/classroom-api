package com.ledungcobra.course.entity;

import com.ledungcobra.common.Audit;
import lombok.*;

import javax.persistence.*;

@Table(name = "assignments_student")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class AssignmentStudent extends Audit {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "assignmentid")
    private Assignment assignment;

    @ManyToOne
    @JoinColumn(name = "studentid")
    private Student student;

    @Column(name = "userid")
    private Integer userId;


}
