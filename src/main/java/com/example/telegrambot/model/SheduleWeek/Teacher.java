package com.example.telegrambot.model.SheduleWeek;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Entity
@Setter
@Getter
@NoArgsConstructor
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String nameTeacher;

    @OneToMany(mappedBy = "teacher")
    private List<Schedule> schedules;

    public Teacher(Long id, String nameTeacher) {
        this.id = id;
        this.nameTeacher = nameTeacher;
    }

    public Teacher(String nameTeacher) {
        this.nameTeacher = nameTeacher;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", nameTeacher='" + nameTeacher + '\'' +
                ", lessons=" + schedules +
                '}';
    }
}
