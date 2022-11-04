package com.example.interview.repository;

import com.example.interview.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookJsonRepositoryTest {

    @Mock
    private BookJsonRepository bookJsonRepository;

    private Book book;

    @BeforeEach
    void setUp() {
        book = new Book("1", "Harry Potter and the Philosopher's Stone", "Joanne Rowling", 50);
    }

    @Test
    void findAll() {
        // given
        Flux<Book> bookList = Flux.just(book,
                new Book("2", "The Lord of the Rings: The Fellowship of the Ring", "John Ronald Reuel Tolkien", 10),
                new Book("3", "Epam handbook", "Epam", 1));
        when(bookJsonRepository.findAll()).thenReturn(bookList);

        // when
        Flux<Book> actual = bookJsonRepository.findAll();

        // then
        StepVerifier
                .create(actual)
                .expectSubscription()
                .expectNext(book)
                .expectNextCount(2)
                .expectComplete()
                .verify();
    }

    @Test
    void findById() {
        // given
        String id = "1";
        Mono<Book> expected = Mono.just(new Book("1",
                "Harry Potter and the Philosopher's Stone", "Joanne Rowling", 50));
        when(bookJsonRepository.findById(id)).thenReturn(expected);

        // when
        Mono<Book> actual = bookJsonRepository.findById(id);

        // then
        StepVerifier
                .create(actual)
                .expectSubscription()
                .expectNext(book)
                .expectComplete()
                .verify();
    }
}
