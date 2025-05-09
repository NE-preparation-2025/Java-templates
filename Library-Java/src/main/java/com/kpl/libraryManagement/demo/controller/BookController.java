package com.kpl.libraryManagement.demo.controller;

import com.kpl.libraryManagement.demo.dto.BookDto;
import com.kpl.libraryManagement.demo.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/books")
public class BookController {

        @Autowired
        private BookService bookService;

        @GetMapping
        @PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN')")
        public ResponseEntity<List<BookDto>> getAllBooks() {
            return ResponseEntity.ok(bookService.getAllBooks());
        }

        @GetMapping("/{id}")
        @PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN')")
        public ResponseEntity<BookDto> getStudentById(@PathVariable Long id) {
            return ResponseEntity.ok(bookService.getBookById(id));
        }

        @PostMapping
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<BookDto> createBook(@Valid @RequestBody BookDto bookDto) {
            return new ResponseEntity<>(bookService.createBook(bookDto), HttpStatus.CREATED);
        }

        @PutMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<BookDto> updateBook(@PathVariable Long id, @Valid @RequestBody BookDto bookDto) {
            return ResponseEntity.ok(bookService.updateBook(id, bookDto));
        }

        @DeleteMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
            bookService.deleteBook(id);
            return ResponseEntity.noContent().build();
        }

        @GetMapping("/search")
        @PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN')")
        public ResponseEntity<List<BookDto>> searchBooks(@RequestParam String query) {
            return ResponseEntity.ok(bookService.searchBooks(query));
        }



}
