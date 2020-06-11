package com.online.library.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.online.library.domain.Comment;
import com.online.library.domain.User;
import com.online.library.domain.Views;
import com.online.library.service.CommentService;
import com.online.library.service.UserService;
import com.online.library.util.ControllerUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("comment")
public class CommentController {
    private final CommentService commentService;
    private final ControllerUtils controllerUtils;

    @Autowired
    public CommentController(CommentService commentService, ControllerUtils controllerUtils) {
        this.commentService = commentService;
        this.controllerUtils = controllerUtils;
    }

    @PostMapping
    @JsonView(Views.FullComment.class)
    public Comment create(
            @RequestBody Comment comment,
            @AuthenticationPrincipal Principal principal
    ) {
        User user = controllerUtils.findUserByPrincipal(principal);

        return commentService.create(comment, user);
    }
}
