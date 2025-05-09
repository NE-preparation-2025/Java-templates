package com.kpl.libraryManagement.demo.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {

        private Long id;

        @NotBlank(message = "title is required")
        private String title;

        @NotBlank(message = "author is required")
        private String author;

        @NotBlank(message = "publisher is required")
        private String publisher;



        private LocalDate publisherYear;

        private Long departmentId;

        private String categoryName;

}
