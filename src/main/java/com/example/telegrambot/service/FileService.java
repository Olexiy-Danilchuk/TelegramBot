package com.example.telegrambot.service;

import com.example.telegrambot.model.SheduleWeek.Discipline;
import com.example.telegrambot.model.SheduleWeek.Groups;
import com.example.telegrambot.model.SheduleWeek.Schedule;
import com.example.telegrambot.model.SheduleWeek.Teacher;
import com.example.telegrambot.repo.ScheduleWeek.DisciplineRepository;
import com.example.telegrambot.repo.ScheduleWeek.GroupsRepository;
import com.example.telegrambot.repo.ScheduleWeek.ScheduleRepository;
import com.example.telegrambot.repo.ScheduleWeek.TeacherRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


@Service
@Component
public class FileService {
    private final GroupsRepository groupsRepository;
    private final TeacherRepository teacherRepository;
    private final ScheduleRepository scheduleRepository;
    private final ReplacementService replacementService;
    private final DisciplineRepository disciplineRepository;

    @Autowired
    public FileService(GroupsRepository groupsRepository, TeacherRepository teacherRepository, ScheduleRepository scheduleRepository,
                       ReplacementService replacementService, DisciplineRepository disciplineRepository) {
        this.groupsRepository = groupsRepository;
        this.teacherRepository = teacherRepository;
        this.scheduleRepository = scheduleRepository;
        this.replacementService = replacementService;
        this.disciplineRepository = disciplineRepository;

    }

    public void readFile() {

        String fileName = "schedule2.csv";
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        try (CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {
            saveNameGroup(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void saveNameGroup(CSVReader reader) {
        try {
            List<String[]> allRows = new ArrayList<>();
            String[] row;
            while ((row = reader.readNext()) != null) {
                allRows.add(row);
            }
            int i = 0;
            for (String rows : allRows.get(0)) {
                Groups group = new Groups(Long.valueOf(i + 1), allRows.get(0)[i]);
                i++;
                groupsRepository.save(group);
            }
            saveLectures(allRows);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }

    }

    @Transactional
    public void saveLectures(List<String[]> allRows) {
        List<Groups> groups = groupsRepository.findBy();
        int n = 0;
        for (int i = 0; i < groups.size(); i++) {
            readGroups(allRows, i, groups.get(i), n);
            n = n + 25;
        }
    }

    @Transactional
    public void readGroups(List<String[]> allRows, int groupID, Groups groups, int n) {
        int i = 1;
        int x = 1 + n;
        for (int f = 1; f <= (allRows.size() - 1) / 2; f++) {
            Discipline discipline = new Discipline(Long.valueOf(x), allRows.get(i)[groupID]);
            Teacher teacher = new Teacher(Long.valueOf(x), allRows.get(i + 1)[groupID]);
            Schedule schedule = new Schedule(Long.valueOf(x), discipline, teacher, groups);


            teacherRepository.save(teacher);
            disciplineRepository.save(discipline);
            scheduleRepository.save(schedule);
            x++;
            i = i + 2;
        }

    }

    public void analiseFileReplacement() {

        String fileName = "replacement.csv";
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        try (CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {
            replacementService.saveGroupReplacement(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
