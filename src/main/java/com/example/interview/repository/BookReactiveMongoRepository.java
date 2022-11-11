package com.example.interview.repository;

import com.example.interview.model.Book;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface BookReactiveMongoRepository extends ReactiveMongoRepository<Book, String> {

}
