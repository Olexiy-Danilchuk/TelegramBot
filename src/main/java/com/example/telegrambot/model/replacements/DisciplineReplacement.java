package com.example.telegrambot.model.replacements;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Entity
@Data
@NoArgsConstructor
@Table(name = "discipline_Replacement")
public class DisciplineReplacement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private  String lectureReplacement;

    @OneToMany(mappedBy = "disciplineReplacement")
    private List<ScheduleReplacement> ScheduleReplacement;


    public DisciplineReplacement(Long id, String lectureReplacement) {
        this.id = id;
        this.lectureReplacement = lectureReplacement;
    }

    public DisciplineReplacement(String lectureReplacement) {
        this.lectureReplacement = lectureReplacement;
    }

    @Override
    public String toString() {
        return "Leson{" +
                "id=" + id +
                ", lecture='" + lectureReplacement + '\'' +
                '}';
    }
}
