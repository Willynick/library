package com.online.library.repository;

import com.online.library.domain.Book;
import com.online.library.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepo extends JpaRepository<Book, Long> {
    Page<Book> findAll(Pageable pageable);

    List<Book> findAll();

    Optional<Book> findById(Long id);

    Page<Book> findByAuthorContaining(String author, Pageable pageable);

    Page<Book> findByTitleContaining(String title, Pageable pageable);

    Page<Book> findByAuthorContainingAndTitleContaining(String author, String title, Pageable pageable);

    Page<Book> findByAuthorOfPost(User author, Pageable pageable);
}

