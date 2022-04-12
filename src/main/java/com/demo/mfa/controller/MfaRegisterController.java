package com.demo.mfa.controller;

import com.demo.mfa.dto.UserDto;
import com.demo.mfa.exceprion.UserAlreadyExistException;
import com.demo.mfa.model.User;
import com.demo.mfa.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrDataFactory;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.util.Utils;

/**
 * MfaRegisterController
 */
@Controller
public class MfaRegisterController {

    @Autowired
    private UserService userService;

    @Autowired
    private QrDataFactory qrDataFactory;

    @Autowired
    private QrGenerator qrGenerator;

    @GetMapping("/")
    public String register(Model model) {

        model.addAttribute("user", new UserDto());
        return "register";
    }

    @PostMapping("/")
    public String registerUser(final UserDto userDto, final BindingResult bindingResult, final Model model,final RedirectAttributes redirectAttributes) {
        User user;
        try {
            user = userService.registerNewUser(userDto);
        } catch (UserAlreadyExistException uaeEx) {
            model.addAttribute("user", new UserDto());
            model.addAttribute("error", uaeEx.getMessage());
            return "register";
        }

        redirectAttributes.addFlashAttribute("user", user);
        return "redirect:/mfa/qrcode";
    }

    @GetMapping("/mfa/qrcode")
    public String confirmRegister(@ModelAttribute("user") User user, Model model,final RedirectAttributes redirectAttributes) throws QrGenerationException {
        String qrCodeImage = Utils.getDataUriForImage(qrGenerator.generate(getQrData(user)),
                qrGenerator.getImageMimeType());

                
        model.addAttribute("qrCode", qrCodeImage);
        model.addAttribute("code", new String());
        return "qrcode";
    }

    private QrData getQrData(User user) {

        return qrDataFactory.newBuilder()
                .label(user.getEmail())
                .secret(user.getSecret())
                .issuer("2FA_Demo")
                .build();
    }
}