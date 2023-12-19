package com.example.telegrambot.model;

import com.example.telegrambot.model.SheduleWeek.Groupss;
import com.example.telegrambot.model.replacements.GroupssReplacement;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;



@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue
    private Long id;

    private Long chatId;

    @ManyToOne
    @JoinColumn(name = "groupss_id")
    private Groupss groupss;

    private Boolean admin;


    private ReplyKeyboardMarkup keyboardMarkup;

    public User( Long chatId, Boolean admin) {
        this.chatId = chatId;
        this.admin = admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public User(Long chatId, Groupss groupss) {
        this.chatId = chatId;
        this.groupss = groupss;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", group=" + groupss +
                ", admin=" + admin +
                //", notified=" + notified +
                '}';
    }
}
