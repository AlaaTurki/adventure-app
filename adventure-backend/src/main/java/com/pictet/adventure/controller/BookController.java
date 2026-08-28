package com.pictet.adventure.controller;

import com.pictet.adventure.dto.BookDTO;
import com.pictet.adventure.dto.SectionDTO;
import com.pictet.adventure.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@CrossOrigin(origins = "http://localhost:4200")
public class BookController {
    
    @Autowired
    private BookService bookService;
    
    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String difficulty) {
        return ResponseEntity.ok(bookService.getAllBooks(search, difficulty));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }
    
    @GetMapping("/title/{title}")
    public ResponseEntity<BookDTO> getBookByTitle(@PathVariable String title) {
        return ResponseEntity.ok(bookService.getBookByTitle(title));
    }
    
    @GetMapping("/{bookId}/sections/{sectionId}")
    public ResponseEntity<SectionDTO> getSection(@PathVariable Long bookId, @PathVariable Integer sectionId) {
        return ResponseEntity.ok(bookService.getSection(bookId, sectionId));
    }
    
    @PostMapping
    public ResponseEntity<BookDTO> createBook(@RequestBody BookDTO bookDTO) {
        return ResponseEntity.ok(bookService.saveBook(bookDTO));
    }
}
