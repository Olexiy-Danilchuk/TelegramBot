package com.example.telegrambot.service;

import com.example.telegrambot.config.BotConfig;
import com.example.telegrambot.model.*;
import com.example.telegrambot.model.SheduleWeek.Discipline;
import com.example.telegrambot.model.SheduleWeek.Groupss;
import com.example.telegrambot.model.SheduleWeek.Schedule;
import com.example.telegrambot.model.SheduleWeek.Teacher;
import com.example.telegrambot.model.replacements.DisciplineReplacement;
import com.example.telegrambot.model.replacements.GroupssReplacement;
import com.example.telegrambot.model.replacements.ScheduleReplacement;
import com.example.telegrambot.model.replacements.TeacherReplacement;
import com.example.telegrambot.repo.*;
import com.example.telegrambot.repo.ScheduleWeek.DisciplineRepository;
import com.example.telegrambot.repo.ScheduleWeek.GroupssRepository;
import com.example.telegrambot.repo.ScheduleWeek.ScheduleRepository;
import com.example.telegrambot.repo.ScheduleWeek.TeacherRepository;
import com.example.telegrambot.repo.replacements.DisciplineReplacementRepository;
import com.example.telegrambot.repo.replacements.GroupssReplacementRepository;
import com.example.telegrambot.repo.replacements.ScheduleReplacementRepository;
import com.example.telegrambot.repo.replacements.TeacherReplacementRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class TelegramBot  extends TelegramLongPollingBot {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private GroupssRepository groupssRepository;

    @Autowired
    private DisciplineRepository disciplineRepository;

    @Autowired
    private DisciplineReplacementRepository disciplineReplacementRepository;

    @Autowired
    private GroupssReplacementRepository groupssReplacementRepository;

    @Autowired
    private ScheduleReplacementRepository scheduleReplacementRepository;

    @Autowired
    private TeacherReplacementRepository teacherReplacementRepository;


    final BotConfig config;


    public TelegramBot(BotConfig config) {
        this.config = config;
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "Розпочати роботу з ботом"));
        listOfCommands.add(new BotCommand("/hellp", "Допомога"));
        listOfCommands.add(new BotCommand("/analisFile", "Оновити розклад занять на тиждень"));
        listOfCommands.add(new BotCommand("/mygroup", "Виберіть групу заміни якої ви хочете відслідковувати  "));
        listOfCommands.add(new BotCommand("/mySchebule", "Виберіть групу розклад якої ви хочете побачити (спочатку треба вибрати групу /mygroup)"));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error seting bot's command list: " + e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String massageText = update.getMessage().getText();

            Long chatId = update.getMessage().getChatId();
                switch (massageText) {
                    case "/start":
                        startCommandReceived(chatId);
                        butonGroup(chatId);
                        sendMessage(chatId," Для перегляду розкладу на тиждень /mySchebule " +
                                                    " Для перегляду замін /myReplacement ");
                        break;
                    case "/mygroup":
                        butonGroup(chatId);
                        break;
                    case "/analisFile":
                        if(isUserAdmin(chatId) == true){
                            readFile();
                            sendMessageAll("Розклад оновлено!");
                        }else {
                            sendMessage(chatId,"Ви не являєтесь адміном");
                        }
                        break;
                    case "/hellp":
                        sendMessage(chatId, " Для початку роботи введіть /start " +
                                " Для аналізу предметів  /analisFile " +
                                " Для анадізу замін /analisReplacement " +
                                " Для зміни групи введіть /mygroup " +
                                " Для створення адміна /makeAdmin " +
                                " Для перегляду розкладу на тиждень /mySchebule " +
                                " Для перегляду замін /myReplacement ");
                        break;
                    case "/mySchebule": {
                        scheduleForUser(chatId);
                        break;
                    }
                    case "/analisReplacement": {
                        if(isUserAdmin(chatId) == true){
                            analisFileReplacement();
                            sendMessageAll("Заміни оновлено!");
                        }else {
                            sendMessage(chatId,"Ви не являєтесь адміном");
                        }

                        break;
                    }
                    case "/myReplacement": {
                        replacementForUser(chatId);
                        break;
                    }
                    case "/makeAdmin":
                        if(isUserAdmin(chatId)== true){
                            makeUserAdmin(chatId);
                            sendMessageAll("Заміни оновлено!");
                        }else {
                            sendMessage(chatId,"Ви не являєтесь адміном");
                        }
                        break;
            }
        } else if (update.hasCallbackQuery()  ) {
            String callbacData = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            List<Groupss> groupss = groupssRepository.findBy();


            for(Groupss group : groupss){
                if(callbacData.equals("NAME_GROUP_BUTON_"+group.getGroupName())){
                    EditMessageText message = new EditMessageText();
                    message.setChatId(String.valueOf(chatId));
                    message.setMessageId((int) messageId);
                    List<User> users = userRepository.findBy();


                    message.setText("Ви вибрали групу групу : " + group.getGroupName());

                    if (userRepository.existsUserByChatId(chatId)) {
                        for (User user : users) {
                            if (user.getChatId().equals(chatId)) {
                                user.setGroupss(group);
                                userRepository.save(user);
                            }
                        }
                    } else {
                        User user = new User(chatId, group);
                        userRepository.save(user);
                    }
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        log.error("Error occurred: " + e.getMessage());
                    }
                }
            }
        }
    }
    private boolean isUserAdmin(Long chatId) {

        List<User> users = userRepository.findAll();
        for (User user : users){
            if (user.getAdmin() == true & user.getChatId().equals(chatId)){
                return true;
            }
        }
        return false;
    }


    private void makeUserAdmin(Long chatId) {
        Optional<User> optionalUser = userRepository.findById(chatId);
        optionalUser.ifPresent(user -> {
            user.setAdmin(true);
            userRepository.save(user);
            sendMessage(chatId, "Ви успішно призначені адміністратором.");
        });
    }


    private void scheduleForUser (Long chatId){
        List<User> users = userRepository.findBy();
        List<Schedule> schedules = scheduleRepository.findBy();

        String[] nameDay = new String[]{"Понеділок: ", "Вівторок: ", "Середа: ", "Четверг: ", "П`ятниця: "};

        int dayName =0;
        int numberLeson =1;
        int day =0;

        for (User user : users){
            if (user.getGroupss().getGroupName() != null && user.getChatId().equals(chatId)){
                for (Schedule schedule : schedules){
                    if( schedule.getGroup().getGroupName().equals(user.getGroupss().getGroupName())){
                        if (day  % 5 ==0 ){
                            sendMessage(chatId, nameDay[dayName++]);
                        }if(numberLeson>5 ){
                            numberLeson =1;
                        }
                        if(!schedule.getDiscipline().getLecture().equals("") && !schedule.getTeacher().getNameTeacher().equals("")){
                            String text = numberLeson  + " -- " + schedule.getDiscipline().getLecture() + " -- " + schedule.getTeacher().getNameTeacher();
                            sendMessage(chatId,  text);
                        }
                        day++;
                        numberLeson++;
                    }
                }
            }
        }

    }

    private void butonGroup (long chatId){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Виберіть групу : ");

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<Groupss> groupss = groupssRepository.findBy();

        List<String> names =new ArrayList<>() ;
        for (Groupss group : groupss){
            String name = group.getGroupName();
            names.add(name);
        }

        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        int buttonsInRow = 5;
        for (int i = 0; i < names.size(); i++){
            var nameGroupButton = new InlineKeyboardButton();
            nameGroupButton.setText(names.get(i));
            nameGroupButton.setCallbackData("NAME_GROUP_BUTON_"+names.get(i));
            rowInLine.add(nameGroupButton);

            if ((i + 1) % buttonsInRow == 0 || (i + 1) == names.size()) {
                rowsInLine.add(rowInLine);
                rowInLine = new ArrayList<>();
            }
        }

        markup.setKeyboard(rowsInLine);
        message.setReplyMarkup(markup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }

    }

    private void startCommandReceived(Long chatId) {
        String answer = "Доброго дня! Для перегляду команд введіть /hellp";
        var users = userRepository.findAll();
        if (users.size() == 0) {
            User user = new User(chatId, true);
            userRepository.save(user);
        }
        sendMessage(chatId, answer);
    }

    private void sendMessage(Long chatid, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatid));
        message.setText(textToSend);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }

    public void readFile() {
        String csvFilePath = "classpath:/static/Schedule.csv";
        //Path absolutePath = Paths.get(relativePath).toAbsolutePath();
        String filePath = "TelegramBot/src/main/resources/static/Schedule.csv";

        //String csvFilePath = String.valueOf(absolutePath);
        //String csvFilePath = "\\Schedule.csv";
        try (CSVReader reader = new CSVReader(new FileReader(csvFilePath))) {
            saveNameGroup(reader);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Transactional
    public void sendMessageAll(String text){
        var users  = userRepository.findAll();
        for (User user : users){
            sendMessage(user.getChatId(), text);
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
                Groupss group = new Groupss(Long.valueOf(i + 1), allRows.get(0)[i]);
                i++;
                groupssRepository.save(group);
            }
            saveLectures( allRows);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }

    }

    @Transactional
    public void saveLectures( List<String[]> allRows) {
        List<Groupss> groupss = groupssRepository.findBy();
        int n = 0;
        for (int i = 0; i < groupss.size(); i++) {
            readGroups(allRows, i, groupss.get(i), n);
            n = n + 25;
        }
    }

    @Transactional
    public void readGroups(List<String[]> allRows, int groupID, Groupss groups, int n) {
        int i = 1;
        int x = 1 + n;
        for (int f = 1; f <= (allRows.size() - 1) / 2; f++) {
            Discipline discipline = new Discipline(Long.valueOf(x), allRows.get(i)[groupID]);
            Teacher teacher = new Teacher(Long.valueOf(x), allRows.get(i + 1)[groupID]);
            Schedule schedule = new Schedule(Long.valueOf(x), discipline, teacher,  groups);

            teacherRepository.save(teacher);
            disciplineRepository.save(discipline);
            scheduleRepository.save(schedule);
            x++;
            i = i + 2;
        }

    }

    public void analisFileReplacement() {
        String csvFilePath = "A:\\replacement.csv";

        try (CSVReader reader = new CSVReader(new FileReader(csvFilePath))) {
            saveGroupReplacement(reader);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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
                GroupssReplacement groupssReplacement = new GroupssReplacement(Long.valueOf(i + 1), allRows.get(0)[i]);
                i++;
                groupssReplacementRepository.save(groupssReplacement);
            }
            saveReplacemt( allRows);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }

    }
    @Transactional
    public void saveReplacemt( List<String[]> allRows) {
        List<GroupssReplacement> groupssReplacements = groupssReplacementRepository.findBy();
        int n = 0;
        for (int i = 0; i < groupssReplacements.size(); i++) {
            readReplacement(allRows, i, groupssReplacements.get(i), n);
            n = n + 5;
        }
    }
    @Transactional
    public void readReplacement(List<String[]> allRows, int groupID, GroupssReplacement groupssReplacements, int n) {
        int i = 1;
        int x = 1 + n;
        int a = 0;
        for (int f = 1; f <= (allRows.size() - 1) / 2; f++) {
            DisciplineReplacement disciplineReplacement = new DisciplineReplacement(Long.valueOf(x), allRows.get(i)[groupID]);

            TeacherReplacement teacherReplacement = new TeacherReplacement(Long.valueOf(x), allRows.get(i + 1)[groupID]);
            ScheduleReplacement scheduleReplacement = new ScheduleReplacement(Long.valueOf(x), disciplineReplacement, teacherReplacement,  groupssReplacements);

            teacherReplacementRepository.save(teacherReplacement);
            disciplineReplacementRepository.save(disciplineReplacement);
            scheduleReplacementRepository.save(scheduleReplacement);

            x++;
            i = i + 2;
        }
    }
    private void replacementForUser (Long chatId){
        List<User> users = userRepository.findBy();
        List<ScheduleReplacement> schedulesReplacement = scheduleReplacementRepository.findBy();
        List<GroupssReplacement> groupssReplacements = groupssReplacementRepository.findBy();

        int numberLeson =1;

        for (User user : users){
            if (user.getGroupss().getGroupName() != null && user.getChatId().equals(chatId)){
                for (GroupssReplacement groupssReplacement : groupssReplacements){
                    if(user.getGroupss().getGroupName().equals(groupssReplacement.getGroupNameReplacement())){
                        for (ScheduleReplacement schedule : schedulesReplacement){
                            if( schedule.getGroupReplacement().getGroupNameReplacement().equals(user.getGroupss().getGroupName())){
                                if(!schedule.getDisciplineReplacement().getLectureReplacement().equals("") && !schedule.getTeacherReplacement().getNameTeacherReplacement().equals("")){
                                    String text = numberLeson  + " -- " + schedule.getDisciplineReplacement().getLectureReplacement() + " -- " + schedule.getTeacherReplacement().getNameTeacherReplacement();
                                    sendMessage(chatId,  text);
                                }
                                numberLeson++;
                            }
                        }
                    }
                }
            }
        }
    }

}