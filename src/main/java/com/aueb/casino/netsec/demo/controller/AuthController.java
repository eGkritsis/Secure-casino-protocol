package com.aueb.casino.netsec.demo.controller;

import com.aueb.casino.netsec.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.aueb.casino.netsec.demo.service.UserService;

@Controller
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User()); // Create a new empty user object to bind with the form
        return "register"; // Return the name of the registration template
    }

    @PostMapping("/register")
    public String registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.registerUser(user); // Handle registration logic
        return "redirect:/api/auth/login"; // Redirect to the login page after successful registration
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // Return the login template
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String username, @RequestParam String password, Model model) {
        User foundUser = userService.findByUsername(username);
        if (foundUser != null && passwordEncoder.matches(password, foundUser.getPassword())) {
            return "redirect:/api/auth/home"; // Redirect to the home page if login is successful
        } else {
            model.addAttribute("error", "Invalid credentials");
            return "login"; // Show login page with an error message
        }
    }
}