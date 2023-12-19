package com.example.telegrambot.repo.ScheduleWeek;

import com.example.telegrambot.model.SheduleWeek.Groupss;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupssRepository extends JpaRepository<Groupss,Long> {
    List<Groupss> findBy();
}
