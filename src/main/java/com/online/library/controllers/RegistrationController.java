package com.online.library.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.online.library.domain.Book;
import com.online.library.domain.User;
import com.online.library.domain.Views;
import com.online.library.dto.CaptchaResponseDto;
import com.online.library.repository.UserDetailsRepo;
import com.online.library.service.UserService;
import com.online.library.util.ControllerUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {
    private static final Logger logger = Logger.getLogger(RegistrationController.class);

    private final static String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";

    @Autowired
    private UserService userService;

    @Value("${recaptcha.secret}")
    private String secret;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(
            @RequestParam("password2") String passwordConfirm,
            @RequestParam("g-recaptcha-response") String captchaResponse,
            @Valid User user,
            BindingResult bindingResult,
            Model model) {
        String url = String.format(CAPTCHA_URL, secret, captchaResponse);
        CaptchaResponseDto response  = restTemplate.postForObject(url, Collections.emptyList(), CaptchaResponseDto.class);

        if (!response.isSuccess()) {
            model.addAttribute("captchaError", "Fill captcha");

            return "registration";
        }

        boolean isConfirmEmpty = StringUtils.isEmpty(passwordConfirm);
        if (isConfirmEmpty) {
            model.addAttribute("password2Error", "Passwords are different!");

            return "registration";
        }

        if (user.getPassword() != null && !user.getPassword().equals(passwordConfirm)) {
            model.addAttribute("passwordError", "Passwords are different!");

            return "registration";
        }

        if (isConfirmEmpty || bindingResult.hasErrors() || !response.isSuccess()) {
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);

            model.mergeAttributes(errors);

            return "registration";
        }

        if (!userService.isOtherEmail(user.getEmail())) {
            model.addAttribute("emailError", "User with such email already exists!");
            return "registration";
        }

        if (!userService.addUser(user)) {
            model.addAttribute("usernameError", "User exists!");
            return "registration";
        }

        return "redirect:/sauth";
    }
}
