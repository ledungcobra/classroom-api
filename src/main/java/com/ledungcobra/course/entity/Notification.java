package com.ledungcobra.course.entity;

import com.ledungcobra.common.Audit;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Notification")
@Builder
public class Notification extends Audit {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Lob
    @Column(name = "message")
    private String message;

    @Column(name = "isseen")
    private Byte isSeen;

    @Column(name = "typenotification")
    private Integer typeNotification;

    @Column(name = "userid")
    private Integer userId;

    @Lob
    @Column(name = "sendername")
    private String senderName;

    @Column(name = "courseid")
    private Integer courseId;

    @Column(name = "gradeid")
    private Integer gradeId;

    @Column(name = "gradereviewid")
    private Integer gradeReviewId;

    public void setIsSeen(boolean isSeen) {
        this.isSeen = (byte) (isSeen ? 1 : 0);
    }
}
