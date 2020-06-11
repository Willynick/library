package com.online.library.domain;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.codehaus.jackson.annotate.JsonManagedReference;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table
@EqualsAndHashCode(of = { "id" })
@ToString(of = {"id", "authorOfPost"})
public class ShoppingCart {
    @Id
    @GeneratedValue
    @JsonView(Views.Id.class)
    private Long id;

    @OneToMany(mappedBy="shoppingCart")
    @JsonView(Views.FullMessage.class)
    @JsonManagedReference
    private Set<ItemCart> items = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonView(Views.FullMessage.class)
    private User authorOfPost;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getAuthorOfPost() {
        return authorOfPost;
    }

    public void setAuthorOfPost(User authorOfPost) {
        this.authorOfPost = authorOfPost;
    }

    public Set<ItemCart> getItems() {
        return items;
    }

    public void setItems(Set<ItemCart> items) {
        this.items = items;
    }
}
