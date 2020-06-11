package com.online.library.repository;

import com.online.library.domain.ItemCart;
import com.online.library.domain.ShoppingCart;
import com.online.library.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemCartRepo extends JpaRepository<ItemCart, Long> {
    List<ItemCart> findAll();
}
