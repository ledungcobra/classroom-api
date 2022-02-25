package com.ledungcobra.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    @GetMapping("")
    public ResponseEntity<?> getNotifications() {
        return null;
    }

    @PutMapping("/mark-seen/{id}")
    public ResponseEntity<?> putMarkNotificationSeen(@PathVariable("id") Integer id) {
        return null;
    }

    @PutMapping("/mark-seen")
    public ResponseEntity<?> putMarkNotificationsSeen() {
        return null;
    }

}
