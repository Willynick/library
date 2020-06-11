package com.online.library.service;

import com.online.library.domain.Book;
import com.online.library.domain.ItemCart;
import com.online.library.domain.ShoppingCart;
import com.online.library.domain.User;
import com.online.library.repository.BookRepo;
import com.online.library.repository.ItemCartRepo;
import com.online.library.repository.ShoppingCartRepo;
import com.online.library.repository.UserDetailsRepo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShoppingCartServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @MockBean
    private UserDetailsRepo userRepo;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private ShoppingCartRepo shoppingCartRepo;

    @MockBean
    private ItemCartRepo itemCartRepo;

    @MockBean
    private BookRepo bookRepo;

    @Test
    public void buyBooks() {
        User user = getUser();

        shoppingCartService.buyBooks(user);

        Assert.assertTrue(user.getIdBooks().contains(2L));
        Mockito.verify(userRepo, Mockito.times(2)).save(user);
        Mockito.verify(itemCartRepo, Mockito.times(1)).delete(ArgumentMatchers.any(ItemCart.class));

    }

    @Test
    public void findTotalPrice() {
        User user = getUser();

        Book book = new Book();
        book.setId(2L);
        book.setPrice(10);

        Mockito.doReturn(Optional.of(book))
                .when(bookRepo)
                .findById(2L);

        double totalPrice = shoppingCartService.findTotalPrice(user);

        Assert.assertEquals(totalPrice, 10, 0.001);
    }

    private User getUser() {
        User user = new User();
        userService.addUser(user);

        ShoppingCart shoppingCart = new ShoppingCart();

        ItemCart itemCart = new ItemCart();
        itemCart.setIdBook(2L);
        itemCart.setNumberOfCopies(1L);
        itemCart.setShoppingCart(shoppingCart);

        Set<ItemCart> items = new HashSet<>();
        items.add(itemCart);
        shoppingCart.setItems(items);

        Mockito.doReturn(shoppingCart)
                .when(shoppingCartRepo)
                .findByAuthorOfPost(user);

        return user;
    }
}