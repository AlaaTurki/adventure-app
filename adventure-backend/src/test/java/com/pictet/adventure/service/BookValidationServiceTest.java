package com.pictet.adventure.service;

import com.pictet.adventure.dto.BookDTO;
import com.pictet.adventure.dto.ChoiceDTO;
import com.pictet.adventure.dto.ConsequenceDTO;
import com.pictet.adventure.dto.SectionDTO;
import com.pictet.adventure.exception.AdventureException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookValidationServiceTest {

    @Autowired
    private BookValidationService bookValidationService;

    @Test
    void validBookPassesValidation() {
        BookDTO book = new BookDTO();
        book.setTitle("Test Book");
        book.setAuthor("Author");
        book.setDifficulty("EASY");
        book.setSections(List.of(
                new SectionDTO(1, "Start", "BEGIN", List.of(new ChoiceDTO("Go", 2, new ConsequenceDTO("HEALTH", "+5", "You feel refreshed.")))),
                new SectionDTO(2, "Finish", "END", List.of())
        ));

        assertDoesNotThrow(() -> bookValidationService.validateBook(book));
    }

    @Test
    void invalidBookWithoutBeginFails() {
        BookDTO book = new BookDTO();
        book.setTitle("Broken Book");
        book.setAuthor("Author");
        book.setDifficulty("EASY");
        book.setSections(List.of(
                new SectionDTO(1, "Only node", "NODE", List.of(new ChoiceDTO("Go", 2, null))),
                new SectionDTO(2, "Finish", "END", List.of())
        ));

        AdventureException ex = assertThrows(AdventureException.class, () -> bookValidationService.validateBook(book));
        assertTrue(ex.getMessage().contains("BEGIN"));
    }

    @Test
    void invalidBookWithMultipleBeginSectionsFails() {
        BookDTO book = new BookDTO();
        book.setTitle("Broken Book");
        book.setAuthor("Author");
        book.setDifficulty("EASY");
        book.setSections(List.of(
                new SectionDTO(1, "Start A", "BEGIN", List.of(new ChoiceDTO("Go", 3, null))),
                new SectionDTO(2, "Start B", "BEGIN", List.of(new ChoiceDTO("Go", 3, null))),
                new SectionDTO(3, "Finish", "END", List.of())
        ));

        AdventureException ex = assertThrows(AdventureException.class, () -> bookValidationService.validateBook(book));
        assertTrue(ex.getMessage().toLowerCase().contains("begin"));
    }

    @Test
    void invalidBookWithoutEndFails() {
        BookDTO book = new BookDTO();
        book.setTitle("Broken Book");
        book.setAuthor("Author");
        book.setDifficulty("EASY");
        book.setSections(List.of(
                new SectionDTO(1, "Start", "BEGIN", List.of(new ChoiceDTO("Go", 2, null))),
                new SectionDTO(2, "Node", "NODE", List.of(new ChoiceDTO("Go", 1, null)))
        ));

        AdventureException ex = assertThrows(AdventureException.class, () -> bookValidationService.validateBook(book));
        assertTrue(ex.getMessage().toLowerCase().contains("end"));
    }

    @Test
    void invalidBookWithMissingGotoSectionFails() {
        BookDTO book = new BookDTO();
        book.setTitle("Broken Book");
        book.setAuthor("Author");
        book.setDifficulty("EASY");
        book.setSections(List.of(
                new SectionDTO(1, "Start", "BEGIN", List.of(new ChoiceDTO("Broken", 99, null))),
                new SectionDTO(2, "Finish", "END", List.of())
        ));

        AdventureException ex = assertThrows(AdventureException.class, () -> bookValidationService.validateBook(book));
        assertTrue(ex.getMessage().contains("non-existing section") || ex.getMessage().contains("missing section"));
    }

    @Test
    void invalidBookWithNullChoiceFails() {
        BookDTO book = new BookDTO();
        book.setTitle("Broken Book");
        book.setAuthor("Author");
        book.setDifficulty("EASY");
        book.setSections(List.of(
                new SectionDTO(1, "Start", "BEGIN", java.util.Arrays.asList((ChoiceDTO) null)),
                new SectionDTO(2, "Finish", "END", java.util.List.of())
        ));

        AdventureException ex = assertThrows(AdventureException.class, () -> bookValidationService.validateBook(book));
        assertTrue(ex.getMessage().toLowerCase().contains("choice cannot be null"));
    }

    @Test
    void invalidBookWithNullGotoIdFails() {
        BookDTO book = new BookDTO();
        book.setTitle("Broken Book");
        book.setAuthor("Author");
        book.setDifficulty("EASY");
        book.setSections(List.of(
                new SectionDTO(1, "Start", "BEGIN", java.util.List.of(new ChoiceDTO("Go", null, null))),
                new SectionDTO(2, "Finish", "END", java.util.List.of())
        ));

        AdventureException ex = assertThrows(AdventureException.class, () -> bookValidationService.validateBook(book));
        assertTrue(ex.getMessage().toLowerCase().contains("destination"));
    }

    @Test
    void invalidBookWithNonEndSectionWithoutOptionsFails() {
        BookDTO book = new BookDTO();
        book.setTitle("Broken Book");
        book.setAuthor("Author");
        book.setDifficulty("EASY");
        book.setSections(List.of(
                new SectionDTO(1, "Start", "BEGIN", List.of()),
                new SectionDTO(2, "Finish", "END", List.of())
        ));

        AdventureException ex = assertThrows(AdventureException.class, () -> bookValidationService.validateBook(book));
        assertTrue(ex.getMessage().toLowerCase().contains("without options"));
    }
}

