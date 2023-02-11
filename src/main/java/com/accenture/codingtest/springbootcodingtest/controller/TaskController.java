package com.accenture.codingtest.springbootcodingtest.controller;

import com.accenture.codingtest.springbootcodingtest.entity.Task;
import com.accenture.codingtest.springbootcodingtest.repository.TaskRepository;
import com.accenture.codingtest.springbootcodingtest.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.*;

/**
 * @author Tan Oliver
 */
@Controller
@RequestMapping("/api/v1/tasks")
@Secured({"PRODUCT_OWNER"})
public class TaskController {

    private final TaskRepository repository;

    private final TaskService service;

    public TaskController(TaskRepository repository, TaskService service) {
        this.repository = repository;
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        var projectList = this.repository.findAll();
        return ok(projectList);
    }

    @GetMapping(path = "/{task_id}")
    public ResponseEntity<Task> getTaskById(@PathVariable("task_id") UUID id) {
        return this.repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Secured({"PRODUCT_OWNER"})
    public Task createOneTask(@RequestBody Task task) {
        return this.repository.save(task);
    }

    @PutMapping(path = "/{task_id}")
    public ResponseEntity<Object> updateOneTask(@PathVariable("task_id") UUID id, @RequestBody Task newTask) {
        /**
         * Alternatively, we could setup a transactional query to minimize the required calls
         * to database from 2 to 1 (FindById.exists ? save and return 204 : return 404).
         */
        var task = this.repository.findById(id);
        if (task.isEmpty()) {
            return notFound().build();
        }
        task.get().replace(newTask);
        this.repository.save(task.get());
        return noContent().build();
    }

    // I don't know what type of data the client will send me for updating
    @PatchMapping(path = "/users/{task_id}")
    public ResponseEntity<Object> patchOneTask(@PathVariable("task_id") UUID id) {
        return new ResponseEntity<>("I am a teapot", HttpStatus.I_AM_A_TEAPOT);
    }

    @DeleteMapping(path = "/users/{task_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOneTask(@PathVariable("task_id") UUID id) {
        this.repository.deleteById(id);
    }

    // I don't have enough info to check if user is in a given project
    @PatchMapping(path = "/users/{task_id}/{user_id}")
    @Secured({"PRODUCT_OWNER"})
    public ResponseEntity<Object> assignTask(@PathVariable("task_id") UUID taskId, @PathVariable("user_id") UUID userId) {
        var task = this.repository.findById(taskId);
        if (task.isEmpty()) {
            return notFound().build();
        }
        task.get().setUser_id(userId);
        this.repository.save(task.get());
        return noContent().build();
    }

    @PatchMapping(path = "/users/{task_id}/{user_id}/{task_status}")
    public ResponseEntity<Task> updateTaskStatus(@PathVariable("task_id") UUID taskId,
                                                   @PathVariable("user_id") UUID userId,
                                                   @PathVariable("task_status") Task.TaskStatus status) {
        return service.updateTaskStatus(taskId, userId, status)
                .map(ResponseEntity::ok)
                .orElse(badRequest().build());

    }

}
