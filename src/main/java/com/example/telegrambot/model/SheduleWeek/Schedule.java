package com.example.telegrambot.model.SheduleWeek;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "schedule")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "discipline_id")
    private Discipline discipline;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;


    @ManyToOne
    @JoinColumn(name = "group_id")
    private Groups group;

    public Schedule(Long id, Discipline discipline, Teacher teacher, Groups group) {
        this.id = id;
        this.discipline = discipline;
        this.teacher = teacher;
        this.group = group;
    }

}