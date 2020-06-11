package com.online.library.service;

import com.online.library.domain.ItemCart;
import com.online.library.domain.ShoppingCart;
import com.online.library.domain.User;
import com.online.library.repository.ItemCartRepo;
import com.online.library.repository.ShoppingCartRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemCartService {

    private final ItemCartRepo itemCartRepo;
    private final ShoppingCartRepo shoppingCartRepo;

    public ItemCartService(ItemCartRepo itemCartRepo, ShoppingCartRepo shoppingCartRepo) {
        this.itemCartRepo = itemCartRepo;
        this.shoppingCartRepo = shoppingCartRepo;
    }


    public List<ItemCart> findAll() {
        return itemCartRepo.findAll();
    }

    public ItemCart create(User userFromDb, Long idBook) {

        ShoppingCart shoppingCart = shoppingCartRepo.findByAuthorOfPost(userFromDb);

        ItemCart itemCart = new ItemCart();
        itemCart.setShoppingCart(shoppingCart);
        itemCart.setIdBook(idBook);
        itemCart.setNumberOfCopies(1L);

        return itemCartRepo.save(itemCart);
    }

    public ItemCart update(ItemCart itemCart, Long quantity) {
        itemCart.setNumberOfCopies((quantity));

        return itemCartRepo.save(itemCart);
    }

    public void delete(ItemCart itemCart) {
        itemCartRepo.delete(itemCart);
    }
}
