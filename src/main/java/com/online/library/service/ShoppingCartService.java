package com.online.library.service;

import com.online.library.domain.*;
import com.online.library.dto.EventType;
import com.online.library.dto.ObjectType;
import com.online.library.repository.BookRepo;
import com.online.library.repository.ItemCartRepo;
import com.online.library.repository.ShoppingCartRepo;
import com.online.library.repository.UserDetailsRepo;
import com.online.library.util.WsSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.BiConsumer;

@Service
public class ShoppingCartService {

    private final ItemCartRepo itemCartRepo;
    private final ShoppingCartRepo shoppingCartRepo;
    private final UserDetailsRepo userRepo;
    private final BiConsumer<EventType, User> wsSenderUser;
    private final BookRepo bookRepo;

    public ShoppingCartService(ItemCartRepo itemCartRepo, ShoppingCartRepo shoppingCartRepo, UserDetailsRepo userRepo, WsSender wsSenderUser, BookRepo bookRepo) {
        this.itemCartRepo = itemCartRepo;
        this.shoppingCartRepo = shoppingCartRepo;
        this.userRepo = userRepo;
        this.wsSenderUser = wsSenderUser.getSender(ObjectType.USER, Views.FullMessage.class);
        this.bookRepo = bookRepo;
    }

    public User buyBooks(User userFromDb) {
        ShoppingCart shoppingCart = shoppingCartRepo.findByAuthorOfPost(userFromDb);

        for (ItemCart item : shoppingCart.getItems()) {
            if (!userFromDb.getIdBooks().contains(item.getIdBook())) {
                userFromDb.getIdBooks().add(item.getIdBook());
            }

            itemCartRepo.delete(item);
        }

        User updatedUser = userRepo.save(userFromDb);
        wsSenderUser.accept(EventType.UPDATE, updatedUser);

        return updatedUser;
    }

    public double findTotalPrice(User userFromDb) {
        double totalPrice = 0.0;

        ShoppingCart shoppingCart = shoppingCartRepo.findByAuthorOfPost(userFromDb);

        for (ItemCart item : shoppingCart.getItems()) {
            totalPrice += bookRepo.findById(item.getIdBook()).get().getPrice() * item.getNumberOfCopies();
        }

        return totalPrice;
    }
}
