package com.example.interview.mapper;

import com.example.interview.dto.BookDto;
import com.example.interview.model.Book;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookDto toDto(Book book);
}
