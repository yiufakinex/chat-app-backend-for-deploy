package com.franklin.chatapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.franklin.chatapp.annotation.GetUser;
import com.franklin.chatapp.entity.User;

@Controller
public class FrontendController {

    @GetMapping(path = "/")
    public String index() {
        return "index";
    }

    @GetMapping(path = "/login")
    public String login(@GetUser User user) {
        if (user == null) {
            return "login";
        }
        return "index";
    }

    @GetMapping(path = "/new_user")
    public String newUser() {
        return "newUser";
    }

    @GetMapping(path = "/error")
    public String error() {
        return "error";
    }
}
