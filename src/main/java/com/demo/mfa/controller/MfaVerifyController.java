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
import org.springframework.web.bind.annotation.ResponseBody;

import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.exceptions.QrGenerationException;

/**
 * MfaVerifyController
 */
@Controller
public class MfaVerifyController {

    private User user;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CodeVerifier codeVerifie;

    @GetMapping("/login")
    public String login(Model model) throws QrGenerationException {
        model.addAttribute("user", new UserDto());
        return "login";
    }

    @PostMapping("/login")
    public String login(final UserDto userDto, Model model) {
        user = userRepository.findByEmail(userDto.getEmail());

        if (user == null) {
            model.addAttribute("error", "User is not exists");
            model.addAttribute("user", new UserDto());
            return "login";
        }

        if (user.getPassword().equals(userDto.getPassword())) {
            model.addAttribute("code", new String());
            return "verify";
        }

        model.addAttribute("error", "Incorrect password");
        model.addAttribute("user", new UserDto());
        return "login";
    }

    @PostMapping("/mfa/verify")
    @ResponseBody
    public String verify(@RequestParam String code) {
        if (codeVerifie.isValidCode(user.getSecret(), code.replaceAll(" ", ""))) {
            return "CORRECT CODE";
        }

        return "INCORRECT CODE";
    }
}