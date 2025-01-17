package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.Application;
import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Iterator;

/**
 * Test class for the UserResource REST resource.
 *
 * @see UserService
 */
@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes= Application.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)

public class UserServiceTest {
    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    public void createUser() {
        userRepository.deleteAll();
        Assert.assertNull(userRepository.findByUsername("testUsername"));

        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");

        User createdUser = userService.createUser(testUser);

        Assert.assertNotNull(createdUser.getToken());
        Assert.assertNotNull(createdUser.getCreationDate());
        Assert.assertEquals(createdUser.getStatus(),UserStatus.OFFLINE); // Changed to offline since no log on during creation.
        Assert.assertEquals(createdUser, userRepository.findByToken(createdUser.getToken()));
    }

    @Test
    public void loginUser() {
        userRepository.deleteAll();

        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");

        userService.createUser(testUser);
        User loggedInUser = userService.loginUser("testUsername", "testPassword");

        Assert.assertNotNull(loggedInUser);
        Assert.assertEquals(UserStatus.ONLINE, userService.getUserByUsername("testUsername").getStatus());
    }

    @Test
    public void logoutUser() {
        userRepository.deleteAll();

        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");

        userService.createUser(testUser);
        User loggedInUser = userService.loginUser("testUsername", "testPassword");

        Assert.assertNotNull(loggedInUser);
        Assert.assertEquals(UserStatus.ONLINE, userService.getUserByUsername("testUsername").getStatus());

        userService.logoutUser(loggedInUser.getToken());
        Assert.assertEquals(userService.getUserByUsername("testUsername").getStatus(), UserStatus.OFFLINE);
    }

    @Test
    public void updateUser() {
        userRepository.deleteAll();

        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");

        userService.createUser(testUser);

        userService.updateUser(1, "changedUsername"); // Causes null-pointer exception when ran together.

        Assert.assertEquals(userService.getUserByUserId(1).getUsername(), "changedUsername");
    }

    @Test
    public void getUsers() {
        userRepository.deleteAll();

        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        userService.createUser(testUser);

        User testUser2 = new User();
        testUser2.setUsername("testUsername2");
        testUser2.setPassword("testPassword2");
        userService.createUser(testUser2);

        User testUser3 = new User();
        testUser3.setUsername("testUsername3");
        testUser3.setPassword("testPassword3");
        userService.createUser(testUser3);

        Iterable<User> returnedUsers = userService.getUsers();
        Iterator<User> userIter = returnedUsers.iterator();

        Assert.assertEquals(userIter.next(), testUser);
        userIter.remove();
        Assert.assertEquals(userIter.next(), testUser2);
        userIter.remove();
        Assert.assertEquals(userIter.next(), testUser3);

    }

    @Test
    public void getUserByUsername() {
        userRepository.deleteAll();

        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");

        User createdUser = userService.createUser(testUser);

        Assert.assertNotNull(userService.getUsers());
        Assert.assertEquals(createdUser, userService.getUserByUsername("testUsername"));

    }

    @Test
    public void getUserByUserId() {
        userRepository.deleteAll();

        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");

        User createdUser = userService.createUser(testUser);

        Assert.assertNotNull(userService.getUsers());
        Assert.assertEquals(createdUser, userService.getUserByUserId(1));

    }

    @Test
    public void getUserByToken() {
        userRepository.deleteAll();

        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");

        User createdUser = userService.createUser(testUser);

        Assert.assertNotNull(userService.getUsers());
        Assert.assertEquals(createdUser, userService.getUserByToken(createdUser.getToken()));
    }
}
