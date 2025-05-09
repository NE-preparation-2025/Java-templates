package com.example.studentmanagement.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private List<String> roles;

}
