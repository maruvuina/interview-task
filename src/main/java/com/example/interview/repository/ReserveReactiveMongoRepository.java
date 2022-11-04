package com.example.interview.repository;

import com.example.interview.model.Reservation;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ReserveReactiveMongoRepository extends ReactiveMongoRepository<Reservation, String> {
}
