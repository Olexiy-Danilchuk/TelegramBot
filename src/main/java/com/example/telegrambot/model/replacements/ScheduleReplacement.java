package com.example.telegrambot.model.replacements;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;



@Entity
@Data
@NoArgsConstructor
@Table(name = "schedule_Replacement")
public class ScheduleReplacement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "discipline_Replacement_id")
    private DisciplineReplacement disciplineReplacement;

    @ManyToOne
    @JoinColumn(name = "teacher_Replacement_id")
    private TeacherReplacement teacherReplacement;

    @ManyToOne
    @JoinColumn(name = "group_Replacement_id")
    private GroupssReplacement groupReplacement;

    public ScheduleReplacement(Long id, DisciplineReplacement disciplineReplacement, TeacherReplacement teacherReplacement) {
        this.id = id;
        this.disciplineReplacement = disciplineReplacement;
        this.teacherReplacement = teacherReplacement;
    }

    public ScheduleReplacement(Long id, DisciplineReplacement disciplineReplacement, TeacherReplacement teacherReplacement, GroupssReplacement groupReplacement) {
        this.id = id;
        this.disciplineReplacement = disciplineReplacement;
        this.teacherReplacement = teacherReplacement;
        this.groupReplacement = groupReplacement;
    }


    @Override
    public String toString() {
        return "Lesson{" +
                "id=" + id +
                ", discipline=" + disciplineReplacement +
                ", teacher=" + teacherReplacement +
                '}';
    }
}