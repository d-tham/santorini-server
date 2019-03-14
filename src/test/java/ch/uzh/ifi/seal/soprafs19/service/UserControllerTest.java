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

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
public class UserControllerTest {

    //@Qualifier("userRepository")

    @Autowired
    private MockMvc mockMvc;

    @Qualifier ("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserController userController;

    @Test
    public void testGetUsers() throws Exception {
        this.mockMvc.perform(get("/users")).andExpect(status().isOk());
    }

    @Test
    public void testCreateUser() throws Exception {
        userRepository.deleteAll();
        this.mockMvc.perform(post("/users")
                .content("{\"username\": \"testUsername\", \"name\" : \"testName\", \"password\" : \"testPassword\"}")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated());
    }

    @Test
    public void testCreateUserConflict() throws Exception {
        userRepository.deleteAll();

        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setName("testName");
        testUser.setPassword("testPassword");
        testUser.setBirthDate(new Date());

        userService.createUser(testUser);

        this.mockMvc.perform(post("/users")
                .content("{\"username\": \"testUsername\", \"name\" : \"testName\", \"password\" : \"testPassword\"}")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isConflict());
    }

    @Test
    public void testGetUserValidToken() throws Exception {
        userRepository.deleteAll();
        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setName("testName");
        testUser.setPassword("testPassword");
        testUser.setBirthDate(new Date());

        User createdUser = userService.createUser(testUser);

        this.mockMvc.perform(get("/users/1").header("Access-Token", createdUser.getToken())).andExpect(status().isOk());
    }

    @Test
    public void testGetUserInvalidToken() throws Exception {
        userRepository.deleteAll();
        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setName("testName");
        testUser.setPassword("testPassword");
        testUser.setBirthDate(new Date());

        userService.createUser(testUser);

        this.mockMvc.perform(get("/users/1").header("Access-Token", "Invalid Token")).andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetUserNotFound() throws Exception {
        userRepository.deleteAll();
        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setName("testName");
        testUser.setPassword("testPassword");
        testUser.setBirthDate(new Date());

        User createdUser = userService.createUser(testUser);

        this.mockMvc.perform(get("/users/9").header("Access-Token", createdUser.getToken())).andExpect(status().isNotFound());
    }

    @Test
    public void testLoginUser() throws Exception {
        userRepository.deleteAll();
        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setName("testName");
        testUser.setPassword("testPassword");
        testUser.setBirthDate(new Date());

        userService.createUser(testUser);
        Assert.assertEquals(userService.getUserByToken(testUser.getToken()).getStatus(), UserStatus.OFFLINE);

        this.mockMvc.perform(post("/login")
                .content("{\"username\": \"testUsername\", \"password\" : \"testPassword\"}")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        Assert.assertEquals(userService.getUserByToken(testUser.getToken()).getStatus(), UserStatus.ONLINE);
    }

    @Test
    public void testLoginUserInvalid() throws Exception {
        userRepository.deleteAll();
        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setName("testName");
        testUser.setPassword("testPassword");
        testUser.setBirthDate(new Date());

        userService.createUser(testUser);
        Assert.assertEquals(userService.getUserByToken(testUser.getToken()).getStatus(), UserStatus.OFFLINE);

        this.mockMvc.perform(post("/login")
                .content("{\"username\": \"testUsername\", \"password\" : \"wrongPassword\"}")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isUnauthorized());

        Assert.assertEquals(userService.getUserByToken(testUser.getToken()).getStatus(), UserStatus.OFFLINE);
    }

    @Test
    public void testLoginUserNotFound() throws Exception {
        userRepository.deleteAll();
        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setName("testName");
        testUser.setPassword("testPassword");
        testUser.setBirthDate(new Date());

        userService.createUser(testUser);
        Assert.assertEquals(userService.getUserByToken(testUser.getToken()).getStatus(), UserStatus.OFFLINE);

        this.mockMvc.perform(post("/login")
                .content("{\"username\": \"wrongUsername\", \"password\" : \"wrongPassword\"}")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());

        Assert.assertEquals(userService.getUserByToken(testUser.getToken()).getStatus(), UserStatus.OFFLINE);
    }

    @Test
    public void testLogoutUser() throws Exception {
        userRepository.deleteAll();
        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setName("testName");
        testUser.setPassword("testPassword");
        testUser.setBirthDate(new Date());

        userService.createUser(testUser);
        Assert.assertEquals(userService.getUserByToken(testUser.getToken()).getStatus(), UserStatus.OFFLINE);

        this.mockMvc.perform(post("/login")
                .content("{\"username\": \"testUsername\", \"password\" : \"testPassword\"}")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        Assert.assertEquals(userService.getUserByToken(testUser.getToken()).getStatus(), UserStatus.ONLINE);

        this.mockMvc.perform(post("/logout")
                .header("Access-Token", testUser.getToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        Assert.assertEquals(userService.getUserByToken(testUser.getToken()).getStatus(), UserStatus.OFFLINE);
    }

    @Test
    public void testUpdateUser() throws Exception {
        userRepository.deleteAll();
        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setName("testName");
        testUser.setPassword("testPassword");
        testUser.setBirthDate(new Date());

        User createdUser = userService.createUser(testUser);

        this.mockMvc.perform(put("/users/1")
                .content("{\"username\": \"changedName\", \"birthDate\" : \"1999-12-13T00:00:00.000+0000\"}")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Access-Token", createdUser.getToken()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testUpdateUserInvalid() throws Exception {
        userRepository.deleteAll();
        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setName("testName");
        testUser.setPassword("testPassword");
        testUser.setBirthDate(new Date());

        userService.createUser(testUser);

        this.mockMvc.perform(put("/users/1")
                .content("{\"username\": \"changedName\", \"birthDate\" : \"1999-12-13T00:00:00.000+0000\"}")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Access-Token", "Invalid Token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testUpdateUserNotFound() throws Exception {
        userRepository.deleteAll();
        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setName("testName");
        testUser.setPassword("testPassword");
        testUser.setBirthDate(new Date());

        User createdUser = userService.createUser(testUser);

        this.mockMvc.perform(put("/users/2")
                .content("{\"username\": \"changedName\", \"birthDate\" : \"1999-12-13T00:00:00.000+0000\"}")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Access-Token", createdUser.getToken()))
                .andExpect(status().isNotFound());
    }


}