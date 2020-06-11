package com.online.library.controllers;

import com.online.library.domain.Role;
import com.online.library.domain.User;
import com.online.library.domain.Views;
import com.online.library.dto.BookPageDto;
import com.online.library.repository.ShoppingCartRepo;
import com.online.library.service.BookService;
import com.online.library.service.UserService;
import com.online.library.util.ControllerUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;

@Controller
@RequestMapping("/")
public class MainController {
    private static final Logger logger = Logger.getLogger(MainController.class);

    private final BookService bookService;
    private final UserService userService;
    private final ShoppingCartRepo shoppingCartRepo;

    private final String profile;
    private final ObjectWriter writer;
    private final ControllerUtils controllerUtils;

    @Autowired
    public MainController(BookService bookService, UserService userService, ShoppingCartRepo shoppingCartRepo, ControllerUtils controllerUtils)
    {
        this.bookService = bookService;
        this.userService = userService;
        this.shoppingCartRepo = shoppingCartRepo;
        this.controllerUtils = controllerUtils;
        this.profile = "dev";
        ObjectMapper mapper = new ObjectMapper();

        this.writer = mapper
                .setSerializationConfig(mapper.getSerializationConfig())
                .writerWithView(Views.FullMessage.class);
    }

    @GetMapping
    public String main(@AuthenticationPrincipal Principal principal, Model model) throws IOException {

        logger.info("Try to load the main page");

        User user = controllerUtils.findUserByPrincipal(principal);

        HashMap<Object, Object> data = new HashMap<>();

        if (user != null) {
            data.put("profile", user);

            Sort sort = Sort.by(Sort.Direction.DESC, "id");
            PageRequest pageRequest = PageRequest.of(0, BookController.BOOKS_PER_PAGE, sort);
            BookPageDto bookPageDto = bookService.findAll(pageRequest);

            String books = writer.writeValueAsString(bookPageDto.getBooks());
            String allBooks = writer.writeValueAsString(bookService.findAll());
            String users = writer.writeValueAsString(userService.findAll());

            model.addAttribute("books", books);
            data.put("currentPage", bookPageDto.getCurrentPage());
            data.put("totalPages", bookPageDto.getTotalPages());
            data.put("shoppingCart", shoppingCartRepo.findByAuthorOfPost(user));
            model.addAttribute("allBooks", allBooks);
            if (user.getRoles().contains(Role.ADMIN)) {
                data.put("isAdmin", true);
                model.addAttribute("users", users);
            } else {
                data.put("isAdmin", false);
                model.addAttribute("users", "[]");
            }
        } else {
            model.addAttribute("books", "[]");
            model.addAttribute("allBooks", "[]");
            model.addAttribute("users", "[]");

            logger.info("User are not authorized");
        }

        model.addAttribute("frontendData", data);
        model.addAttribute("isDevMode", "dev".equals(profile));

        return "index";
    }
}
