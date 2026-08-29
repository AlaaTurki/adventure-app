package com.pictet.adventure.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pictet.adventure.dto.BookDTO;
import com.pictet.adventure.exception.AdventureException;
import com.pictet.adventure.repository.BookRepository;
import com.pictet.adventure.service.BookService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {
    
    private final BookService bookService;
    private final BookRepository bookRepository;

    public DataLoader(BookService bookService, BookRepository bookRepository) {
        this.bookService = bookService;
        this.bookRepository = bookRepository;
    }
    
    private static final String[] BOOK_FILES = {
        "crystal-caverns.json",
        "dragon-quest.json",
        "pirates-jade-sea.json",
        "the-prisoner.json"
    };

    private static final String[] BOOK_SEARCH_ROOTS = { "files", "fichiers" };

    private List<Path> getBookSearchPaths() {
        Path currentDir = Paths.get("").toAbsolutePath().normalize();
        List<Path> candidatePaths = new ArrayList<>();

        for (String root : BOOK_SEARCH_ROOTS) {
            candidatePaths.add(currentDir.resolve(root).resolve("books"));
            candidatePaths.add(currentDir.resolve(root));
            candidatePaths.add(currentDir.resolve("..").resolve(root).resolve("books"));
            candidatePaths.add(currentDir.resolve("..").resolve(root));
            candidatePaths.add(currentDir.resolve("..").resolve("..").resolve(root).resolve("books"));
            candidatePaths.add(currentDir.resolve("..").resolve("..").resolve(root));
        }

        return candidatePaths;
    }

    @Override
    public void run(String... args) throws Exception {
        bookRepository.deleteAll();

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        int loaded = 0;
        List<Path> candidatePaths = getBookSearchPaths();

        for (String filename : BOOK_FILES) {
            boolean fileLoaded = false;
            for (Path base : candidatePaths) {
                Path p = base.resolve(filename);
                if (!Files.exists(p)) {
                    continue;
                }

                try {
                    String content = Files.readString(p);
                    if (content == null || content.isBlank()) {
                        System.err.println("Skipping empty file: " + p);
                        continue;
                    }
                    if (content.startsWith("\uFEFF")) {
                        content = content.substring(1);
                    }
                    BookDTO book = mapper.readValue(content, BookDTO.class);
                    bookService.saveBook(book);
                    System.out.println("LOADED: " + book.getTitle() + " from " + p);
                    loaded++;
                    fileLoaded = true;
                    break;
                } catch (AdventureException e) {
                    System.err.println("Skipping invalid book file " + p + ": " + e.getMessage());
                } catch (Exception e) {
                    System.err.println("Skipping invalid file " + p + ": " + e.getMessage());
                }
            }

            if (!fileLoaded) {
                System.err.println("FAILED to load: " + filename + " from any candidate path");
            }
        }

        System.out.println("\nTotal books loaded: " + loaded + "/" + BOOK_FILES.length);
    }
}
