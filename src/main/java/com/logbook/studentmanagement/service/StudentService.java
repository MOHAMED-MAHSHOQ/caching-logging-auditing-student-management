package com.logbook.studentmanagement.service;

import com.logbook.studentmanagement.dto.CreateStudentRequest;
import com.logbook.studentmanagement.dto.StudentResponse;
import com.logbook.studentmanagement.dto.UpdateStudentRequest;
import com.logbook.studentmanagement.entity.StudentStatus;

import java.util.List;

public interface StudentService {
    StudentResponse createStudent(CreateStudentRequest request);
    StudentResponse getStudentById(Long id);
    List<StudentResponse> getAllStudents();
    StudentResponse updateStudent(Long id, UpdateStudentRequest request);
    void deleteStudent(Long id);
    List<StudentResponse> searchStudents(String name);
    List<StudentResponse> getStudentsByStatus(StudentStatus status);



}
