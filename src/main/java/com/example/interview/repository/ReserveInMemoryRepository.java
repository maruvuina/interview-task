package com.example.interview.repository;

import java.util.List;
import java.util.ListIterator;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import com.example.interview.model.Reservation;
import lombok.Setter;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Setter
@Repository
public class ReserveInMemoryRepository implements CustomRepository<Reservation> {

    private List<Reservation> reservations = new CopyOnWriteArrayList<>();

    @Override
    public Flux<Reservation> findAll() {
        return Flux.fromIterable(reservations);
    }

    public Mono<Reservation> save(Reservation reservation) {
        reservation.setId(getRandomId());
        reservations.add(reservation);
        return Mono.just(reservation);
    }

    public Mono<Reservation> update(Reservation reservation) {
        Reservation savedReservation = null;
        for (final ListIterator<Reservation> iterator = reservations.listIterator(); iterator.hasNext();) {
            Reservation storedReservation = iterator.next();
            if(storedReservation.getUsername().equals(reservation.getUsername()) &&
                    storedReservation.getReserveNumber().equals(reservation.getReserveNumber())) {
                savedReservation = reservations.set(iterator.previousIndex(), reservation);
            }
        }
        return Mono.justOrEmpty(savedReservation);
    }

    private String getRandomId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public Mono<Reservation> findById(String id) {
        return Mono.justOrEmpty(reservations.stream()
                .filter(reservation -> reservation.getId().equals(id))
                .findFirst());
    }
}
