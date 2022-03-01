package com.ledungcobra.controller;

import com.ledungcobra.common.*;
import com.ledungcobra.course.entity.Notification;
import com.ledungcobra.course.service.NotificationService;
import com.ledungcobra.dto.notification.NotificationData;
import com.ledungcobra.dto.notification.NotificationsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/notification")
@CrossOrigin(originPatterns = "*")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("")
    public ResponseEntity<?> getNotifications(@RequestParam("StartAt") Integer startAt, @RequestParam("MaxResults") Integer maxResults, @RequestParam("SortColumn") String sortColumn, @RequestParam("CurrentUser") String currentUser) {
        var user = AuthenticationUtils.appUserDetails().unwrap();
        if (user == null) {
            return ok(CommonResponse.builder().status(EStatus.Error).result(EResult.Error).content("").message("Not found user").build());
        }
        startAt = startAt == null ? 0 : startAt;
        maxResults = maxResults == null ? Integer.MAX_VALUE : maxResults;
        Pageable pageable = PageableBuilder.getPageable(startAt, maxResults, sortColumn);
        var numberOfNotifications = notificationService.countByUserId(user.getId());
        var notifications = notificationService.findAllByUserId(user.getId(), pageable);
        return ok(CommonResponse.builder()
                .status(EStatus.Success)
                .result(EResult.Successful)
                .message("Get notifications successfully")
                .content(NotificationsResponse.builder()
                        .hasMore(startAt + notifications.size() < numberOfNotifications)
                        .total(numberOfNotifications)
                        .data(NotificationData.builder()
                                .amountUnseen(notifications.stream().filter(n -> n.getIsSeen() == 0).count())
                                .notifications(notifications.stream()
                                        .toList())
                                .build())
                        .build())
                .build());
    }

    @PutMapping("/mark-seen/{id}")
    public ResponseEntity<?> putMarkNotificationSeen(@PathVariable("id") Integer id) {
        var user = AuthenticationUtils.appUserDetails().unwrap();
        if (user == null) {
            return ok(CommonResponse.builder().status(EStatus.Error).result(EResult.Error).content("").message("Not found user").build());
        }

        var notification = notificationService.findByUserIdAndId(user.getId(), id);
        if (notification == null) {
            return ok(CommonResponse.builder()
                    .content("")
                    .message("Not found notification by id")
                    .result(EResult.Error)
                    .status(EStatus.Error)
                    .build());
        }

        notification.setIsSeen(true);
        notificationService.save(notification, user.getUserName());
        return ok(CommonResponse.builder()
                .status(EStatus.Success)
                .result(EResult.Successful)
                .content("")
                .message("Mark seen notification successfully")
                .build());
    }

    @PutMapping("/mark-seen")
    public ResponseEntity<?> putMarkNotificationsSeen() {
        var user = AuthenticationUtils.appUserDetails().unwrap();
        if (user == null) {
            return ok(CommonResponse.builder()
                    .content("")
                    .message("Not found notification by id")
                    .result(EResult.Error).status(EStatus.Error)
                    .build());
        }
        var notifications = notificationService.findAllByUserId(user.getId());
        for (Notification notification : notifications) {
            notification.setIsSeen(true);
        }
        notificationService.updateBatch(notifications);
        return ok(CommonResponse.builder()
                .status(EStatus.Success)
                .result(EResult.Successful)
                .content("")
                .message("Mark all seen notifications successfully")
                .build());
    }
}
