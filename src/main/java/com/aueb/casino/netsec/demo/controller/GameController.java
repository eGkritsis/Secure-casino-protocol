package com.aueb.casino.netsec.demo.controller;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/game")
public class GameController {

    @PostMapping("/play")
    public String play() {

        // Step 1: Generate random strings
        String rA = RandomStringUtils.randomAlphanumeric(10); // Anna's random string
        String rS = RandomStringUtils.randomAlphanumeric(10); // Server's random string

        // Step 2: Anna chooses a number (randomly, since she doesn't input it)
        int playerChoice = (int) (Math.random() * 6) + 1;

        // Step 3: Server sends rS to Anna (not shown to Anna)
        // Step 4: Anna computes the commitment
        String commitmentInput = playerChoice + rA + rS;
        String hCommit = DigestUtils.sha256Hex(commitmentInput);

        // Step 5: Server chooses its number
        int serverChoice = (int) (Math.random() * 6) + 1;

        // Step 6: Anna reveals her number (not shown to Anna)
        String revealedInput = playerChoice + rA + rS;
        String h2 = DigestUtils.sha256Hex(revealedInput);

        // Step 7: Server verifies the commitment
        if (!h2.equals(hCommit)) {
            return "Error: Invalid commitment!";
        }

        // Step 8: Determine the result
        String result;
        if (playerChoice > serverChoice) {
            result = "User wins!";
        } else if (playerChoice < serverChoice) {
            result = "User loses!";
        } else {
            result = "It's a tie!";
        }

        return "redirect:/api/auth/home?result=" + result +
        "&playerChoice=" + playerChoice +
        "&serverChoice=" + serverChoice +
        "&rA=" + rA +
        "&rS=" + rS +
        "&hCommit=" + hCommit +
        "&h2=" + h2;
    }
}