package com.example.demo.repo;

import com.example.demo.entities.AppUser;
import com.example.demo.entities.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    Optional<Chat> findByMembersContains(AppUser user);
}
