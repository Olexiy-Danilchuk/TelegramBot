package com.example.telegrambot.repo.ScheduleWeek;

import com.example.telegrambot.model.SheduleWeek.Groups;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupsRepository extends JpaRepository<Groups,Long> {
    List<Groups> findBy();
}
