package com.example.interview.service;

import java.util.function.Predicate;

import com.example.interview.exception.BookException;
import com.example.interview.model.Book;
import com.example.interview.repository.BookJsonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {

    private final BookJsonRepository repository;

    public Flux<Book> getAll(boolean available) {
        Predicate<Book> bookPredicate = available ? book -> book.getCopies() > 0 : book -> true;
        return repository.findAll().filter(bookPredicate);
    }

    public Mono<Book> getById(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(() -> {
                    log.error("Book with id='{}' does not found", id);
                    return new BookException("Book with id='" + id + "' does not found");
                }));
    }
}
