package com.accenture.codingtest.springbootcodingtest.controller;

import com.accenture.codingtest.springbootcodingtest.entity.Task;
import com.accenture.codingtest.springbootcodingtest.repository.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
public class TaskController {

    private final TaskRepository repository;

    public TaskController(TaskRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        var projectList = this.repository.findAll();
        return ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(projectList);
    }

    @GetMapping(path = "/{task_id}")
    public ResponseEntity<Task> getTaskById(@PathVariable("task_id") UUID id) {
        return this.repository.findById(id)
                .map(task -> ok().contentType(MediaType.APPLICATION_JSON).body(task))
                .orElse(notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task createOneUser(@RequestBody Task task) {
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

}
