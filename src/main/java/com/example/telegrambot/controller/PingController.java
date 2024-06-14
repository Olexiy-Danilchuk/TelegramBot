package com.example.telegrambot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class PingController {

    @GetMapping("/ping")
    public String pingGet() {
        log.info("Life get");
        return "ok get";
    }

    @RequestMapping(method = RequestMethod.HEAD, path = "/ping")
    public String pingHead() {
        log.info("Life head");
        return "ok head";
    }
}
