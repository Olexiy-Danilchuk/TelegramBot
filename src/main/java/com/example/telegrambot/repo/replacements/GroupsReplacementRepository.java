package com.example.telegrambot.repo.replacements;

import com.example.telegrambot.model.replacements.GroupsReplacement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupsReplacementRepository extends JpaRepository<GroupsReplacement, Long> {
    List<GroupsReplacement> findBy();
}
