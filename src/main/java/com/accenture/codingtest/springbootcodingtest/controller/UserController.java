package com.accenture.codingtest.springbootcodingtest.controller;

import com.accenture.codingtest.springbootcodingtest.entity.User;
import com.accenture.codingtest.springbootcodingtest.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.accenture.codingtest.springbootcodingtest.controller.UserController.DEFAULT_REQUEST_MAPPING;
import static org.springframework.http.ResponseEntity.*;

/**
 * @author Tan Oliver
 */
@Controller
@RequestMapping(DEFAULT_REQUEST_MAPPING)
public class UserController {

    static final String DEFAULT_REQUEST_MAPPING = "/api/v1/users";

    private final UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        var userList = this.repository.findAll();
        return ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userList);
    }

    @GetMapping(path = "/{user_id}")
    public ResponseEntity<User> getUserById(@PathVariable UUID user_id) {
        return this.repository.findById(user_id)
                .map(user -> ok().contentType(MediaType.APPLICATION_JSON).body(user))
                .orElse(notFound().build());
    }

    // TODO Most of the following API endpoints deal with one resource, maybe should user singular?
    @PostMapping(path = "/users")
    @ResponseStatus(HttpStatus.CREATED)
    public User createOneUser(@RequestBody User user) {
        return this.repository.save(user);
    }

    @PutMapping(path = "/users/{user_id}")
    public ResponseEntity<Object> updateOneUser(@PathVariable UUID user_id, @RequestBody User newUser) {
        /**
         * Alternatively, we could setup a transactional query to minimize the required calls
         * to database from 2 to 1 (FindById.exists ? save and return 204 : return 404).
         */
        var user = this.repository.findById(user_id);
        if (user.isEmpty()) {
            return notFound().build();
        }
        user.get().replace(newUser);
        this.repository.save(user.get());
        return noContent().build();
    }

    // I don't know what type of data the client will send me for updating
    @PatchMapping(path = "/users/{user_id}")
    public ResponseEntity<Object> patchOneUser(@PathVariable UUID user_id) {
        return new ResponseEntity<>("I am a teapot", HttpStatus.I_AM_A_TEAPOT);
    }

    @DeleteMapping(path = "/users/{user_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOneUser(@PathVariable UUID user_id) {
        this.repository.deleteById(user_id);
    }

}
