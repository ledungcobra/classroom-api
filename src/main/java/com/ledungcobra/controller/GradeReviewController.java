package com.ledungcobra.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/grade-review")
public class GradeReviewController {

    @GetMapping("")
    public ResponseEntity<?> getGradeReview() {
        return null;
    }

    @PostMapping("")
    public ResponseEntity<?> postCreateGradeReview() {
        return null;
    }

    @GetMapping("/comments")
    public ResponseEntity<?> getGradeReviewComments() {
        return null;
    }

    @PostMapping("/approval")
    public ResponseEntity<?> postApproveGradeReview() {
        return null;
    }

    @PutMapping("/update")
    public ResponseEntity<?> putUpdateGradeReview() {
        return null;
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteGradeReview() {
        return null;
    }

    @PostMapping("/teacher-comment")
    public ResponseEntity<?> postTeacherComment() {
        return null;
    }

    @PutMapping("/teacher-comment/update")
    public ResponseEntity<?> putTeacherUpdateComment() {
        return null;
    }

    @DeleteMapping("/teacher-comment/delete")
    public ResponseEntity<?> deleteTeacherTeacherComment() {
        return null;
    }

    @PostMapping("/student-comment")
    public ResponseEntity<?> postStudentComment() {
        return null;
    }

    @PutMapping("/student-comment/update")
    public ResponseEntity<?> putStudentUpdateComment() {
        return null;
    }

    @DeleteMapping("/student-comment/delete")
    public ResponseEntity<?> deleteStudentDeleteComment() {
        return null;
    }

}
