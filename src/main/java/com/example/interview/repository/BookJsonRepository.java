package com.example.interview.repository;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.PostConstruct;

import com.example.interview.exception.JsonIOException;
import com.example.interview.model.Book;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class BookJsonRepository implements CustomRepository<Book> {

    private static final String JSON_FILE = "books.json";

    private List<Book> books;

    @PostConstruct
    public void init() {
        ObjectMapper mapper = new ObjectMapper();
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(JSON_FILE);
        try {
            books = mapper.readValue(inputStream, new TypeReference<>(){});
        } catch (IOException e) {
            log.error("Cannot read json file: {}", e.getMessage());
            throw new JsonIOException(e.getMessage());
        }
    }

    @Override
    public List<Book> findAll() {
        return books;
    }

    @Override
    public Optional<Book> findById(String id) {
        return findAll().stream()
                .filter(book -> Objects.equals(book.getId(), id))
                .findAny();
    }
}
