package com.example.telegrambot.repo.ScheduleWeek;

import com.example.telegrambot.model.SheduleWeek.Discipline;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DisciplineRepository extends JpaRepository<Discipline,Long> {
    List<Discipline> findBy();
}
