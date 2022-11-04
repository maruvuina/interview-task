package com.example.interview.controller;

import com.example.interview.dto.BookDto;
import com.example.interview.mapper.BookMapper;
import com.example.interview.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("api/v1/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    private final BookMapper bookMapper;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public Flux<BookDto> getAll(@RequestParam(defaultValue = "false", required = false) boolean available) {
        return bookService.getAll(available).map(bookMapper::toDto);
    }
}
