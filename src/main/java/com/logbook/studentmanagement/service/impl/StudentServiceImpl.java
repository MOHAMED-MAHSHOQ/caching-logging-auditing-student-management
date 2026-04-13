package com.logbook.studentmanagement.service.impl;

import com.logbook.studentmanagement.dto.CreateStudentRequest;
import com.logbook.studentmanagement.dto.StudentResponse;
import com.logbook.studentmanagement.dto.UpdateStudentRequest;
import com.logbook.studentmanagement.entity.Student;
import com.logbook.studentmanagement.entity.StudentStatus;
import com.logbook.studentmanagement.event.StudentEvent;
import com.logbook.studentmanagement.exception.DuplicateEmailException;
import com.logbook.studentmanagement.exception.StudentNotFoundException;
import com.logbook.studentmanagement.mapper.StudentMapper;
import com.logbook.studentmanagement.repository.StudentRepository;
import com.logbook.studentmanagement.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final ApplicationEventPublisher eventPublisher;
    @Override
    public StudentResponse createStudent(CreateStudentRequest request) {
        if (studentRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException(request.getEmail());
        }
        Student student = studentMapper.toEntity(request);
        Student savedStudent = studentRepository.save(student);
        eventPublisher.publishEvent(
                new StudentEvent(this, savedStudent, StudentEvent.EventType.CREATED, "system")
        );
        return studentMapper.toResponse(savedStudent);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    @Cacheable(value = "students", key = "#id")
    public StudentResponse getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));

        return studentMapper.toResponse(student);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    @Cacheable(value = "students-list")
    public List<StudentResponse> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        return studentMapper.toResponseList(students);
    }
    @Override
    @Caching(
            put    = { @CachePut(value = "students", key = "#id") },
            evict  = { @CacheEvict(value = "students-list", allEntries = true) }
    )
    public StudentResponse updateStudent(Long id, UpdateStudentRequest request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        StudentStatus oldStatus = student.getStatus();
        studentMapper.updateFromRequest(request, student);
        Student savedStudent = studentRepository.save(student);
        StudentEvent.EventType eventType = (request.getStatus() != null && request.getStatus() != oldStatus)
                ? StudentEvent.EventType.STATUS_CHANGED
                : StudentEvent.EventType.UPDATED;
        eventPublisher.publishEvent(
                new StudentEvent(this, savedStudent, eventType, "system")
        );
        return studentMapper.toResponse(savedStudent);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "students", key = "#id"),
            @CacheEvict(value = "students-list", allEntries = true)
    })
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        studentRepository.delete(student);
        eventPublisher.publishEvent(
                new StudentEvent(this, student, StudentEvent.EventType.DELETED, "system")
        );
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<StudentResponse> searchStudents(String name) {
        List<Student> students = studentRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name);
        return studentMapper.toResponseList(students);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponse> getStudentsByStatus(StudentStatus status) {
        return studentMapper.toResponseList(studentRepository.findByStatus(status));
    }
}
