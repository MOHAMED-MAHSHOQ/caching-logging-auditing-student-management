package com.logbook.studentmanagement.exception;
public class StudentNotFoundException extends RuntimeException {

    private final Long studentId;

    public StudentNotFoundException(Long id) {
        super("Student not found with id: " + id);
        this.studentId = id;
    }

    public Long getStudentId() {
        return studentId;
    }
}