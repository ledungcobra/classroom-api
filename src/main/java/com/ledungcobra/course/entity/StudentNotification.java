package com.ledungcobra.course.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "studentnotification")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentNotification extends Notification {

    @ManyToOne
    @JoinColumn(name = "studentid")
    private Student student;

}
