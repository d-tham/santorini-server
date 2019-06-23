package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.Application;
import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import ch.uzh.ifi.seal.soprafs19.controller.UserController;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.annotation.DirtiesContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.util.Date;

/**
 * Test class for the the controller.
 * Some tests don't work when ran all together.
 * @see UserController
 */
@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes= Application.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)

public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Test
    public void testGetUsers() throws Exception {
        this.mockMvc.perform(get("/users")).andExpect(status().isOk());
    }

    @Test
    public void testCreateUser() throws Exception {
        this.mockMvc.perform(post("/users")
                .content("{\"username\": \"testUsername\", \"password\" : \"testPassword\"}")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated());
    }

    @Test
    public void testCreateUserConflict() throws Exception {
        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");

        userService.createUser(testUser);

        this.mockMvc.perform(post("/users")
                .content("{\"username\": \"testUsername\", \"password\" : \"testPassword\"}")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isConflict());
    }

    @Test
    public void testGetUserValidToken() throws Exception {
        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");

        User createdUser = userService.createUser(testUser);

        this.mockMvc.perform(get("/users/1").header("Access-Token", createdUser.getToken())).andExpect(status().isOk());
    }

    @Test
    public void testGetUserInvalidToken() throws Exception {
        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");

        userService.createUser(testUser);

        this.mockMvc.perform(get("/users/1").header("Access-Token", "Invalid Token")).andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetUserNotFound() throws Exception {
        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");

        User createdUser = userService.createUser(testUser);

        this.mockMvc.perform(get("/users/9").header("Access-Token", createdUser.getToken())).andExpect(status().isNotFound());
    }

    @Test
    public void testLoginUser() throws Exception {
        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");

        userService.createUser(testUser);
        Assert.assertEquals(userService.getUserByToken(testUser.getToken()).getStatus(), UserStatus.OFFLINE);

        this.mockMvc.perform(post("/users/login")
                .content("{\"username\": \"testUsername\", \"password\" : \"testPassword\"}")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        Assert.assertEquals(userService.getUserByToken(testUser.getToken()).getStatus(), UserStatus.ONLINE);
    }

    @Test
    public void testLoginUserInvalid() throws Exception {
        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");

        userService.createUser(testUser);
        Assert.assertEquals(userService.getUserByToken(testUser.getToken()).getStatus(), UserStatus.OFFLINE);

        this.mockMvc.perform(post("/users/login")
                .content("{\"username\": \"testUsername\", \"password\" : \"wrongPassword\"}")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isUnauthorized());

        Assert.assertEquals(userService.getUserByToken(testUser.getToken()).getStatus(), UserStatus.OFFLINE);
    }

    @Test
    public void testLoginUserNotFound() throws Exception {
        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");

        userService.createUser(testUser);
        Assert.assertEquals(userService.getUserByToken(testUser.getToken()).getStatus(), UserStatus.OFFLINE);

        this.mockMvc.perform(post("/users/login")
                .content("{\"username\": \"wrongUsername\", \"password\" : \"wrongPassword\"}")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());

        Assert.assertEquals(userService.getUserByToken(testUser.getToken()).getStatus(), UserStatus.OFFLINE);
    }

    @Test
    public void testLogoutUser() throws Exception {
        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");

        userService.createUser(testUser);
        Assert.assertEquals(userService.getUserByToken(testUser.getToken()).getStatus(), UserStatus.OFFLINE);

        this.mockMvc.perform(post("/users/login")
                .content("{\"username\": \"testUsername\", \"password\" : \"testPassword\"}")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        Assert.assertEquals(userService.getUserByToken(testUser.getToken()).getStatus(), UserStatus.ONLINE);

        this.mockMvc.perform(post("/users/logout")
                .header("Access-Token", testUser.getToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        Assert.assertEquals(userService.getUserByToken(testUser.getToken()).getStatus(), UserStatus.OFFLINE);
    }

    @Test
    public void testUpdateUser() throws Exception {
        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");

        User createdUser = userService.createUser(testUser);

        this.mockMvc.perform(put("/users/1")
                .content("{\"username\": \"changedName\"}")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Access-Token", createdUser.getToken()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testUpdateUserInvalid() throws Exception {
        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");

        userService.createUser(testUser);

        this.mockMvc.perform(put("/users/1")
                .content("{\"username\": \"changedName\"}")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Access-Token", "Invalid Token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testUpdateUserNotFound() throws Exception {
        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");

        User createdUser = userService.createUser(testUser);

        this.mockMvc.perform(put("/users/2")
                .content("{\"username\": \"changedName\"}")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Access-Token", createdUser.getToken()))
                .andExpect(status().isNotFound());
    }
}