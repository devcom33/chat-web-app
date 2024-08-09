package com.example.demo.sec.repo;

import com.example.demo.sec.entities.Messages;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessagesRepository extends JpaRepository<Messages, Long> {
}
