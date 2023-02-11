package com.accenture.codingtest.springbootcodingtest.service;

import com.accenture.codingtest.springbootcodingtest.entity.Task;
import com.accenture.codingtest.springbootcodingtest.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.noContent;

/**
 * @author Tan Oliver, GfK
 */
@Service
public class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    // We assume task is always valid i.e no empty/null check
    public Optional<Task> updateTaskStatus(UUID taskId, UUID userId, Task.TaskStatus status) {
        var task = this.repository.findById(taskId);
        if (task.isEmpty() || task.get().getUser_id() != userId) {
            return Optional.empty();
        }
        task.get().setStatus(status);
        return Optional.of(this.repository.save(task.get()));
    }
}
