package com.online.library.service;

import com.online.library.domain.Comment;
import com.online.library.domain.User;
import com.online.library.repository.CommentRepo;
import com.online.library.repository.ShoppingCartRepo;
import com.online.library.repository.UserDetailsRepo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommentServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @MockBean
    private UserDetailsRepo userRepo;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private CommentRepo commentRepo;

    @MockBean
    private ShoppingCartRepo shoppingCartRepo;

    @Test
    public void create() {
        User user = new User();
        userService.addUser(user);
        Comment comment = new Comment();

        commentService.create(comment, user);

        Assert.assertEquals(comment.getAuthor(), user);
        Mockito.verify(commentRepo, Mockito.times(1)).save(comment);
    }
}