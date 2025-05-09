package com.kpl.libraryManagement.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

        private Long id;

        @NotBlank(message = "Category name is required")
        private String name;

        private String description;

        private int bookCount;

}
