package com.example.demo.sec.repo;

import com.example.demo.sec.entities.AppUser;
import com.example.demo.sec.entities.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    Optional<Chat> findByMembersContains(AppUser user);
}
