package com.accenture.codingtest.springbootcodingtest.repository;

import com.accenture.codingtest.springbootcodingtest.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author Tan Oliver, GfK
 */
public interface TaskRepository extends JpaRepository<Task, UUID> {
}
