package com.example.interview.service;

import java.time.Instant;
import java.util.Random;
import java.util.function.Predicate;

import com.example.interview.dto.ReservationParameter;
import com.example.interview.dto.ReserveRequest;
import com.example.interview.dto.ReserveResponse;
import com.example.interview.dto.ReserveUpdate;
import com.example.interview.exception.BookException;
import com.example.interview.mapper.ReserveMapper;
import com.example.interview.model.Book;
import com.example.interview.model.Reservation;
import com.example.interview.repository.ReserveReactiveMongoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SynchronousSink;
import reactor.util.function.Tuple2;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReserveService {

    private final ReserveReactiveMongoRepository repository;

    private final BookService bookService;

    private final ReserveMapper reserveMapper;

    public Mono<ReserveResponse> reserve(ReserveRequest reserveRequest) {
        Mono<Book> foundBook = bookService.getById(reserveRequest.getBookId());
        return foundBook.<Book>handle((book, sink) ->
                        validateBookCopies(reserveRequest.getCopies(), book, sink))
                .flatMap(book -> {
                    Reservation reservation =
                            Reservation.builder()
                                    .username(reserveRequest.getUsername())
                                    .reserveNumber(getRandomReserveNumber())
                                    .bookId(book.getId())
                                    .copies(reserveRequest.getCopies())
                                    .createDate(Instant.now())
                                    .lastUpdateDate(Instant.now())
                                    .build();
                    return repository.save(reservation);
                })
                .map(reserveMapper::toReserveResponse);
    }

    private void validateBookCopies(Integer copies, Book book, SynchronousSink<Book> sink) {
        if (book.getCopies() < copies) {
            log.error("Not enough copies of book '{}' in database. Available {}", book.getName(), book.getCopies());
            sink.error(new BookException("Not enough copies of book '" + book.getName() + "' in database. Available '" + book.getCopies() + "'"));
        } else {
            sink.next(book);
        }
    }

    private String getRandomReserveNumber() {
        int max = 10000;
        return String.valueOf(new Random().nextInt(max) + 1);
    }

    public Flux<ReserveResponse> getAll(ReservationParameter reservationParameter) {
        Flux<Reservation> reservations = repository.findAll();
        Predicate<Reservation> predicate = getCommonPredicate(reservationParameter);
        return getByPredicate(reservations, predicate);
    }

    private Flux<ReserveResponse> getByPredicate(Flux<Reservation> reservations, Predicate<Reservation> predicate) {
        return reservations
                .filter(predicate)
                .map(reserveMapper::toReserveResponse);
    }

    private Predicate<Reservation> getCommonPredicate(ReservationParameter reservationParameter) {
        String username = reservationParameter.getUsername();
        String reserveNumber = reservationParameter.getReserveNumber();
        Predicate<Reservation> usernamePredicate = reservation -> reservation.getUsername().equals(username);
        Predicate<Reservation> reserveNumberPredicate = reservation -> reservation.getReserveNumber().equals(reserveNumber);
        Predicate<Reservation> predicate = reservation -> true;
        if (isParameterNotEmpty(username) && isParameterNotEmpty(reserveNumber)) {
            predicate = usernamePredicate.and(reserveNumberPredicate);
        } else if (isParameterNotEmpty(username)) {
            predicate = usernamePredicate;
        } else if (isParameterNotEmpty(reserveNumber)) {
            predicate = reserveNumberPredicate;
        }
        return predicate;
    }

    private boolean isParameterNotEmpty(String parameter) {
        return parameter != null && !parameter.isEmpty();
    }

    public Mono<ReserveResponse> update(String id, ReserveUpdate reserveUpdate) {
        Mono<Reservation> foundReservation = repository.findById(id);
        Mono<Book> foundBook = bookService.getById(reserveUpdate.getBookId());

        return Mono.zip(foundReservation, foundBook)
                .flatMap((Tuple2<Reservation, Book> data) -> {
                    Reservation reservation = data.getT1();
                    Book book = data.getT2();
                    if (book.getCopies() < reserveUpdate.getCopies()) {
                        log.error("Not enough copies of book '{}' in database. Available {}", book.getName(), book.getCopies());
                        return Mono.error(new BookException("Not enough copies of book '" + book.getName() +
                                "' in database. Available '" + book.getCopies() + "'"));
                    }
                    reservation.setBookId(reserveUpdate.getBookId());
                    reservation.setCopies(reserveUpdate.getCopies());
                    reservation.setLastUpdateDate(Instant.now());
                    return repository.save(reservation);
                }).map(reserveMapper::toReserveResponse);
    }

}
