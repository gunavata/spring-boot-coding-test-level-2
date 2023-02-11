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
public class Project {

    @Id
    @GeneratedValue
    UUID id;

    @NonNull
    String name;

    // Can actually just use setter since its a single field
    public void replace(Project newProject) {
        this.name = name;
    }
}
