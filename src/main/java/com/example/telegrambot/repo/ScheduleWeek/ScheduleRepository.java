package com.example.telegrambot.repo.ScheduleWeek;

import com.example.telegrambot.model.SheduleWeek.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findBy();
}
