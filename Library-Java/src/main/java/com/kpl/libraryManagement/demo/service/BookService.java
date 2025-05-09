package com.kpl.libraryManagement.demo.service;

import com.kpl.libraryManagement.demo.dto.BookDto;
import com.kpl.libraryManagement.demo.exception.ResourceNotFoundException;
import com.kpl.libraryManagement.demo.model.Book;
import com.kpl.libraryManagement.demo.model.Category;
import com.kpl.libraryManagement.demo.repository.BookRepository;
import com.kpl.libraryManagement.demo.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<BookDto> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public BookDto getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        return convertToDto(book);
    }

    @Transactional
    public BookDto createBook(BookDto bookDto) {
        Category category = null;
        if (bookDto.getDepartmentId() != null) {
            category = categoryRepository.findById(bookDto.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + bookDto.getDepartmentId()));
        }

        Book book = new Book();
        book.setTitle(bookDto.getTitle());
        book.setAuthor(bookDto.getAuthor());
        book.setPublisher(bookDto.getPublisher());
        book.setPublicationYear(bookDto.getPublisherYear());
        book.setCategory(category);

        Book savedBook = bookRepository.save(book);
        return convertToDto(savedBook);
    }

    @Transactional
    public BookDto updateBook(Long id, BookDto bookDto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));

        Category category = null;
        if (bookDto.getDepartmentId() != null) {
            category = categoryRepository.findById(bookDto.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + bookDto.getDepartmentId()));
        }

        book.setTitle(bookDto.getTitle());
        book.setAuthor(bookDto.getAuthor());
        book.setPublisher(bookDto.getPublisher());
        book.setPublicationYear(bookDto.getPublisherYear());
        book.setCategory(category);

        Book updatedBook = bookRepository.save(book);
        return convertToDto(updatedBook);
    }

    @Transactional
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        bookRepository.delete(book);
    }

    public List<BookDto> searchBooks(String query) {
        return bookRepository
                .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(query, query)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private BookDto convertToDto(Book book) {
        BookDto dto = new BookDto();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setPublisher(book.getPublisher());
        dto.setPublisherYear(book.getPublicationYear());

        if (book.getCategory() != null) {
            dto.setDepartmentId(book.getCategory().getId());
            dto.setCategoryName(book.getCategory().getName());
        }

        return dto;
    }

}
