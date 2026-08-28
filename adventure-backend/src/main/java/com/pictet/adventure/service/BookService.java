package com.pictet.adventure.service;

import com.pictet.adventure.dto.BookDTO;
import com.pictet.adventure.dto.SectionDTO;
import com.pictet.adventure.exception.AdventureException;
import com.pictet.adventure.model.Book;
import com.pictet.adventure.model.Section;
import com.pictet.adventure.repository.BookRepository;
import com.pictet.adventure.repository.SectionRepository;
import com.pictet.adventure.util.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {
    
    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private SectionRepository sectionRepository;
    
    @Autowired
    private BookMapper bookMapper;
    
    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public BookDTO getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new AdventureException("Book not found with id: " + id));
        return bookMapper.toDTO(book);
    }
    
    public BookDTO getBookByTitle(String title) {
        Book book = bookRepository.findByTitle(title);
        if (book == null) {
            throw new AdventureException("Book not found with title: " + title);
        }
        return bookMapper.toDTO(book);
    }
    
    public SectionDTO getSection(Long bookId, Integer sectionId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new AdventureException("Book not found with id: " + bookId));

        Section section = book.getSections().stream()
                .filter(s -> s.getSectionId() != null && s.getSectionId().equals(sectionId))
                .findFirst()
                .orElseThrow(() -> new AdventureException("Section not found: " + sectionId + " for book id: " + bookId));

        return bookMapper.sectionToDTO(section);
    }
    
    public BookDTO saveBook(BookDTO bookDTO) {
        Book book = bookMapper.toEntity(bookDTO);
        Book savedBook = bookRepository.save(book);
        return bookMapper.toDTO(savedBook);
    }
}
