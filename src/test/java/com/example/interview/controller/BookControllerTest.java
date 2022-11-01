package com.example.interview.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.interview.dto.BookDto;
import com.example.interview.mapper.BookMapper;
import com.example.interview.model.Book;
import com.example.interview.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({BookController.class, BookMapper.class, BookDto.class })
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Test
    void getAll() throws Exception {
        List<Book> books = new ArrayList<>(
                Arrays.asList(new Book("1", "Harry Potter and the Philosopher's Stone",
                                "Joanne Rowling", 50),
                        new Book("2", "The Lord of the Rings: The Fellowship of the Ring",
                                "John Ronald Reuel Tolkien", 10),
                        new Book("3", "Epam handbook", "Epam", 1)));

        when(bookService.getAll(false)).thenReturn(books);

        mockMvc.perform(get("/api/v1/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(books.size()))
                .andDo(print());
    }
}
