package com.example.telegrambot.repo;

import com.example.telegrambot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {
    List<User> findBy();

    User findByChatId(@Param("chatId")Long chatId);

    boolean existsUserByChatId (Long chatId);

}
