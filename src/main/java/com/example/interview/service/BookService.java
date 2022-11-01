package com.example.interview.service;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.example.interview.exception.BookException;
import com.example.interview.model.Book;
import com.example.interview.repository.BookJsonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {

    private final BookJsonRepository repository;

    public List<Book> getAll(boolean available) {
        Predicate<Book> bookPredicate = available ? book -> book.getCopies() > 0 : book -> false;
        return repository.findAll()
                .stream()
                .filter(bookPredicate)
                .collect(Collectors.toList());
    }

    public Book getById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Book with id='{}' does not found", id);
                    return new BookException("Book with id='" + id + "' does not found");
                });
    }
}
