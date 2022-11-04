package com.example.interview.repository;

import java.util.Random;
import java.util.UUID;

import com.example.interview.model.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReserveInMemoryRepositoryTest {

    @Mock
    private ReserveInMemoryRepository reserveInMemoryRepository;

    private Reservation reservation;

    private Flux<Reservation> reservationFlux;

    @BeforeEach
    void setUp() {
        reservation = Reservation.builder()
                .id("1")
                .reserveNumber("4444")
                .username("Anna")
                .bookId("3")
                .copies(10)
                .build();
        Reservation reservation1 = Reservation.builder()
                .id("1111")
                .reserveNumber(getRandomReserveNumber())
                .username("Inna")
                .bookId(getRandomId())
                .copies(10)
                .build();
        Reservation reservation2 = Reservation.builder()
                .id("2222")
                .reserveNumber(getRandomReserveNumber())
                .username("Sakura")
                .bookId(getRandomId())
                .copies(14)
                .build();
        reservationFlux = Flux.just(reservation, reservation1, reservation2);
    }

    @Test
    void findAll() {
        // given
        when(reserveInMemoryRepository.findAll()).thenReturn(reservationFlux);

        // when
        Flux<Reservation> actual = reserveInMemoryRepository.findAll();

        // then
        StepVerifier
                .create(actual)
                .expectSubscription()
                .expectNext(reservation)
                .expectNextCount(2)
                .expectComplete()
                .verify();
    }

    @Test
    void save() {
        // given
        Reservation expected = Reservation.builder()
                .id("3333")
                .reserveNumber(getRandomReserveNumber())
                .username("Naruto")
                .bookId(getRandomId())
                .copies(22)
                .build();
        when(reserveInMemoryRepository.save(expected)).thenReturn(Mono.just(expected));

        // when
        Mono<Reservation> actual = reserveInMemoryRepository.save(expected);

        // then
        StepVerifier
                .create(actual)
                .expectSubscription()
                .expectNext(expected)
                .expectComplete()
                .verify();
    }

    @Test
    void findById() {
        // given
        String id = "1";
        when(reserveInMemoryRepository.findById(id)).thenReturn(Mono.just(reservation));

        // when
        Mono<Reservation> actual = reserveInMemoryRepository.findById(id);

        // then
        StepVerifier
                .create(actual)
                .expectSubscription()
                .expectNext(reservation)
                .expectComplete()
                .verify();
    }

    @Test
    void update() {
        // given
        String bookId = "1";
        Integer copies = 20;
        Reservation reservationToUpdate = Reservation.builder()
                .id("1")
                .reserveNumber("4444")
                .username("Anna")
                .bookId(bookId)
                .copies(copies)
                .build();
        when(reserveInMemoryRepository.update(reservationToUpdate)).thenReturn(Mono.just(reservationToUpdate));

        // when
        Mono<Reservation> actual = reserveInMemoryRepository.update(reservationToUpdate);

        // then
        StepVerifier
                .create(actual)
                .expectSubscription()
                .assertNext(expectedReservation -> {
                    assertNotNull(expectedReservation);
                    assertThat(expectedReservation.getBookId(), is(bookId));
                    assertThat(expectedReservation.getCopies(), is(copies));
                })
                .expectComplete()
                .verify();
    }


    private String getRandomId() {
        return UUID.randomUUID().toString();
    }

    private String getRandomReserveNumber() {
        int max = 10000;
        return String.valueOf(new Random().nextInt(max) + 1);
    }
}
