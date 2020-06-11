package com.online.library.service;

import com.online.library.controllers.BookController;
import com.online.library.domain.*;
import com.online.library.dto.EventType;
import com.online.library.dto.ObjectType;
import com.online.library.dto.UserDto;
import com.online.library.repository.ItemCartRepo;
import com.online.library.repository.ShoppingCartRepo;
import com.online.library.repository.UserDetailsRepo;
import com.online.library.util.ServiceUtils;
import com.online.library.util.WsSender;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.BiConsumer;

@Service
public class UserService implements UserDetailsService {
    private static final Logger logger = Logger.getLogger(UserService.class);

    private final UserDetailsRepo userRepo;
    private final ShoppingCartRepo shoppingCartRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${upload.path}")
    private String uploadPath;

    private final BiConsumer<EventType, User> wsSender;
    private final BiConsumer<EventType, User> wsSenderUser;

    public UserService(UserDetailsRepo userRepo, ShoppingCartRepo shoppingCartRepo, WsSender wsSender) {
        this.userRepo = userRepo;
        this.shoppingCartRepo = shoppingCartRepo;
        this.wsSender = wsSender.getSender(ObjectType.PROFILE, Views.FullMessage.class);
        this.wsSenderUser = wsSender.getSender(ObjectType.USER, Views.FullMessage.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        if (Character.isDigit(username.charAt(0))) {
            Optional<User> user = userRepo.findByGoogleId(username);

            if (!user.isPresent()) {
                return null;
            } else {
                logger.info("User are not finding by google ID");
            }

            return user.get();
        }
        User user = userRepo.findByUsername(username);

        if (user == null) {
            logger.info("User are not finding by username");
        }
        return user;
    }

    public boolean isOtherEmail(String email) {
        return !userRepo.findByEmail(email).isPresent();
    }

    public boolean isOtherUsername(String username) {
        User userFromDb = userRepo.findByUsername(username);

        if (userFromDb != null) {
            return false;
        }

        return true;
    }

    public boolean addUser(User user) {
        if (isOtherUsername(user.getUsername()) && isOtherEmail(user.getEmail())) {

            user.setActive(true);
            user.setRoles(Collections.singleton(Role.USER));
            user.setActivationCode(UUID.randomUUID().toString());
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            user.setUserpic("user_icon.png");

            user.setLastVisit(LocalDateTime.now());

            userRepo.save(user);

            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setAuthorOfPost(user);
            shoppingCartRepo.save(shoppingCart);


            //sendMessage(user);

            return true;
        }
        return false;
    }

    public User updateProfile(User userFromDb, UserDto user) {
        String userEmail = userFromDb.getEmail();

        String email = user.getEmail();
        String password = user.getPassword();
        String username = user.getUsername();

        boolean isEmailChanged = ((email != null && !email.equals(userEmail))
                || (userEmail != null && !userEmail.equals(email))) && email != "";

        if (isOtherEmail(user.getEmail()) && isEmailChanged) {
            userFromDb.setEmail(email);

            if(!StringUtils.isEmpty(email)) {
                userFromDb.setActivationCode(UUID.randomUUID().toString());
            }
        }

        if(!StringUtils.isEmpty(password) && password != "") {
            userFromDb.setPassword(passwordEncoder.encode(password));
        }

        if(isOtherUsername(user.getUsername()) && !StringUtils.isEmpty(username) && username != "") {
            userFromDb.setUsername(username);
        }

        User updatedUser =  userRepo.save(userFromDb);

        wsSender.accept(EventType.UPDATE, updatedUser);

        return updatedUser;
    }
    public User updateFile(User userFromDb, MultipartFile file) throws IOException {
        if (file != null) {
            String resultFilename = ServiceUtils.updateFile(file, uploadPath);

            userFromDb.setUserpic(resultFilename);

            User updatedUser = userRepo.save(userFromDb);
            wsSender.accept(EventType.UPDATE, updatedUser);

            return updatedUser;
        }

        return userFromDb;
    }

    public User saveUser(User user) {

        if (!user.getRoles().contains(Role.ADMIN)) {
            user.getRoles().add(Role.ADMIN);
        } else {
            user.getRoles().remove(Role.ADMIN);
        }

        User updatedUser = userRepo.save(user);
        wsSenderUser.accept(EventType.UPDATE, updatedUser);

        return updatedUser;
    }

    public List<User> findAll() {
            return userRepo.findAll();
    }

    public User giveSubscription(User user) {
        user.setHasSubscription(!user.isHasSubscription());

        User updatedUser = userRepo.save(user);
        wsSenderUser.accept(EventType.UPDATE, updatedUser);

        return updatedUser;
    }

    public User giveAccessToBook(User user, Set<Long> idBooks) {
        for (Long id : idBooks) {
            if (!user.getIdBooks().contains(id)) {
                user.getIdBooks().add(id);
            } else {
                user.getIdBooks().remove(id);
            }
        }

        User updatedUser = userRepo.save(user);
        wsSenderUser.accept(EventType.UPDATE, updatedUser);

        return updatedUser;
    }
}
