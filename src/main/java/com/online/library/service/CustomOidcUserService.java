package com.online.library.service;

import com.online.library.domain.Role;
import com.online.library.domain.ShoppingCart;
import com.online.library.domain.User;
import com.online.library.repository.ShoppingCartRepo;
import com.online.library.repository.UserDetailsRepo;
import com.online.library.util.GoogleUserInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

@Service
public class CustomOidcUserService extends OidcUserService {

    private static final Logger logger = Logger.getLogger(CustomOidcUserService.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsRepo userRepository;

    @Autowired
    private ShoppingCartRepo shoppingCartRepo;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);

        try {
            return processOidcUser(userRequest, oidcUser);
        } catch (Exception ex) {
            logger.error("There are some errors with load of user: " +  ex.getMessage());
            return null;
        }
    }

    private OidcUser processOidcUser(OidcUserRequest userRequest, OidcUser oidcUser) {
        GoogleUserInfo googleUserInfo = new GoogleUserInfo(oidcUser.getAttributes());

        // see what other data from userRequest or oidcUser you need

        Optional<User> userOptional = userRepository.findByEmail(googleUserInfo.getEmail());
        if (!userOptional.isPresent()) {
            User user = new User();
            user.setEmail(googleUserInfo.getEmail());
            user.setUsername(googleUserInfo.getName());
            user.setGoogleId(googleUserInfo.getId());
            user.setUserpic(googleUserInfo.getPicture());
            user.setLocale(googleUserInfo.getLocale());
            user.setGender(googleUserInfo.getGender());

            user.setPassword(passwordEncoder.encode("oauth2user"));

            user.setLastVisit(LocalDateTime.now());

            user.setRoles(Collections.singleton(Role.USER));

            userRepository.save(user);

            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setAuthorOfPost(user);
            shoppingCartRepo.save(shoppingCart);
        }

        return oidcUser;
    }
}
