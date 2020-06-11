package com.online.library.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.online.library.domain.ItemCart;
import com.online.library.domain.User;
import com.online.library.domain.Views;
import com.online.library.service.ItemCartService;
import com.online.library.service.ShoppingCartService;
import com.online.library.util.ControllerUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("shoppingCart")
public class ItemCartController {
    private static final Logger logger = Logger.getLogger(ItemCartController.class);

    private final ControllerUtils controllerUtils;
    private final ItemCartService itemCartService;
    private final ShoppingCartService shoppingCartService;

    @Autowired
    public ItemCartController(ControllerUtils controllerUtils, ItemCartService itemCartService, ShoppingCartService shoppingCartService) {
        this.controllerUtils = controllerUtils;
        this.itemCartService = itemCartService;
        this.shoppingCartService = shoppingCartService;
    }

    @PostMapping
    @JsonView(Views.FullMessage.class)
    public ItemCart create(
            @AuthenticationPrincipal Principal principal,
            @RequestBody Long idBook
    ) {
        User userFromDb = controllerUtils.findUserByPrincipal(principal);

        return itemCartService.create(userFromDb, idBook);
    }

    @PutMapping("{id}")
    @JsonView(Views.FullMessage.class)
    public ItemCart update(
            @PathVariable("id") ItemCart itemCart,
            @RequestBody Long quantity
    ) {
        return itemCartService.update(itemCart, quantity);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") ItemCart itemCart)
    {
        itemCartService.delete(itemCart);
    }

    @PutMapping("buy_books")
    @JsonView(Views.FullMessage.class)
    public User userGiveAccessToBook(
            @AuthenticationPrincipal Principal principal
    ) {
        User userFromDb = controllerUtils.findUserByPrincipal(principal);

         return shoppingCartService.buyBooks(userFromDb);
    }

    @GetMapping("find_total_price")
    @JsonView(Views.FullMessage.class)
    public double findTotalPrice(
            @AuthenticationPrincipal Principal principal
    ) {
        User userFromDb = controllerUtils.findUserByPrincipal(principal);

        return shoppingCartService.findTotalPrice(userFromDb);
    }
}
