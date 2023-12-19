package com.example.telegrambot.model.replacements;

import com.example.telegrambot.model.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "group_Replacement")
public class GroupssReplacement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String groupNameReplacement;

    @OneToMany(mappedBy = "groupReplacement")
    private List<ScheduleReplacement> schedules = new ArrayList<>();

    public GroupssReplacement(Long id, String groupNameReplacement) {
        this.id = id;
        this.groupNameReplacement = groupNameReplacement;
    }

}
