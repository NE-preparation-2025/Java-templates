package com.kpl.libraryManagement.demo.repository;

import com.kpl.libraryManagement.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {//gives you CRUD methods like findAll(),save(),findById(),delete()
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}