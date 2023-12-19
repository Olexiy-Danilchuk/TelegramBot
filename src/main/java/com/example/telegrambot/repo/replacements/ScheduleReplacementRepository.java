package com.example.telegrambot.repo.replacements;

import com.example.telegrambot.model.replacements.ScheduleReplacement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleReplacementRepository extends JpaRepository<ScheduleReplacement,Long> {
    List<ScheduleReplacement> findBy();
}
