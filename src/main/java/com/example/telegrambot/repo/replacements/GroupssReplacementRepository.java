package com.example.telegrambot.repo.replacements;

import com.example.telegrambot.model.replacements.GroupssReplacement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupssReplacementRepository extends JpaRepository<GroupssReplacement,Long> {
    List<GroupssReplacement> findBy();
}
