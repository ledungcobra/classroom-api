package com.ledungcobra.controller;

import com.ledungcobra.common.CommonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @PostMapping("/login")
    public ResponseEntity<?> login() {
        // :TODO LOGIN ADMIN
        return ok(CommonResponse.builder()
                .content("")
                .build());
    }

    @PostMapping("/register")
    public ResponseEntity<?> register() {
        return null;
    }

    @GetMapping("/admin-account")
    public ResponseEntity<?> getAdminAccounts() {
        return null;
    }

    @GetMapping("/admin-account/{id}")
    public ResponseEntity<?> getAdminAccountById(@PathVariable("id") Integer id) {
        return null;
    }

    @PostMapping("/admin-account/create-account")
    public ResponseEntity<?> createAccount() {
        return null;
    }

    @GetMapping("/user-account")
    public ResponseEntity<?> getUserAccount() {
        return null;
    }

    @PostMapping("/user-account/change-student-id")
    public ResponseEntity<?> postChangeStudentId() {
        return null;
    }

    @PostMapping("/user-account/lock-account")
    public ResponseEntity<?> postLockAccount() {
        return null;
    }

    @PostMapping("/user-account/unlock-account")
    public ResponseEntity<?> postUnlockAccount() {
        return null;
    }

    @GetMapping("/course")
    public ResponseEntity<?> getCourses() {
        return null;
    }

    @GetMapping("/course/{id}")
    public ResponseEntity<?> getCourseById(@PathVariable("id") Integer courseId) {
        return null;
    }

    public ResponseEntity<?> getAssignments() {
        return null;
    }

}
