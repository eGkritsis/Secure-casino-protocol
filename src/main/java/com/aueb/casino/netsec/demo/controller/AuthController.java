package com.aueb.casino.netsec.demo.controller;

import com.aueb.casino.netsec.demo.DTO.UserDto;
import com.aueb.casino.netsec.demo.config.JwtUtil;
import com.aueb.casino.netsec.demo.entity.User;
import com.aueb.casino.netsec.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtUtil jwtUtil = new JwtUtil();

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    private BCryptPasswordEncoder passwordEncoder;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/home")
    public String home(Model model, Principal principal) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("userdetail", userDetails);
        return "home";
    }

    @GetMapping("/login")
    public String login(Model model, UserDto userDto) {
        model.addAttribute("user", userDto);
        return "login";
    }

    @PostMapping("/login")
    public String authenticateUser(@RequestParam String username, @RequestParam String password, Model model) {
        if ("user".equals(username) && "password".equals(password)) {
            String token = jwtUtil.generateToken(username);
            model.addAttribute("token", token);
            return "home"; // Redirects to a welcome page
        } else {
            model.addAttribute("error", "Invalid credentials");
            return "login"; // Return to login page with error
        }
    }

    @GetMapping("/register")
    public String register(Model model, UserDto userDto) {
        model.addAttribute("user", userDto);
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") @Valid UserDto userDto, BindingResult result, Model model) {
        // Check if there are any validation errors
        try {
            if (result.hasErrors()) {
                // Return the registration page with validation errors
                return "register";
            } else {
                // Check if the user already exists
                User existingUser = userService.findByUsername(userDto.getUsername());
                if (existingUser != null) {
                    model.addAttribute("error", "User already exists");
                    return "register"; // Return to the form with the error message
                }

                // Save the user to the database
                userService.save(userDto);

                // Add success message to model
                model.addAttribute("success", "Registration successful! You can now log in.");

                // Stay on the registration page with the success message instead of redirecting
                return "register";
            }
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred. Please try again.");
        }
        return "register";
    }
}