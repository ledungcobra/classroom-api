package com.ledungcobra.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class NotificationsResponse implements Serializable {
    private long total;
    private boolean hasMore;
    private NotificationData data;
}
