package com.online.library.util;

import com.online.library.controllers.BookController;
import com.online.library.domain.User;
import com.online.library.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.swing.plaf.synth.SynthTreeUI;
import java.security.Principal;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class ControllerUtils {

    private static final Logger logger = Logger.getLogger(ControllerUtils.class);

    private final UserService userService;

    @Autowired
    public ControllerUtils(UserService userService) {
        this.userService = userService;
    }

    public static Map<String, String> getErrors(BindingResult bindingResult) {
        Collector<FieldError, ?, Map<String, String>> collector = Collectors.toMap(
                fieldError -> fieldError.getField() + "Error",
                FieldError::getDefaultMessage
        );
        return bindingResult.getFieldErrors().stream().collect(collector);
    }

    public User findUserByPrincipal(Principal principal)
    {
        User userFromDb = null;

        if (principal != null) {
            userFromDb = (User) userService.loadUserByUsername(principal.getName());
        } else {
            logger.error("User are not find by principal");
        }

        return userFromDb;
    }
}
