package com.example.taskmanager.repository;

import com.example.taskmanager.model.Priority;
import com.example.taskmanager.model.Status;
import com.example.taskmanager.model.Task;
import org.springframework.data.jpa.domain.Specification;

public final class TaskSpecification {

    private TaskSpecification() {
        // Utility class
    }

    public static Specification<Task> withKeyword(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return Specification.where(null);
        }

        String pattern = "%" + keyword.toLowerCase() + "%";
        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("title")), pattern),
                cb.like(cb.lower(root.get("description")), pattern)
        );
    }

    public static Specification<Task> withStatus(Status status) {
        if (status == null) {
            return Specification.where(null);
        }

        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }

    public static Specification<Task> withPriority(Priority priority) {
        if (priority == null) {
            return Specification.where(null);
        }

        return (root, query, cb) -> cb.equal(root.get("priority"), priority);
    }

    public static Specification<Task> withFilters(String keyword, Status status, Priority priority) {
        return Specification.where(withKeyword(keyword))
                .and(withStatus(status))
                .and(withPriority(priority));
    }
}
