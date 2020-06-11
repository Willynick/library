package com.online.library.service;

import com.online.library.controllers.MainController;
import com.online.library.domain.Role;
import com.online.library.domain.ShoppingCart;
import com.online.library.domain.User;
import com.online.library.dto.EventType;
import com.online.library.dto.UserDto;
import com.online.library.repository.ShoppingCartRepo;
import com.online.library.repository.UserDetailsRepo;
import org.hamcrest.CoreMatchers;
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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserDetailsRepo userRepo;

    @MockBean
    private ShoppingCartRepo shoppingCartRepo;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private BiConsumer<EventType, User> wsSenderUser;

    @Test
    public void addUser() {
        User user = new User();
        user.setEmail("email@email.com");

        boolean isUserCreated = userService.addUser(user);

        Assert.assertTrue(isUserCreated);
        Assert.assertTrue(CoreMatchers.is(user.getRoles()).matches(Collections.singleton(Role.USER)));

        Mockito.verify(userRepo, Mockito.times(1)).save(user);
        Mockito.verify(shoppingCartRepo, Mockito.times(1)).save(ArgumentMatchers.any(ShoppingCart.class));
    }

    @Test
    public void addUserFailTest() {
        User user = new User();
        user.setUsername("Name");

        Mockito.doReturn(new User())
                .when(userRepo)
                .findByUsername("Name");

        boolean isUserCreated = userService.addUser(user);

        Assert.assertFalse(isUserCreated);

        Mockito.verify(userRepo, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
        Mockito.verify(shoppingCartRepo, Mockito.times(0)).save(ArgumentMatchers.any(ShoppingCart.class));
    }

    @Test
    public void loadUserByUsername() {
        Mockito.doReturn(new User())
                .when(userRepo)
                .findByUsername("Name");

        User userFromDb = (User) userService.loadUserByUsername("Name");

        Assert.assertNotNull(userFromDb);
    }

    @Test
    public void loadUserByUsernameFailTest() {
        User userFromDb = (User) userService.loadUserByUsername("Something");

        Assert.assertNull(userFromDb);
    }

    @Test
    public void updateProfile() {
        User user = new User();
        user.setUsername("Name");
        user.setId(25L);

        userService.addUser(user);

        UserDto newUser = new UserDto("Ivan", "other@mal.ru", "somepassword");

        userService.updateProfile(user, newUser);

        Assert.assertEquals(user.getUsername(), "Ivan");
        Mockito.verify(userRepo, Mockito.times(2)).save(ArgumentMatchers.any(User.class));
    }

    @Test
    public void updateProfileFailTest() {
        Mockito.doReturn(new User())
                .when(userRepo)
                .findByUsername("Ivan");

        User anotherUser = new User();
        anotherUser.setUsername("Kolya");

        UserDto newUser = new UserDto("Ivan", "hi@mail.com", "somepassword");
        newUser.setUsername("Ivan");

        userService.updateProfile(anotherUser, newUser);

        Assert.assertNotEquals(anotherUser.getUsername(), "Ivan");
    }

    @Test
    public void giveSubscription() {
        User user = new User();
        userService.addUser(user);

        userService.giveSubscription(user);
        Assert.assertTrue(user.isHasSubscription());
        Mockito.verify(userRepo, Mockito.times(2)).save(user);
    }

    @Test
    public void giveAccessToBook() {
        User user = new User();
        userService.addUser(user);

        Set<Long> idBooks = new HashSet<>();
        idBooks.add(2L);

        userService.giveAccessToBook(user, idBooks);
        Assert.assertEquals(user.getIdBooks(), idBooks);
        Mockito.verify(userRepo, Mockito.times(2)).save(user);
    }
}