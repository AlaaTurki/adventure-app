package com.pictet.adventure.controller;

import com.pictet.adventure.dto.ChoiceRequest;
import com.pictet.adventure.dto.GameDTO;
import com.pictet.adventure.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/games")
@CrossOrigin(origins = "http://localhost:4200")
public class GameController {

    @Autowired
    private GameService gameService;

    @PostMapping("/start")
    public ResponseEntity<GameDTO> startGame(@RequestParam Long bookId) {
        return ResponseEntity.ok(gameService.startGame(bookId));
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<GameDTO> getGame(@PathVariable UUID gameId) {
        return ResponseEntity.ok(gameService.getGame(gameId));
    }

    @PostMapping("/{gameId}/choices")
    public ResponseEntity<GameDTO> chooseOption(@PathVariable UUID gameId, @RequestBody ChoiceRequest request) {
        return ResponseEntity.ok(gameService.chooseOption(gameId, request.getOptionIndex()));
    }

    @PostMapping("/{gameId}/save")
    public ResponseEntity<?> saveGame(@PathVariable UUID gameId) {
        gameService.saveGameSnapshot(gameId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/saved")
    public ResponseEntity<?> listSavedGames(@RequestParam(required = false) Long bookId) {
        return ResponseEntity.ok(gameService.listSavedGames(bookId));
    }
}
