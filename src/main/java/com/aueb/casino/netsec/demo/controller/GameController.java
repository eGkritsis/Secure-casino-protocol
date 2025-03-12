package com.aueb.casino.netsec.demo.controller;

import com.aueb.casino.netsec.demo.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/game")
public class GameController {

    @Autowired
    private GameService gameService;

    @PostMapping("/play")
    public String play(Model model) {
        // Step 1: Generate random strings
        String rA = gameService.generateRandomString(); // User's random string
        String rS = gameService.generateRandomString(); // Server's random string

        // Step 2: User chooses a number
        int playerChoice = gameService.generateRandomNumber(); // User's number

        // Step 3: Server sends rS to User
        // Step 4: User computes the commitment
        String commitmentInput = playerChoice + rA + rS;
        String hCommit = gameService.computeHash(commitmentInput);

        // Step 5: Server chooses its number
        int serverChoice = gameService.generateRandomNumber(); // Server's number

        // Step 6: User reveals her number (not shown to User)
        String revealedInput = playerChoice + rA + rS;
        String h2 = gameService.computeHash(revealedInput);

        // Step 7: Server verifies the commitment
        if (!h2.equals(hCommit)) {
            model.addAttribute("error", "Invalid commitment!");
            return "error"; // Render an error page
        }

        // Step 8: Determine the result
        String result = gameService.determineResult(playerChoice, serverChoice);

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