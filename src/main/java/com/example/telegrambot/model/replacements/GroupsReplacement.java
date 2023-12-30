package com.example.telegrambot.model.replacements;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "group_Replacement")
public class GroupsReplacement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String groupNameReplacement;

    @OneToMany(mappedBy = "groupReplacement")
    private List<ScheduleReplacement> schedules = new ArrayList<>();

    public GroupsReplacement(Long id, String groupNameReplacement) {
        this.id = id;
        this.groupNameReplacement = groupNameReplacement;
    }

}
