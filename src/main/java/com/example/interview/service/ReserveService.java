package com.example.interview.service;

import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.example.interview.dto.ReservationParameter;
import com.example.interview.dto.ReserveRequest;
import com.example.interview.dto.ReserveResponse;
import com.example.interview.dto.ReserveUpdate;
import com.example.interview.exception.BookException;
import com.example.interview.mapper.ReserveMapper;
import com.example.interview.model.Book;
import com.example.interview.model.Reservation;
import com.example.interview.repository.ReserveInMemoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class ReserveService {

    private final ReserveInMemoryRepository repository;

    private final BookService bookService;

    private final ReserveMapper reserveMapper;

    public ReserveResponse reserve(ReserveRequest reserveRequest) {
        Book book = bookService.getById(reserveRequest.getBookId());
        validateBookCopies(reserveRequest.getCopies(), book);
        Reservation reservation =
               Reservation.builder()
                    .username(reserveRequest.getUsername())
                    .reserveNumber(getRandomReserveNumber())
                    .bookId(book.getId())
                    .copies(reserveRequest.getCopies())
                    .createDate(Instant.now())
                    .lastUpdateDate(Instant.now())
                    .build();
        return reserveMapper.toReserveResponse(repository.save(reservation));
    }

    private void validateBookCopies(Integer copies, Book book) {
        if (book.getCopies() < copies) {
            log.error("Not enough copies of book '{}' in database. Available {}", book.getName(), book.getCopies());
            throw new BookException("Not enough copies of book '" + book.getName() + "' in database. Available '" + book.getCopies() + "'");
        }
    }

    private String getRandomReserveNumber() {
        int max = 10000;
        return String.valueOf(new Random().nextInt(max) + 1);
    }

    public List<ReserveResponse> getAll(ReservationParameter reservationParameter) {
        List<Reservation> reservations = repository.findAll();
        Predicate<Reservation> predicate = getCommonPredicate(reservationParameter);
        return getByPredicate(reservations, predicate);
    }

    private List<ReserveResponse> getByPredicate(List<Reservation> reservations, Predicate<Reservation> predicate) {
        return reservations.stream()
                .filter(predicate)
                .map(reserveMapper::toReserveResponse)
                .collect(Collectors.toList());
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

    public ReserveResponse update(String id, ReserveUpdate reserveUpdate) {
        Reservation reservation = repository.findById(id).orElseThrow();
        Book book = bookService.getById(reserveUpdate.getBookId());
        if (book.getCopies() < reserveUpdate.getCopies()) {
            log.error("Not enough copies of book '{}' in database. Available {}", book.getName(), book.getCopies());
            throw new BookException("Not enough copies of book '" + book.getName() +
                    "' in database. Available '" + book.getCopies() + "'");
        }
        reservation.setBookId(reserveUpdate.getBookId());
        reservation.setCopies(reserveUpdate.getCopies());
        reservation.setLastUpdateDate(Instant.now());
        Reservation savedReservation = repository.update(reservation);
        return reserveMapper.toReserveResponse(savedReservation);
    }

}
