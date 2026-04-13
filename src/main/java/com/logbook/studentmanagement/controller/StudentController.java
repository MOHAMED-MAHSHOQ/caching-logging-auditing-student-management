package com.logbook.studentmanagement.controller;

import com.logbook.studentmanagement.dto.ApiResponse;
import com.logbook.studentmanagement.dto.CreateStudentRequest;
import com.logbook.studentmanagement.dto.StudentResponse;
import com.logbook.studentmanagement.dto.UpdateStudentRequest;
import com.logbook.studentmanagement.entity.Student;
import com.logbook.studentmanagement.entity.StudentStatus;
import com.logbook.studentmanagement.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;
    @PostMapping
    public ResponseEntity<ApiResponse<StudentResponse>> createStudent(
            @Valid @RequestBody CreateStudentRequest request) {

        StudentResponse response = studentService.createStudent(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Student created successfully", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponse>> getStudentById(
            @PathVariable Long id) {

        StudentResponse response = studentService.getStudentById(id);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<StudentResponse>>> getAllStudents(
            @RequestParam(required = false) String search) {

        List<StudentResponse> response;

        if (search != null && !search.isBlank()) {
            response = studentService.searchStudents(search);
        } else {
            response = studentService.getAllStudents();
        }

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<StudentResponse>>> getStudentsByStatus(
            @PathVariable StudentStatus status) {

        List<StudentResponse> response = studentService.getStudentsByStatus(status);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponse>> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStudentRequest request) {

        StudentResponse response = studentService.updateStudent(id, request);

        return ResponseEntity.ok(ApiResponse.success("Student updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);

        return ResponseEntity.noContent().build();
    }

//    @GetMapping("/{id}/history")
//    public ResponseEntity<ApiResponse<Object>> getStudentHistory(@PathVariable Long id) {
//
//        Revisions<Long, Student> revisions =
//                studentService.getStudentHistory(id);
//
//        return ResponseEntity.ok(ApiResponse.success("Audit history retrieved", revisions));
//    }
}
