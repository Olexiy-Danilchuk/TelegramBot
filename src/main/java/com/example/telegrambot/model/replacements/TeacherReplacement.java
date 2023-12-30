package com.example.telegrambot.model.replacements;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "teacher_Replacement")
public class TeacherReplacement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String nameTeacherReplacement;

    @OneToMany(mappedBy = "teacherReplacement")
    private List<ScheduleReplacement> schedulesReplacement;

    public TeacherReplacement(Long id, String nameTeacherReplacement) {
        this.id = id;
        this.nameTeacherReplacement = nameTeacherReplacement;
    }

}
