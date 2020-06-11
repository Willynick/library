package com.online.library.repository;

import com.online.library.domain.ShoppingCart;
import com.online.library.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepo extends JpaRepository<ShoppingCart, Long> {
    ShoppingCart findByAuthorOfPost(User authorOfPost);
}
