package com.example.interview.repository;

import java.util.List;
import java.util.Optional;

public interface CustomRepository<T> {

    List<T> findAll();

    Optional<T> findById(String id);

}
