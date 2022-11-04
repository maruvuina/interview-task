package com.example.interview.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import com.example.interview.model.Book;
import com.example.interview.service.BookService;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private BookService bookService;

    @Test
    void getAll() throws IOException {
        // given
        Flux<Book> books = Flux.just(
                new Book("1", "Harry Potter and the Philosopher's Stone",
                                "Joanne Rowling", 50),
                        new Book("2", "The Lord of the Rings: The Fellowship of the Ring",
                                "John Ronald Reuel Tolkien", 10),
                        new Book("3", "Epam handbook", "Epam", 1));

        when(bookService.getAll(false)).thenReturn(books);

        final var expectedJson = IOUtils
                .toString(Objects.requireNonNull(this.getClass()
                        .getClassLoader()
                        .getResourceAsStream("books.json")), StandardCharsets.UTF_8);

        // when
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/books")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                // then
                .expectStatus()
                .isOk()
                .expectBody()
                .json(expectedJson);
    }
}
