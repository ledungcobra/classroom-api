package com.ledungcobra.course.entity;

import com.ledungcobra.user.entity.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "teachernotification")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeacherNotification extends Notification {

    @ManyToOne
    @JoinColumn(name = "teacherid")
    private User teacher;

}
