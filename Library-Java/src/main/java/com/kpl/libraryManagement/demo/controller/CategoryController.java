package com.kpl.libraryManagement.demo.controller;

import com.kpl.libraryManagement.demo.dto.CategoryDto;
import com.kpl.libraryManagement.demo.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

        @Autowired
        private CategoryService categoryService;

        @GetMapping
        @PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN')")
        public ResponseEntity<List<CategoryDto>> getAllCategories() {
            return ResponseEntity.ok(categoryService.getAllCategories());
        }

        @GetMapping("/{id}")
        @PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN')")
        public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id) {
            return ResponseEntity.ok(categoryService.getCategoryById(id));
        }

        @GetMapping("/name/{name}")
        @PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN')")
        public ResponseEntity<CategoryDto> getCategoryByName(@PathVariable String name) {
            return ResponseEntity.ok(categoryService.getCategoryByName(name));
        }

        @PostMapping
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
            return new ResponseEntity<>(categoryService.createCategory(categoryDto), HttpStatus.CREATED);
        }

        @PutMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryDto categoryDto) {
            return ResponseEntity.ok(categoryService.updateCategory(id, categoryDto));
        }

        @DeleteMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
            categoryService.deleteCategory(id);
            return ResponseEntity.noContent().build();
        }

}
