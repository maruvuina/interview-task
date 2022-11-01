package com.example.interview.repository;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    @Override
    public List<Book> findAll() {
        return readFromJson();
    }

    private List<Book> readFromJson() {
        ObjectMapper mapper = new ObjectMapper();
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(JSON_FILE);
        try {
            return mapper.readValue(inputStream, new TypeReference<>(){});
        } catch (IOException e) {
            log.error("Cannot read json file: {}", e.getMessage());
            throw new JsonIOException(e.getMessage());
        }
    }

    @Override
    public Optional<Book> findById(String id) {
        return findAll().stream()
                .filter(book -> Objects.equals(book.getId(), id))
                .findAny();
    }
}
