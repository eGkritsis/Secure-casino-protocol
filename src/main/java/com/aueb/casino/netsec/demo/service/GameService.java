package com.aueb.casino.netsec.demo.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    public String generateRandomString() {
        return RandomStringUtils.randomAlphanumeric(10); // Generate a random string
    }

    public int generateRandomNumber() {
        return (int) (Math.random() * 6) + 1; // Generate a random number between 1 and 6
    }

    public String computeHash(String input) {
        return DigestUtils.sha256Hex(input); // Compute SHA-256 hash
    }

    public String determineResult(int playerChoice, int serverChoice) {
        if (playerChoice > serverChoice) {
            return "User wins!";
        } else if (playerChoice < serverChoice) {
            return "User loses!";
        } else {
            return "It's a tie!";
        }
    }
    
}