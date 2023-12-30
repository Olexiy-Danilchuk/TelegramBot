package com.example.telegrambot.model.SheduleWeek;

import com.example.telegrambot.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "groupa")
public class Groups {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String groupName;

    @OneToMany(mappedBy = "groups")
    private List<User> user= new ArrayList<>();;


    @OneToMany(mappedBy = "group")
    private List<Schedule> schedules = new ArrayList<>();


    public Groups(Long id, String groupName) {
        this.id = id;
        this.groupName = groupName;
    }

}
