package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.Date;

@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Iterable<User> getUsers() {
        return this.userRepository.findAll();
    }

    public User createUser(User newUser) {
        newUser.setToken(UUID.randomUUID().toString());
        newUser.setStatus(UserStatus.OFFLINE);
        newUser.setCreationDate(new Date());
        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public User loginUser(String username, String password) {
        User tempUser = this.getUserByUsername(username);
        if (password.equals(tempUser.getPassword())) {
            tempUser.setStatus(UserStatus.ONLINE);
            log.debug("Logging in User: {}", tempUser);
            return tempUser;
        } else {
            return null;
        }
    }

    public void logoutUser(String token) {
        User tempUser = this.getUserByToken(token);
        tempUser.setStatus(UserStatus.OFFLINE);
        userRepository.save(tempUser);
        log.debug("Logging out User: {}", tempUser);
    }

    public void updateUser(long id, String username) {
        User tempUser = this.getUserByUserId(id);
        tempUser.setUsername(username);
        userRepository.save(tempUser);
    }

    public User getUserByUsername (String username) { return this.userRepository.findByUsername(username); }

    public User getUserByUserId (long id) {
        return this.userRepository.findById(id);
    }

    public User getUserByToken (String token) { return this.userRepository.findByToken(token); }

    public Boolean validateToken(String token) {
        return this.userRepository.findByToken(token) != null;
    }

}
