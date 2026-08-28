package com.pictet.adventure.service;

import com.pictet.adventure.dto.GameDTO;
import com.pictet.adventure.model.Book;
import com.pictet.adventure.model.Choice;
import com.pictet.adventure.model.Consequence;
import com.pictet.adventure.model.Section;
import com.pictet.adventure.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GameServiceTest {

    @Autowired
    private GameService gameService;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
    }

    @Test
    void gameStartsWithTenHealthAndBeginSection() {
        Book book = createValidBook();
        Book saved = bookRepository.save(book);

        GameDTO game = gameService.startGame(saved.getId());

        assertEquals(10, game.getHealth());
        assertEquals("PLAYING", game.getStatus());
        assertEquals(1, game.getCurrentSectionId());
    }

    @Test
    void choiceAppliesHealthAndMovesToNextSection() {
        Book book = createValidBook();
        Book saved = bookRepository.save(book);

        GameDTO started = gameService.startGame(saved.getId());
        GameDTO updated = gameService.chooseOption(started.getId(), 0);

        assertEquals(15, updated.getHealth());
        assertEquals(2, updated.getCurrentSectionId());
        assertEquals("WON", updated.getStatus());
    }

    @Test
    void healthCannotGoBelowZeroAndGameEnds() {
        Book book = new Book();
        book.setTitle("Danger Book");
        book.setAuthor("Author");
        book.setDifficulty("HARD");

        Section begin = new Section();
        begin.setSectionId(1);
        begin.setText("Start");
        begin.setType("BEGIN");
        begin.setOptions(List.of(new Choice(null, "Take hit", 2, new Consequence(null, "HEALTH", "-15", "Hit!"))));

        Section end = new Section();
        end.setSectionId(2);
        end.setText("Dead end");
        end.setType("END");
        end.setOptions(List.of());

        book.setSections(List.of(begin, end));
        Book saved = bookRepository.save(book);

        GameDTO started = gameService.startGame(saved.getId());
        GameDTO updated = gameService.chooseOption(started.getId(), 0);

        assertEquals(0, updated.getHealth());
        assertEquals("DEAD", updated.getStatus());
    }

    private Book createValidBook() {
        Book book = new Book();
        book.setTitle("Test Book");
        book.setAuthor("Author");
        book.setDifficulty("EASY");

        Section begin = new Section();
        begin.setSectionId(1);
        begin.setText("Start");
        begin.setType("BEGIN");
        begin.setOptions(List.of(new Choice(null, "Go forward", 2, new Consequence(null, "HEALTH", "+5", "You feel refreshed."))));

        Section end = new Section();
        end.setSectionId(2);
        end.setText("Finish");
        end.setType("END");
        end.setOptions(List.of());

        book.setSections(List.of(begin, end));
        return book;
    }
}
