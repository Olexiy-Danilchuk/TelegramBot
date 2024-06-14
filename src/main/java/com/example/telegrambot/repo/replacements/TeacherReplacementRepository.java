package com.example.telegrambot.repo.replacements;

import com.example.telegrambot.model.replacements.TeacherReplacement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeacherReplacementRepository extends JpaRepository<TeacherReplacement, Long> {
    List<TeacherReplacement> findBy();
}
