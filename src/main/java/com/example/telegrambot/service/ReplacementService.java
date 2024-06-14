package com.example.telegrambot.service;

import com.example.telegrambot.model.replacements.DisciplineReplacement;
import com.example.telegrambot.model.replacements.GroupsReplacement;
import com.example.telegrambot.model.replacements.ScheduleReplacement;
import com.example.telegrambot.model.replacements.TeacherReplacement;
import com.example.telegrambot.repo.replacements.DisciplineReplacementRepository;
import com.example.telegrambot.repo.replacements.GroupsReplacementRepository;
import com.example.telegrambot.repo.replacements.ScheduleReplacementRepository;
import com.example.telegrambot.repo.replacements.TeacherReplacementRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Component
public class ReplacementService {

    private final DisciplineReplacementRepository disciplineReplacementRepository;
    private final GroupsReplacementRepository groupsReplacementRepository;
    private final ScheduleReplacementRepository scheduleReplacementRepository;
    private final TeacherReplacementRepository teacherReplacementRepository;


    @Autowired
    public ReplacementService(DisciplineReplacementRepository disciplineReplacementRepository, GroupsReplacementRepository groupsReplacementRepository,
                              ScheduleReplacementRepository scheduleReplacementRepository, TeacherReplacementRepository teacherReplacementRepository) {
        this.disciplineReplacementRepository = disciplineReplacementRepository;
        this.groupsReplacementRepository = groupsReplacementRepository;
        this.scheduleReplacementRepository = scheduleReplacementRepository;
        this.teacherReplacementRepository = teacherReplacementRepository;

    }

    @Transactional
    public void saveGroupReplacement(CSVReader reader) {
        try {
            List<String[]> allRows = new ArrayList<>();
            String[] row;
            while ((row = reader.readNext()) != null) {
                allRows.add(row);
            }
            int i = 0;
            for (String rows : allRows.get(0)) {
                GroupsReplacement groupsReplacement = new GroupsReplacement(Long.valueOf(i + 1), allRows.get(0)[i]);
                i++;
                groupsReplacementRepository.save(groupsReplacement);
            }
            saveReplacement(allRows);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }

    }

    @Transactional
    public void saveReplacement(List<String[]> allRows) {
        List<GroupsReplacement> groupsReplacements = groupsReplacementRepository.findBy();
        int n = 0;
        for (int i = 0; i < groupsReplacements.size(); i++) {
            readReplacement(allRows, i, groupsReplacements.get(i), n);
            n = n + 5;
        }
    }

    @Transactional
    public void readReplacement(List<String[]> allRows, int groupID, GroupsReplacement groupsReplacements, int n) {
        int i = 1;
        int x = 1 + n;
        int a = 0;
        for (int f = 1; f <= (allRows.size() - 1) / 2; f++) {
            DisciplineReplacement disciplineReplacement = new DisciplineReplacement(Long.valueOf(x), allRows.get(i)[groupID]);

            TeacherReplacement teacherReplacement = new TeacherReplacement(Long.valueOf(x), allRows.get(i + 1)[groupID]);
            ScheduleReplacement scheduleReplacement = new ScheduleReplacement(Long.valueOf(x), disciplineReplacement, teacherReplacement, groupsReplacements);

            teacherReplacementRepository.save(teacherReplacement);
            disciplineReplacementRepository.save(disciplineReplacement);
            scheduleReplacementRepository.save(scheduleReplacement);

            x++;
            i = i + 2;
        }
    }


}
