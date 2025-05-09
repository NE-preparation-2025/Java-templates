package com.example.bank.repository;

import com.example.bank.model.Customer;
import com.example.bank.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByCustomerOrderByDateTimeDesc(Customer customer);
}
