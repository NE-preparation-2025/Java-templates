package com.kpl.libraryManagement.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "books")
public class Book {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @NotBlank
        @Size(max = 20)
        private String title;

        @NotBlank
        @Size(max = 50)
        private String author;

        @NotBlank
        @Size(max = 120)
        private String publisher;

        private LocalDate publicationYear;

        @ManyToOne
        @JoinColumn(name = "category_id")
        private Category category;

}
