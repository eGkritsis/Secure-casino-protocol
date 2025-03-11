package controller;

import entity.User;
import service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private UserService userService;

    @GetMapping("/user/{username}")
    public String testDatabaseConnection(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        if (user != null) {
            return "User found: " + user.getUsername() + ", " + user.getFirstName() + " " + user.getLastName();
        } else {
            return "User not found!";
        }
    }
}
