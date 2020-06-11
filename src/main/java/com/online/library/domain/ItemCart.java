package com.online.library.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.EqualsAndHashCode;
import org.codehaus.jackson.annotate.JsonBackReference;

import javax.persistence.*;

@Entity
@Table
@EqualsAndHashCode(of = { "id" })
public class ItemCart {
    @Id
    @GeneratedValue
    @JsonView(Views.IdName.class)
    private Long id;

    @JsonView(Views.FullMessage.class)
    private Long idBook;
    @JsonView(Views.FullMessage.class)
    private Long numberOfCopies;

    @ManyToOne
    @JoinColumn(name="shoppingCart_id")
    @JsonBackReference
    @JsonIgnore
    private ShoppingCart shoppingCart;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public Long getIdBook() {
        return idBook;
    }

    public void setIdBook(Long idBook) {
        this.idBook = idBook;
    }

    public Long getNumberOfCopies() {
        return numberOfCopies;
    }

    public void setNumberOfCopies(Long numberOfCopies) {
        this.numberOfCopies = numberOfCopies;
    }
}
