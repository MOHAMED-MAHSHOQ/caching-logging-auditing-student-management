package com.logbook.studentmanagement.listener;

import com.logbook.studentmanagement.event.StudentEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
public class StudentEventListener {
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleStudentCreatedAsync(StudentEvent event) {
        if (event.getEventType() != StudentEvent.EventType.CREATED) return;

        log.info("[ASYNC EVENT] Processing CREATED event for studentId={}. " +
                        "Simulating: sending welcome email to {}...",
                event.getStudent().getId(),
                event.getStudent().getEmail());

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        log.info("[ASYNC EVENT] Welcome email 'sent' to {}", event.getStudent().getEmail());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleStudentStatusChanged(StudentEvent event) {
        if (event.getEventType() != StudentEvent.EventType.STATUS_CHANGED) return;

        log.info("[ASYNC EVENT] Student {} status changed. " +
                        "Simulating: notifying admin dashboard...",
                event.getStudent().getId());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleStudentDeletedAsync(StudentEvent event) {
        if (event.getEventType() != StudentEvent.EventType.DELETED) return;

        log.info("[ASYNC EVENT] Processing DELETED event for studentId={}. " +
                        "Simulating: removing from search index, notifying external systems...",
                event.getStudent().getId());
    }
}
