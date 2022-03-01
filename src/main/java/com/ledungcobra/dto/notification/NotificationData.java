package com.ledungcobra.dto.notification;

import com.ledungcobra.course.entity.Notification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationData implements Serializable {
    private long amountUnseen;
    private List<Notification> notifications;
}
