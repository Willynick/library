package com.online.library.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.online.library.domain.Book;
import com.online.library.domain.Views;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
@JsonView(Views.FullMessage.class)
public class BookPageDto {
    private List<Book> books;
    private int currentPage;
    private int totalPages;

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
