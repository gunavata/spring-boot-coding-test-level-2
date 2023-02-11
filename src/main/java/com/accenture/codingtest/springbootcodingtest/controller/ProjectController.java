package com.accenture.codingtest.springbootcodingtest.controller;

import com.accenture.codingtest.springbootcodingtest.entity.Project;
import com.accenture.codingtest.springbootcodingtest.entity.Task;
import com.accenture.codingtest.springbootcodingtest.repository.ProjectRepository;
import com.accenture.codingtest.springbootcodingtest.repository.TaskRepository;
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
@RequestMapping("/api/v1/projects")
public class ProjectController {

    private final ProjectRepository repository;

    public ProjectController(ProjectRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        var projectList = this.repository.findAll();
        return ok(projectList);
    }

    @GetMapping(path = "/{project_id}")
    public ResponseEntity<Project> getProjectById(@PathVariable("project_id") UUID id) {
        return this.repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Secured({"PRODUCT_OWNER"})
    public Project createOneProject(@RequestBody Project project) {
        return this.repository.save(project);
    }

    @PutMapping(path = "/{project_id}")
    public ResponseEntity<Object> updateOneProject(@PathVariable("project_id") UUID id, @RequestBody Project newProject) {
        /**
         * Alternatively, we could setup a transactional query to minimize the required calls
         * to database from 2 to 1 (FindById.exists ? save and return 204 : return 404).
         */
        var project = this.repository.findById(id);
        if (project.isEmpty()) {
            return notFound().build();
        }
        project.get().replace(newProject);
        this.repository.save(project.get());
        return noContent().build();
    }

    // I don't know what type of data the client will send me for updating
    @PatchMapping(path = "/users/{project_id}")
    public ResponseEntity<Object> patchOneProject(@PathVariable("project_id") UUID id) {
        return new ResponseEntity<>("I am a teapot", HttpStatus.I_AM_A_TEAPOT);
    }

    @DeleteMapping(path = "/users/{project_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOneTask(@PathVariable("project_id") UUID id) {
        this.repository.deleteById(id);
    }

}
