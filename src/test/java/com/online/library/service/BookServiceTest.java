package com.online.library.service;

import com.online.library.domain.Book;
import com.online.library.domain.User;
import com.online.library.repository.BookRepo;
import com.online.library.repository.ShoppingCartRepo;
import com.online.library.repository.UserDetailsRepo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Multipart;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BookServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @MockBean
    private UserDetailsRepo userRepo;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private ShoppingCartRepo shoppingCartRepo;

    @MockBean
    private BookRepo bookRepo;

    @Test
    public void update() throws IOException {
        User user = new User();
        userService.addUser(user);

        Book book = new Book();
        Book otherBook = new Book();
        otherBook.setTitle("SomeTitle");
        otherBook.setYearOfPublishing(2000);
        otherBook.setDescription("something");

        bookService.update(book, otherBook);

        Assert.assertEquals(book.getTitle(), otherBook.getTitle());
        Assert.assertEquals(book.getYearOfPublishing(), otherBook.getYearOfPublishing());
        Mockito.verify(bookRepo, Mockito.times(1)).save(book);
    }

    @Test
    public void updateFile() throws IOException {
        User user = new User();
        userService.addUser(user);

        Book book = new Book();
        MultipartFile file = new MockMultipartFile("name", "name", "name", (byte[]) null);

        bookService.updateFile(book, file, "image");
        Assert.assertNotNull(book.getFilename());
        Mockito.verify(bookRepo, Mockito.times(1)).save(book);
    }

    @Test
    public void create() throws IOException {
        User user = new User();
        userService.addUser(user);

        Book book = new Book();
        book.setDescription("someDescription");

        bookService.create(book, user);

        Assert.assertEquals(book.getAuthorOfPost(), user);
        Mockito.verify(bookRepo, Mockito.times(1)).save(book);
    }

    @Test
    public void delete() {
        User user = new User();
        userService.addUser(user);

        Book book = new Book();

        bookService.delete(book);
        Mockito.verify(bookRepo, Mockito.times(1)).delete(book);
    }
}