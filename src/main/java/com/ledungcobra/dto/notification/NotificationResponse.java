package com.ledungcobra.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponse implements Serializable {
    private Integer id;
    private String message;
    private Byte isSeen;
    private Integer typeNotification;
    private Integer userId;
    private String senderName;
    private Integer courseId;
    private Integer gradeId;
    private Integer gradeReviewId;
    private String createBy;
    private String updateBy;
    private Instant createOn;
    private Instant updateOn;
}
