package com.ledungcobra.course.entity;


import com.ledungcobra.common.Audit;
import com.ledungcobra.user.entity.ClassRole;
import com.ledungcobra.user.entity.User;
import lombok.*;

import javax.persistence.*;

@Table(name = "courseuser")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseUser extends Audit {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "userid")
    private User user;

    @ManyToOne
    @JoinColumn(name = "courseid")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "role")
    private ClassRole role;

}
