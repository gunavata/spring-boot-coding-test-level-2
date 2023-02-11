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
}
