package com.example.interview.repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.example.interview.model.Book;
import org.springframework.stereotype.Repository;

@Repository
public class BookInMemoryRepository implements CustomRepository<Book> {

    private List<Book> books = List.of(
            new Book("1", "Harry Potter and the Philosopher's Stone", "Joanne Rowling", 50),
            new Book("2", "The Lord of the Rings: The Fellowship of the Ring", "John Ronald Reuel Tolkien", 10),
            new Book("3", "Epam handbook", "Epam",1)
    );

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
