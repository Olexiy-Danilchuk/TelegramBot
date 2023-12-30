package com.example.telegrambot.repo;

import com.example.telegrambot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findBy();

    boolean existsUserByChatId(Long chatId);

}
