package com.example.interview.repository;

import java.util.List;
import java.util.Optional;

import com.example.interview.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookJsonRepositoryTest {

    private BookJsonRepository bookJsonRepository;

    @BeforeEach
    void setUp() {
        bookJsonRepository = new BookJsonRepository();
    }

    @Test
    void findAll() {
        List<Book> bookList = bookJsonRepository.findAll();
        assertEquals(3, bookList.size());
    }

    @Test
    void findById() {
        String id = "1";
        Optional<Book> book = bookJsonRepository.findById(id);
        assertEquals(id, book.get().getId());
    }
}
