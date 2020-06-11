package com.online.library.service;

import com.online.library.domain.Book;
import com.online.library.domain.User;
import com.online.library.domain.Views;
import com.online.library.dto.EventType;
import com.online.library.dto.BookPageDto;
import com.online.library.dto.MetaDto;
import com.online.library.dto.ObjectType;
import com.online.library.repository.BookRepo;
import com.online.library.util.ServiceUtils;
import com.online.library.util.WsSender;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BookService {
    private static String URL_PATTERN = "https?:\\/\\/?[\\w\\d\\._\\-%\\/\\?=&#]+";
    private static String IMAGE_PATTERN = "\\.(jpeg|jpg|gif|png)$";

    private static Pattern URL_REGEX = Pattern.compile(URL_PATTERN, Pattern.CASE_INSENSITIVE);
    private static Pattern IMG_REGEX = Pattern.compile(IMAGE_PATTERN, Pattern.CASE_INSENSITIVE);

    private final BookRepo bookRepo;
    private final BiConsumer<EventType, Book> wsSender;

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    public BookService(BookRepo bookRepo, WsSender wsSender) {
        this.bookRepo = bookRepo;
        this.wsSender = wsSender.getSender(ObjectType.BOOK, Views.FullMessage.class);
    }


    private void fillMeta(Book book) throws IOException {
        String text = book.getDescription();
        Matcher matcher = URL_REGEX.matcher(text);

        if (matcher.find()) {
            String url = text.substring(matcher.start(), matcher.end());

            matcher = IMG_REGEX.matcher(url);

            book.setLink(url);

            if (matcher.find()) {
                book.setLinkCover(url);
            } else if (!url.contains("youtu")) {
                MetaDto meta = getMeta(url);

                book.setLinkCover(meta.getCover());
                book.setLinkTitle(meta.getTitle());
                book.setLinkDescription(meta.getDescription());
            }
        }
    }

    private MetaDto getMeta(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();

        Elements title = doc.select("meta[name$=title],meta[property$=title]");
        Elements description = doc.select("meta[name$=description],meta[property$=description]");
        Elements cover = doc.select("meta[name$=image],meta[property$=image]");

        return new MetaDto(
                getContent(title.first()),
                getContent(description.first()),
                getContent(cover.first())
        );
    }

    private String getContent(Element element) {
        return element == null ? "" : element.attr("content");
    }

    public void delete(Book book) {
        bookRepo.delete(book);
        wsSender.accept(EventType.REMOVE, book);
    }

    public Book update(Book bookFromDb, Book book) throws IOException {
        copyBook(book, bookFromDb);
        fillMeta(bookFromDb);
        Book updatedBook = bookRepo.save(bookFromDb);

        wsSender.accept(EventType.UPDATE, updatedBook);

        return updatedBook;
    }

    public Book updateFile(Book bookFromDb, MultipartFile file, String type) throws IOException {
        if (file != null) {
            String resultFilename = ServiceUtils.updateFile(file, uploadPath);

            if (type.equals("image")) {
                bookFromDb.setFilename(resultFilename);
            } else {
                bookFromDb.setPdfFilename(resultFilename);
            }

            Book updatedBook = bookRepo.save(bookFromDb);
            wsSender.accept(EventType.UPDATE, updatedBook);

            return updatedBook;
        }

        return bookFromDb;
    }

    public Book create(Book book, User user) throws IOException {
        book.setCreationDate(LocalDateTime.now());
        fillMeta(book);
        book.setAuthorOfPost(user);
        Book updatedBook = bookRepo.save(book);

        wsSender.accept(EventType.CREATE, updatedBook);

        return updatedBook;
    }

    public BookPageDto findAll(Pageable pageable) {
        Page<Book> page = bookRepo.findAll(pageable);
        return new BookPageDto(
                page.getContent(),
                pageable.getPageNumber(),
                page.getTotalPages()
        );
    }

    public List<Book> findAll() {
        return bookRepo.findAll();
    }

    private void copyBook(Book from, Book to) {
        to.setAuthor(from.getAuthor());
        to.setDescription(from.getDescription());
        to.setFilename(from.getFilename());
        to.setNumberOfCopies(from.getNumberOfCopies());
        to.setNumberOfUnoccupied(from.getNumberOfUnoccupied());
        to.setPrice(from.getPrice());
        to.setTitle(from.getTitle());
        to.setYearOfPublishing(from.getYearOfPublishing());
    }

    public BookPageDto findBooksByFilters(String filterAuthor, String filterTitle, Pageable pageable) {
        Page<Book> page;

        if (filterAuthor != null && !filterAuthor.isEmpty()
                && filterTitle != null && !filterTitle.isEmpty()) {
            page =  bookRepo.findByAuthorContainingAndTitleContaining(filterAuthor, filterTitle, pageable);
        } else if (filterAuthor != null && !filterAuthor.isEmpty() ){
            page = bookRepo.findByAuthorContaining(filterAuthor, pageable);
        } else if (filterTitle != null && !filterTitle.isEmpty() ){
            page = bookRepo.findByTitleContaining(filterTitle, pageable);
        } else {
            page = bookRepo.findAll(pageable);
        }


        return new BookPageDto(
                page.getContent(),
                pageable.getPageNumber(),
                page.getTotalPages()
        );
    }
}