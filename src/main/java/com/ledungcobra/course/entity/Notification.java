package com.ledungcobra.course.entity;

import com.ledungcobra.common.Audit;
import lombok.*;

import javax.persistence.*;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notification extends Audit {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @Lob
    @Column(name = "message")
    protected String message;

    @Column(name = "isseen")
    protected Byte isSeen;

    @Column(name = "typenotification")
    protected Integer typeNotification;

    @Column(name = "userid")
    protected Integer userId;

    @Lob
    @Column(name = "sendername")
    protected String senderName;

}
