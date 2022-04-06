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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    private User user;
    private String qrCodeImage;

    @Autowired
    private UserService userService;

    @Autowired
    private QrDataFactory qrDataFactory;

    @Autowired
    private QrGenerator qrGenerator;

    @Autowired
    private CodeVerifier codeVerifie;

    @GetMapping("/")
    public String register(Model model) throws QrGenerationException {

        model.addAttribute("user", new UserDto());
        return "register";
    }

    @PostMapping("/")
    public String registerUser(final UserDto userDto, final BindingResult bindingResult, final Model model)
            throws QrGenerationException {

        try {
            user = userService.registerNewUser(userDto);
        } catch (UserAlreadyExistException uaeEx) {
            model.addAttribute("user", new UserDto());
            model.addAttribute("error", uaeEx.getMessage());
            return "register";
        }

        return "redirect:/mfa/qrcode";
    }

    @GetMapping("/mfa/qrcode")
    public String confirmRegister(Model model) throws QrGenerationException {

        // Generate the QR code image data as a base64 string which
        // can be used in an <img> tag:
        qrCodeImage = Utils.getDataUriForImage(qrGenerator.generate(getQrData(user)),
                qrGenerator.getImageMimeType());

        model.addAttribute("qrCode", qrCodeImage);
        model.addAttribute("code", new String());
        return "qrcode";
    }

    @PostMapping("/mfa/qrcode")
    public String confirmRegister(@RequestParam String code, final Model model) {

        if (codeVerifie.isValidCode(user.getSecret(), code.replaceAll(" ", ""))) {
            return "redirect:/login";
        }

        model.addAttribute("qrCode", qrCodeImage);
        model.addAttribute("code", new String());
        model.addAttribute("error", "INCORRECT CODE");

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