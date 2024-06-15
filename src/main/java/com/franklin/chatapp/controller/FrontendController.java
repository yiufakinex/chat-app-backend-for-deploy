package com.franklin.chatapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.franklin.chatapp.annotation.GetUser;
import com.franklin.chatapp.entity.User;

@Controller
public class FrontendController {

    @GetMapping(path = "/")
    public String index() {
        return "redirect:https://chat-app-frontend-for-deploy-leep.onrender.com";
    }

    @GetMapping(path = "/login")
    public String login(@GetUser User user) {
        if (user == null) {
            return "redirect:https://chat-app-frontend-for-deploy-leep.onrender.com/login";
        }
        return "redirect:https://chat-app-frontend-for-deploy-leep.onrender.com";
    }

    @GetMapping(path = "/new_user")
    public String newUser() {
        return "redirect:https://chat-app-frontend-for-deploy-leep.onrender.com/new_user";
    }
}
