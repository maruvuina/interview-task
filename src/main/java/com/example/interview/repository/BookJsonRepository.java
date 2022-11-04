package com.example.interview.repository;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

import javax.annotation.PostConstruct;

import com.example.interview.exception.JsonIOException;
import com.example.interview.model.Book;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
public class BookJsonRepository implements CustomRepository<Book> {

    private static final String JSON_FILE = "books.json";

    private Flux<Book> books;

    @PostConstruct
    public void init() {
        ObjectMapper mapper = new ObjectMapper();
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(JSON_FILE);
        try {
            List<Book> booksList = mapper.readValue(inputStream, new TypeReference<>(){});
            books = Flux.fromIterable(booksList);
        } catch (IOException e) {
            log.error("Cannot read json file: {}", e.getMessage());
            throw new JsonIOException(e.getMessage());
        }
    }

    @Override
    public Flux<Book> findAll() {
        return books;
    }

    @Override
    public Mono<Book> findById(String id) {
        return books
                .filter(book -> Objects.equals(book.getId(), id))
                .next();
    }
}
