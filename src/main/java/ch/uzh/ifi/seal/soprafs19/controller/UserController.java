package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class UserController {

    private final UserService service;

    UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/users")
    Iterable<User> all() {
        return service.getUsers();
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    User createUser(@RequestBody User newUser) {
        if (this.service.getUserByUsername(newUser.getUsername()) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists!"); }

        return this.service.createUser(newUser);
    }

    @GetMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    User single(@RequestHeader(value = "Access-Token") String token, @PathVariable("userId") long userId) {
        if (!this.service.validateToken(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token!"); }
        else if (this.service.getUserByUserId(userId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User was not found!"); }

        return this.service.getUserByUserId(userId);}

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    User loginUser(@RequestBody User tempUser) {
        if (this.service.getUserByUsername(tempUser.getUsername()) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User was not found!"); }
        else if (this.service.loginUser(tempUser.getUsername(), tempUser.getPassword()) == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong login credentials!");
        }
        return this.service.loginUser(tempUser.getUsername(), tempUser.getPassword());
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    void logoutUser(@RequestHeader(value ="Access-Token") String token) {
        if (this.service.getUserByToken(token) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User was not found!"); }

        this.service.logoutUser(token);
    }

    @PutMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void updateUser(@RequestHeader(value = "Access-Token") String token, @PathVariable("userId") long userId, @RequestBody User tempUser) {
        if (this.service.getUserByUserId(userId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User was not found!");
        } else if (!this.service.getUserByUserId(userId).getToken().equals(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authorized!");
        } else if (this.service.getUserByUsername(tempUser.getUsername()) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Already another user with the same name exists!");
        } else {
            this.service.updateUser(userId, tempUser.getUsername());
        }
    }

}
