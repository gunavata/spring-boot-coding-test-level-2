package com.accenture.codingtest.springbootcodingtest.entity;

import org.springframework.lang.NonNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

/**
 * @author Tan Oliver, GfK
 */
@Entity
public class Task {

    @Id
    @GeneratedValue
    UUID id;

    @NonNull
    String title;

    String description;

    @NonNull
    String status;

    @NonNull
    UUID project_id;

    @NonNull
    UUID user_id;

    public void replace(Task newTask) {
        this.title = newTask.title;
        this.description = newTask.description;
        this.status = newTask.status;
        this.project_id = newTask.project_id;
        this.user_id = newTask.user_id;
    }

}
