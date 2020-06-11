package com.online.library.service;

import com.online.library.domain.ItemCart;
import com.online.library.domain.ShoppingCart;
import com.online.library.domain.User;
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
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemCartServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private ItemCartService itemCartService;

    @MockBean
    private UserDetailsRepo userRepo;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private ShoppingCartRepo shoppingCartRepo;

    @MockBean
    private ItemCartRepo itemCartRepo;

    @Test
    public void create() {
        User user = new User();
        userService.addUser(user);

        ShoppingCart shoppingCart = new ShoppingCart();

        Mockito.doReturn(shoppingCart)
                .when(shoppingCartRepo)
                .findByAuthorOfPost(user);

        itemCartService.create(user, 2L);

        Mockito.verify(itemCartRepo, Mockito.times(1)).save(ArgumentMatchers.any(ItemCart.class));
    }

    @Test
    public void update() {
        ItemCart itemCart = new ItemCart();
        itemCart.setNumberOfCopies(1L);

        itemCartService.update(itemCart, 20L);

        Assert.assertEquals(itemCart.getNumberOfCopies(), 20L, 0.001);
        Mockito.verify(itemCartRepo, Mockito.times(1)).save(ArgumentMatchers.any(ItemCart.class));
    }

    @Test
    public void delete() {
        ItemCart itemCart = new ItemCart();

        itemCartService.delete(itemCart);
        Mockito.verify(itemCartRepo, Mockito.times(1)).delete(ArgumentMatchers.any(ItemCart.class));
    }
}