package com.aueb.casino.netsec.demo.controller;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/game")
public class GameController {

    @PostMapping("/play")
    public String play(Model model) {

        // Step 1: Generate random strings
        String rA = RandomStringUtils.randomAlphanumeric(10); // User's random string
        String rS = RandomStringUtils.randomAlphanumeric(10); // Server's random string

        // Step 2: User chooses a number (randomly, since she doesn't input it)
        int playerChoice = (int) (Math.random() * 6) + 1;

        // Step 3: Server sends rS to User (not shown to User)
        // Step 4: User computes the commitment
        String commitmentInput = playerChoice + rA + rS;
        String hCommit = DigestUtils.sha256Hex(commitmentInput);

        // Step 5: Server chooses its number
        int serverChoice = (int) (Math.random() * 6) + 1;

        // Step 6: User reveals her number (not shown to User)
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

        model.addAttribute("result", result);
        model.addAttribute("playerChoice", playerChoice);
        model.addAttribute("serverChoice", serverChoice);
        model.addAttribute("rA", rA);
        model.addAttribute("rS", rS);
        model.addAttribute("hCommit", hCommit);
        model.addAttribute("h2", h2);   

        return "game";
    }
}