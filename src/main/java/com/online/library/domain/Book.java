package com.online.library.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.codehaus.jackson.annotate.JsonManagedReference;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@ToString(of = {"id", "title", "description", "author", "yearOfPublishing", "price", "numberOfCopies", "numberOfUnoccupied"})
@EqualsAndHashCode(of = {"id"})
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(Views.Id.class)
    private Long id;

    @JsonView(Views.FullMessage.class)
    private String title;

    @JsonView(Views.FullMessage.class)
    private  String description;

    @JsonView(Views.FullMessage.class)
    private String author;

    @JsonView(Views.FullMessage.class)
    private int yearOfPublishing;

    @JsonView(Views.FullMessage.class)
    private double price;

    @JsonView(Views.FullMessage.class)
    private int numberOfCopies;

    @JsonView(Views.FullMessage.class)
    private int numberOfUnoccupied;

    @JsonView(Views.FullMessage.class)
    private String filename;

    @JsonView(Views.FullMessage.class)
    private String pdfFilename;

    @Column(updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonView(Views.FullMessage.class)
    private LocalDateTime creationDate;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYearOfPublishing() {
        return yearOfPublishing;
    }

    public void setYearOfPublishing(int yearOfPublishing) {
        this.yearOfPublishing = yearOfPublishing;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getNumberOfCopies() {
        return numberOfCopies;
    }

    public void setNumberOfCopies(int numberOfCopies) {
        this.numberOfCopies = numberOfCopies;
    }

    public int getNumberOfUnoccupied() {
        return numberOfUnoccupied;
    }

    public void setNumberOfUnoccupied(int numberOfUnoccupied) {
        this.numberOfUnoccupied = numberOfUnoccupied;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public User getAuthorOfPost() {
        return authorOfPost;
    }

    public void setAuthorOfPost(User authorOfPost) {
        this.authorOfPost = authorOfPost;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getPdfFilename() {
        return pdfFilename;
    }

    public void setPdfFilename(String pdfFilename) {
        this.pdfFilename = pdfFilename;
    }

    @JsonView(Views.FullMessage.class)
    private String link;
    @JsonView(Views.FullMessage.class)
    private String linkTitle;
    @JsonView(Views.FullMessage.class)
    private String linkDescription;
    @JsonView(Views.FullMessage.class)
    private String linkCover;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLinkTitle() {
        return linkTitle;
    }

    public void setLinkTitle(String linkTitle) {
        this.linkTitle = linkTitle;
    }

    public String getLinkDescription() {
        return linkDescription;
    }

    public void setLinkDescription(String linkDescription) {
        this.linkDescription = linkDescription;
    }

    public String getLinkCover() {
        return linkCover;
    }

    public void setLinkCover(String linkCover) {
        this.linkCover = linkCover;
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonView(Views.FullMessage.class)
    private User authorOfPost;

    @OneToMany(mappedBy = "book", orphanRemoval = true)
    @JsonView(Views.FullMessage.class)
    @JsonManagedReference
    private List<Comment> comments = new ArrayList<>();

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
