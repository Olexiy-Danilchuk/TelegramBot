package com.example.telegrambot.repo.ScheduleWeek;

import com.example.telegrambot.model.SheduleWeek.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    List<Teacher> findBy();
}
