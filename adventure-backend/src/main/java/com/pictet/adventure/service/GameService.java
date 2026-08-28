package com.pictet.adventure.service;

import com.pictet.adventure.dto.GameDTO;
import com.pictet.adventure.exception.AdventureException;
import com.pictet.adventure.model.Book;
import com.pictet.adventure.model.Choice;
import com.pictet.adventure.model.Game;
import com.pictet.adventure.model.GameStatus;
import com.pictet.adventure.model.Section;
import com.pictet.adventure.repository.BookRepository;
import com.pictet.adventure.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private BookRepository bookRepository;

    public GameDTO startGame(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new AdventureException("Book not found with id: " + bookId, HttpStatus.NOT_FOUND));

        Integer beginSectionId = book.getSections().stream()
                .filter(section -> section != null && "BEGIN".equalsIgnoreCase(section.getType()))
                .map(Section::getSectionId)
                .findFirst()
                .orElseThrow(() -> new AdventureException("Book does not contain a valid BEGIN section", HttpStatus.BAD_REQUEST));

        Game game = new Game();
        game.setBookId(book.getId());
        game.setCurrentSectionId(beginSectionId);
        game.setHealth(10);
        game.setStatus(GameStatus.PLAYING);

        Game savedGame = gameRepository.save(game);
        return toDto(savedGame, book.getTitle());
    }

    public GameDTO getGame(UUID gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new AdventureException("Game not found with id: " + gameId, HttpStatus.NOT_FOUND));
        Book book = bookRepository.findById(game.getBookId())
                .orElseThrow(() -> new AdventureException("Book not found with id: " + game.getBookId(), HttpStatus.NOT_FOUND));
        return toDto(game, book.getTitle());
    }

    public GameDTO chooseOption(UUID gameId, Integer optionIndex) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new AdventureException("Game not found with id: " + gameId, HttpStatus.NOT_FOUND));

        if (game.getStatus() == GameStatus.WON || game.getStatus() == GameStatus.DEAD) {
            throw new AdventureException("Game already ended", HttpStatus.CONFLICT);
        }

        Book book = bookRepository.findById(game.getBookId())
                .orElseThrow(() -> new AdventureException("Book not found with id: " + game.getBookId(), HttpStatus.NOT_FOUND));

        Section currentSection = book.getSections().stream()
                .filter(section -> section != null && game.getCurrentSectionId() != null && game.getCurrentSectionId().equals(section.getSectionId()))
                .findFirst()
                .orElseThrow(() -> new AdventureException("Current section not found in book", HttpStatus.BAD_REQUEST));

        var options = currentSection.getOptions() == null ? java.util.Collections.<Choice>emptyList() : currentSection.getOptions();

        if (optionIndex == null || optionIndex < 0 || optionIndex >= options.size()) {
            throw new AdventureException("Invalid option index", HttpStatus.BAD_REQUEST);
        }

        Choice choice = options.get(optionIndex);
        if (choice.getGotoId() == null) {
            throw new AdventureException("Selected option has no destination", HttpStatus.BAD_REQUEST);
        }

        int healthDelta = 0;
        if (choice.getConsequence() != null) {
            healthDelta = BookValidationService.resolveHealthDelta(new com.pictet.adventure.dto.ConsequenceDTO(
                    choice.getConsequence().getType(),
                    choice.getConsequence().getValue(),
                    choice.getConsequence().getText()));
        }

        game.setCurrentSectionId(choice.getGotoId());

        int updatedHealth = Math.max(0, game.getHealth() + healthDelta);
        game.setHealth(updatedHealth);

        Section nextSection = book.getSections().stream()
                .filter(section -> section != null && choice.getGotoId() != null && choice.getGotoId().equals(section.getSectionId()))
                .findFirst()
                .orElse(null);

        if (updatedHealth == 0) {
            game.setStatus(GameStatus.DEAD);
        } else if (nextSection != null && "END".equalsIgnoreCase(nextSection.getType())) {
            game.setStatus(GameStatus.WON);
        } else {
            game.setStatus(GameStatus.PLAYING);
        }

        Game savedGame = gameRepository.save(game);
        return toDto(savedGame, book.getTitle());
    }

    private GameDTO toDto(Game game, String title) {
        GameDTO dto = new GameDTO();
        dto.setId(game.getId());
        dto.setBookId(game.getBookId());
        dto.setBookTitle(title);
        dto.setCurrentSectionId(game.getCurrentSectionId());
        dto.setHealth(game.getHealth());
        dto.setStatus(game.getStatus().name());
        return dto;
    }
}
