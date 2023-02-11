package com.accenture.codingtest.springbootcodingtest.controller;

import com.accenture.codingtest.springbootcodingtest.entity.User;
import com.accenture.codingtest.springbootcodingtest.model.RoleEnum;
import com.accenture.codingtest.springbootcodingtest.repository.UserRepository;
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
@RequestMapping("/api/v1/users")
@Secured({"ADMIN"})
public class UserController {

    private final UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        var userList = this.repository.findAll();
        return ok(userList);
    }

    @GetMapping(path = "/{user_id}")
    public ResponseEntity<User> getUserById(@PathVariable("user_id") UUID id) {
        return this.repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createOneUser(@RequestBody User user) {
        return this.repository.save(user);
    }

    @PutMapping(path = "/{user_id}")
    public ResponseEntity<Object> updateOneUser(@PathVariable("user_id") UUID id, @RequestBody User newUser) {
        /**
         * Alternatively, we could setup a transactional query to minimize the required calls
         * to database from 2 to 1 (FindById.exists ? save and return 204 : return 404).
         */
        var user = this.repository.findById(id);
        if (user.isEmpty()) {
            return notFound().build();
        }
        user.get().replace(newUser);
        this.repository.save(user.get());
        return noContent().build();
    }

    // I don't know what type of data the client will send me for updating
    @PatchMapping(path = "/{user_id}")
    public ResponseEntity<Object> patchOneUser(@PathVariable("user_id") UUID id) {
        return new ResponseEntity<>("I am a teapot", HttpStatus.I_AM_A_TEAPOT);
    }

    @DeleteMapping(path = "/{user_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOneUser(@PathVariable("user_id") UUID id) {
        this.repository.deleteById(id);
    }

}
