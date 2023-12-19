package com.example.telegrambot.repo.replacements;

import com.example.telegrambot.model.replacements.DisciplineReplacement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DisciplineReplacementRepository extends JpaRepository<DisciplineReplacement,Long> {
    List<DisciplineReplacement> findBy();
}
