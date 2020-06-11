package com.online.library.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.online.library.domain.Book;
import com.online.library.domain.User;
import com.online.library.domain.Views;
import com.online.library.dto.BookPageDto;
import com.online.library.service.BookService;
import com.online.library.service.UserService;
import com.online.library.util.ControllerUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("book")
public class BookController {
    private static final Logger logger = Logger.getLogger(BookController.class);

    public static final int BOOKS_PER_PAGE = 3;

    private final BookService bookService;
    private final ControllerUtils controllerUtils;

    @Autowired
    public BookController(BookService bookService, ControllerUtils controllerUtils) {
        this.bookService = bookService;
        this.controllerUtils = controllerUtils;
    }

    @GetMapping
    @JsonView(Views.FullMessage.class)
    public BookPageDto list(
            @PageableDefault(size = BOOKS_PER_PAGE, sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) String filterAuthor,
            @RequestParam(required = false) String filterTitle
    ) {
        return bookService.findBooksByFilters(filterAuthor, filterTitle, pageable);
    }

    @GetMapping("{id}")
    @JsonView(Views.FullMessage.class)
    public Book getOne(@PathVariable("id") Book book) {
        return book;
    }

    @PostMapping
    @JsonView(Views.FullMessage.class)
    public Book create(
            @RequestBody Book book,
            @AuthenticationPrincipal Principal principal
    ) {
        User user = controllerUtils.findUserByPrincipal(principal);

        try {
            return bookService.create(book, user);
        } catch (IOException e) {
            logger.error("There are some errors with create book of" + user + ".Book: " + book + ". Message: " + e.getMessage());
        }

        return null;
    }

    @PutMapping("{id}")
    @JsonView(Views.FullMessage.class)
    public Book update(
            @PathVariable("id") Book bookFromDb,
            @RequestBody Book book
    ) {
        try {
            return bookService.update(bookFromDb, book);
        } catch (IOException e) {
            logger.error("There are some errors with update book: " + book + ". Message: " + e.getMessage());
        }

        return null;
    }

    @PutMapping("{id}/file")
    @JsonView(Views.FullMessage.class)
    public Book updateFile(
            @PathVariable("id") Book bookFromDb,
            @RequestBody MultipartFile file
    ) {
        try {
            return bookService.updateFile(bookFromDb, file, "image");
        } catch (IOException e) {
            logger.error("There are some errors with update file in book: " + bookFromDb + "File: " + file + ". Message: " + e.getMessage());
        }

        return null;
    }

    @PutMapping("{id}/pdfFile")
    @JsonView(Views.FullMessage.class)
    public Book updatePdfFile(
            @PathVariable("id") Book bookFromDb,
            @RequestBody MultipartFile file
    ) {
        try {
            return bookService.updateFile(bookFromDb, file, "pdf");
        } catch (IOException e) {
            logger.error("There are some errors with update PDF file in book: " + bookFromDb + "File: " + file + ". Message: " + e.getMessage());
        }

        return null;
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Book book) {
        bookService.delete(book);
    }
}

