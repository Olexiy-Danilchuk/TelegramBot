package com.example.telegrambot.model.SheduleWeek;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "Discipline")
public class Discipline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private  String lecture;


    public Discipline(Long id, String lecture) {
        this.id = id;
        this.lecture = lecture;
    }

}
