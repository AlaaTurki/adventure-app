package com.pictet.adventure.service;

import com.pictet.adventure.dto.BookDTO;
import com.pictet.adventure.dto.SectionDTO;
import com.pictet.adventure.exception.AdventureException;
import com.pictet.adventure.model.Book;
import com.pictet.adventure.model.Section;
import com.pictet.adventure.repository.BookRepository;
import com.pictet.adventure.repository.SectionRepository;
import com.pictet.adventure.util.BookMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class BookService {
    
    private final BookRepository bookRepository;
    private final SectionRepository sectionRepository;
    private final BookMapper bookMapper;
    private final BookValidationService bookValidationService;

    public BookService(BookRepository bookRepository, SectionRepository sectionRepository, BookMapper bookMapper, BookValidationService bookValidationService) {
        this.bookRepository = bookRepository;
        this.sectionRepository = sectionRepository;
        this.bookMapper = bookMapper;
        this.bookValidationService = bookValidationService;
    }
    
    public List<BookDTO> getAllBooks() {
        return getAllBooks(null, null);
    }

    public List<BookDTO> getAllBooks(String search, String difficulty) {
        List<Book> books = bookRepository.findAll();

        if (search != null && !search.isBlank()) {
            String normalized = search.trim().toLowerCase(Locale.ROOT);
            books = books.stream()
                    .filter(book ->
                            (book.getTitle() != null && book.getTitle().toLowerCase(Locale.ROOT).contains(normalized)) ||
                            (book.getAuthor() != null && book.getAuthor().toLowerCase(Locale.ROOT).contains(normalized)))
                    .collect(Collectors.toList());
        }

        if (difficulty != null && !difficulty.isBlank()) {
            String normalizedDifficulty = difficulty.trim();
            books = books.stream()
                    .filter(book -> normalizedDifficulty.equalsIgnoreCase(book.getDifficulty()))
                    .collect(Collectors.toList());
        }

        return books.stream()
                .map(bookMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public BookDTO getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new AdventureException("Book not found with id: " + id, HttpStatus.NOT_FOUND));
        return bookMapper.toDTO(book);
    }
    
    public BookDTO getBookByTitle(String title) {
        Book book = bookRepository.findByTitle(title);
        if (book == null) {
            throw new AdventureException("Book not found with title: " + title, HttpStatus.NOT_FOUND);
        }
        return bookMapper.toDTO(book);
    }
    
    public SectionDTO getSection(Long bookId, Integer sectionId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new AdventureException("Book not found with id: " + bookId, HttpStatus.NOT_FOUND));

        Section section = book.getSections().stream()
                .filter(s -> s.getSectionId() != null && s.getSectionId().equals(sectionId))
                .findFirst()
                .orElseThrow(() -> new AdventureException("Section not found: " + sectionId + " for book id: " + bookId, HttpStatus.NOT_FOUND));

        return bookMapper.sectionToDTO(section);
    }
    
    public BookDTO saveBook(BookDTO bookDTO) {
        bookValidationService.validateBook(bookDTO);
        Book book = bookMapper.toEntity(bookDTO);
        Book savedBook = bookRepository.save(book);
        return bookMapper.toDTO(savedBook);
    }
}
