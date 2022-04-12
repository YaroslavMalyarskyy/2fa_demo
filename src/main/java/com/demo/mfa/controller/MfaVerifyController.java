package com.demo.mfa.controller;

import com.demo.mfa.dto.UserDto;
import com.demo.mfa.model.User;
import com.demo.mfa.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dev.samstevens.totp.code.CodeVerifier;

@Controller
public class MfaVerifyController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CodeVerifier codeVerifie;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("code", new String());
        model.addAttribute("user", new UserDto());
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String code, final UserDto userDto, Model model) {
        User user = userRepository.findByEmail(userDto.getEmail());

        if (user == null) {
            model.addAttribute("error", "User is not exists");
            model.addAttribute("code", new String());
            model.addAttribute("user", new UserDto());
            return "login";
        }

        if (!user.getPassword().equals(userDto.getPassword())) {
            model.addAttribute("error", "Incorrect password");
            model.addAttribute("code", new String());
            model.addAttribute("user", new UserDto());
            return "login";
        }

        if (!codeVerifie.isValidCode(user.getSecret(), code.replaceAll(" ", ""))) {
            model.addAttribute("error", "Incorrect code");
            model.addAttribute("code", new String());
            model.addAttribute("user", new UserDto());
            return "login";
        }

        return "home";
    }
}