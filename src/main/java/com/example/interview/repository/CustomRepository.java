package com.example.interview.repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomRepository<T> {

    Flux<T> findAll();

    Mono<T> findById(String id);

}
