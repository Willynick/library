package com.online.library.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.online.library.domain.Role;
import com.online.library.domain.User;
import com.online.library.domain.Views;
import com.online.library.dto.UserDto;
import com.online.library.service.UserService;
import com.online.library.util.ControllerUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Map;
import java.util.Set;

@RestController
public class UserController {

    private static final Logger logger = Logger.getLogger(UserController.class);

    private final UserService userService;
    private final ControllerUtils controllerUtils;

    @Autowired
    public UserController(UserService userService, ControllerUtils controllerUtils) {
        this.userService = userService;
        this.controllerUtils = controllerUtils;
    }

    @PutMapping("/user/profile")
    @JsonView(Views.FullMessage.class)
    public User update(
            @AuthenticationPrincipal Principal principal,
            @RequestBody UserDto user
    ) {
        User userFromDb = controllerUtils.findUserByPrincipal(principal);

        return userService.updateProfile(userFromDb, user);
    }

    @PutMapping("/user/profile/file")
    @JsonView(Views.FullMessage.class)
    public User updateFile(
            @AuthenticationPrincipal Principal principal,
            @RequestBody MultipartFile file
    ) {
        User userFromDb = controllerUtils.findUserByPrincipal(principal);

        try {
            return userService.updateFile(userFromDb, file);
        } catch (IOException e) {
            logger.error("There are some errors with update file of" + userFromDb + ". Message: " + e.getMessage());
        }

        return userFromDb;
    }

    @PutMapping("user/{id}/makeadmin")
    @JsonView(Views.FullMessage.class)
    public User userSave(
            @AuthenticationPrincipal Principal principal,
            @PathVariable("id") User user
    ) {
        User userFromDb = controllerUtils.findUserByPrincipal(principal);

        if (userFromDb.getRoles().contains(Role.ADMIN)) {
            return userService.saveUser(user);
        } else {
            logger.info("Unauthorized access attempt");
            return user;
        }

    }

    @PutMapping("user/{id}/givesubscription")
    @JsonView(Views.FullMessage.class)
    public User userSaveSubscription(
            @AuthenticationPrincipal Principal principal,
            @PathVariable("id") User user
    ) {
        User userFromDb = controllerUtils.findUserByPrincipal(principal);

        if (userFromDb.getRoles().contains(Role.ADMIN)) {
            return userService.giveSubscription(user);
        } else {
            logger.info("Unauthorized access attempt");
            return user;
        }

    }

    @PutMapping("user/{id}/give_access_to_book")
    @JsonView(Views.FullMessage.class)
    public User userGiveAccessToBook(
            @AuthenticationPrincipal Principal principal,
            @PathVariable("id") User user,
            @RequestBody Set<Long> idBooks
    ) {
        User userFromDb = controllerUtils.findUserByPrincipal(principal);

        if (userFromDb.getRoles().contains(Role.ADMIN)) {
            return userService.giveAccessToBook(user, idBooks);
        } else {
            logger.info("Unauthorized access attempt");
            return user;
        }

    }

}
