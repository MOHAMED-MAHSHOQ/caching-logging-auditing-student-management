package com.logbook.studentmanagement.mapper;

import com.logbook.studentmanagement.dto.CreateStudentRequest;
import com.logbook.studentmanagement.dto.StudentResponse;
import com.logbook.studentmanagement.dto.UpdateStudentRequest;
import com.logbook.studentmanagement.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;
@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy =
                NullValuePropertyMappingStrategy.IGNORE
)
public interface StudentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "status", ignore = true)
    Student toEntity(CreateStudentRequest request);

    StudentResponse toResponse(Student student);

    List<StudentResponse> toResponseList(List<Student> students);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateFromRequest(UpdateStudentRequest request, @MappingTarget Student student);
}
