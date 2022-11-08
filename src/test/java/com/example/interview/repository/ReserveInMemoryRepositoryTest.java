package com.example.interview.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import com.example.interview.model.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReserveInMemoryRepositoryTest {

    private ReserveInMemoryRepository reserveInMemoryRepository;

    @BeforeEach
    void setUp() {
        reserveInMemoryRepository = new ReserveInMemoryRepository();
        List<Reservation> reservations = new ArrayList<>();
        Reservation reservation = Reservation.builder()
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
        reservations.add(reservation);
        reservations.add(reservation2);
        reserveInMemoryRepository.setReservations(reservations);
    }

    @Test
    void findAll() {
        assertEquals(2, reserveInMemoryRepository.findAll().size());
    }

    @Test
    void save() {
        // given
        Reservation reservation = Reservation.builder()
                .id("3333")
                .reserveNumber(getRandomReserveNumber())
                .username("Naruto")
                .bookId(getRandomId())
                .copies(22)
                .build();
        // when
        Reservation actual = reserveInMemoryRepository.save(reservation);

        // then
        assertEquals("Naruto", actual.getUsername());
        assertEquals(22, actual.getCopies());
    }

    @Test
    void findById() {
        // given
        String id = "1111";

        // when
        Optional<Reservation> actual = reserveInMemoryRepository.findById(id);

        // then
        assertEquals(id, actual.get().getId());
    }

    private String getRandomId() {
        return UUID.randomUUID().toString();
    }

    private String getRandomReserveNumber() {
        int max = 10000;
        return String.valueOf(new Random().nextInt(max) + 1);
    }
}
