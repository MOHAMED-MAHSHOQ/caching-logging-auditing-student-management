package com.logbook.studentmanagement.event;

import com.logbook.studentmanagement.entity.Student;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
@Getter
public class StudentEvent extends ApplicationEvent {
    public enum EventType {
        CREATED,
        UPDATED,
        DELETED,
        STATUS_CHANGED
    }

    private final Student student;
    private final EventType eventType;
    private final String performedBy;

    public StudentEvent(Object source, Student student, EventType eventType, String performedBy) {
        super(source);
        this.student = student;
        this.eventType = eventType;
        this.performedBy = performedBy;
    }
    @Override
    public String toString() {
        return String.format("StudentEvent{type=%s, studentId=%d, performedBy='%s'}",
                eventType, student.getId(), performedBy);
    }

}
