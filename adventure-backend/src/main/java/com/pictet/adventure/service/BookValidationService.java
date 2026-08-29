package com.pictet.adventure.service;

import com.pictet.adventure.dto.BookDTO;
import com.pictet.adventure.dto.ChoiceDTO;
import com.pictet.adventure.dto.ConsequenceDTO;
import com.pictet.adventure.dto.SectionDTO;
import com.pictet.adventure.exception.AdventureException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookValidationService {

    public void validateBook(BookDTO book) {
        if (book == null) {
            throw new AdventureException("Book payload cannot be null", HttpStatus.BAD_REQUEST);
        }
        if (book.getTitle() == null || book.getTitle().isBlank()) {
            throw new AdventureException("Book title is required", HttpStatus.BAD_REQUEST);
        }
        if (book.getAuthor() == null || book.getAuthor().isBlank()) {
            throw new AdventureException("Book author is required", HttpStatus.BAD_REQUEST);
        }
        if (book.getDifficulty() == null || book.getDifficulty().isBlank()) {
            throw new AdventureException("Book difficulty is required", HttpStatus.BAD_REQUEST);
        }
        if (book.getSections() == null || book.getSections().isEmpty()) {
            throw new AdventureException("Book must contain at least one section", HttpStatus.BAD_REQUEST);
        }

        Map<Integer, SectionDTO> sectionMap = book.getSections().stream()
                .filter(section -> section != null && section.getId() != null)
                .collect(Collectors.toMap(SectionDTO::getId, section -> section, (a, b) -> a));

        Set<Integer> duplicateSectionIds = new HashSet<>();
        Set<Integer> seenSectionIds = new HashSet<>();
        int beginCount = 0;
        int endCount = 0;

        for (SectionDTO section : book.getSections()) {
            if (section == null) {
                throw new AdventureException("Book contains a null section", HttpStatus.BAD_REQUEST);
            }
            if (section.getId() == null) {
                throw new AdventureException("Section id is required for all sections", HttpStatus.BAD_REQUEST);
            }
            if (!seenSectionIds.add(section.getId())) {
                duplicateSectionIds.add(section.getId());
            }

            String type = section.getType();
            if (type != null && type.equalsIgnoreCase("BEGIN")) {
                beginCount++;
            }
            if (type != null && type.equalsIgnoreCase("END")) {
                endCount++;
            }

            if (!"END".equalsIgnoreCase(type) && (section.getOptions() == null || section.getOptions().isEmpty())) {
                throw new AdventureException("Invalid book: non-END section without options at section " + section.getId(), HttpStatus.BAD_REQUEST);
            }

            if (section.getOptions() != null) {
                for (ChoiceDTO choice : section.getOptions()) {
                    if (choice == null) {
                        throw new AdventureException("Invalid book: null choice in section " + section.getId(), HttpStatus.BAD_REQUEST);
                    }
                    if (choice.getGotoId() == null) {
                        throw new AdventureException("Invalid book: choice destination (gotoId) is required in section " + section.getId(), HttpStatus.BAD_REQUEST);
                    }
                    if (!sectionMap.containsKey(choice.getGotoId())) {
                        throw new AdventureException(
                                "Invalid book: section " + section.getId() + " points to missing section " + choice.getGotoId(),
                                HttpStatus.BAD_REQUEST);
                    }
                }
            }
        }

        if (!duplicateSectionIds.isEmpty()) {
            throw new AdventureException("Invalid book: duplicate section ids found: " + duplicateSectionIds, HttpStatus.BAD_REQUEST);
        }
        if (beginCount == 0) {
            throw new AdventureException("Invalid book: no BEGIN section found", HttpStatus.BAD_REQUEST);
        }
        if (beginCount > 1) {
            throw new AdventureException("Invalid book: multiple BEGIN sections found", HttpStatus.BAD_REQUEST);
        }
        if (endCount == 0) {
            throw new AdventureException("Invalid book: no END section found", HttpStatus.BAD_REQUEST);
        }
    }

    public static int resolveHealthDelta(ConsequenceDTO consequence) {
        if (consequence == null || consequence.getValue() == null || consequence.getValue().isBlank()) {
            return 0;
        }

        String type = consequence.getType() == null ? "" : consequence.getType().trim();
        String raw = consequence.getValue().trim();

        if ("HEALTH".equalsIgnoreCase(type)) {
            return parseSignedValue(raw);
        }
        if ("GAIN_HEALTH".equalsIgnoreCase(type)) {
            return Math.max(0, parseSignedValue(raw));
        }
        if ("LOSE_HEALTH".equalsIgnoreCase(type)) {
            return Math.min(0, -Math.abs(parseSignedValue(raw)));
        }

        if (raw.startsWith("+") || raw.startsWith("-")) {
            return parseSignedValue(raw);
        }

        return 0;
    }

    private static int parseSignedValue(String raw) {
        String normalized = raw.trim();
        if (normalized.startsWith("+") || normalized.startsWith("-")) {
            try {
                return Integer.parseInt(normalized);
            } catch (NumberFormatException ex) {
                return 0;
            }
        }
        try {
            return Integer.parseInt(normalized);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }
}
