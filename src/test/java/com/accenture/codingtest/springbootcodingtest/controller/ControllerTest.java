package com.accenture.codingtest.springbootcodingtest.controller;

import com.accenture.codingtest.springbootcodingtest.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Tan Oliver, GfK
 *
 * Normally I'd use `@WebIntegration` and then use `MockMvc`
 * Also probably include TestContainers (If i knew which database we were using) so it can be
 * autowired and the assertion can be done against the database to confirm the relevant entities
 * were created/modified/deleted etc
 */
@DataJpaTest
class ControllerTest {

    private static final TestRestTemplate TEMPLATE = new TestRestTemplate();

    @Test
    @DisplayName("Given I am a valid user, When I try to create a user, Then I should receive a CREATED status")
    void createUserTest() {
        var response = TEMPLATE.postForEntity("/api/v1/users", new User(UUID.randomUUID(), "Marika", "RadagonIsMarika"), User.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        // Query DB and assert the values are correct
    }

    // There's actually two seperate assertions here (Create project + Assign users),
    // might be more relevant to split them.
    @Test
    @DisplayName("Given I am a valid Product Owner and there are 2 valid users for that project," +
            "When I create a project and assign 2 users, " +
            "Then I should have a project with 2 assigned users")
    void createProjectAssignUsers() {
        // Setup 2 valid Users
        var authenticatedTemplate = new TestRestTemplate("product_owner", "1234");
        // POST call to /api/v1/projects with valid PO credentials to create project
        // POST call to /api/v1/users/{task_id}/{user_id} assign user 1
        // POST call to /api/v1/users/{task_id}/{user_id} assign user 2
        // Query DB and assert the values are correctly updated
    }

    @Test
    @DisplayName("Given I am a valid User assigned to a valid Task" +
            "When I reassign the task status" +
            "Then I should have an updated Task with correct status")
    void reassignTask() {
        // Setup User with a valid Task i.e. Task belongs to that user and with status IN_PROGRESS
        // PATCH call to /users/{task_id}/{user_id} with valid user credentials to update task status
        // Query DB and assert the values are correctly updated
    }

}
