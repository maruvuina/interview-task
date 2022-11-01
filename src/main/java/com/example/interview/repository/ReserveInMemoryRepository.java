package com.example.interview.repository;

import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import com.example.interview.model.Reservation;
import lombok.Setter;
import org.springframework.stereotype.Repository;

@Setter
@Repository
public class ReserveInMemoryRepository implements CustomRepository<Reservation> {

    private List<Reservation> reservations = new CopyOnWriteArrayList<>();

    @Override
    public List<Reservation> findAll() {
        return reservations;
    }

    public Reservation save(Reservation reservation) {
        reservation.setId(getRandomId());
        reservations.add(reservation);
        return reservation;
    }

    public Reservation update(Reservation reservation) {
        Reservation savedReservation = null;
        for (final ListIterator<Reservation> iterator = reservations.listIterator(); iterator.hasNext();) {
            Reservation storedReservation = iterator.next();
            if(storedReservation.getUsername().equals(reservation.getUsername()) &&
                    storedReservation.getReserveNumber().equals(reservation.getReserveNumber())) {
                savedReservation = reservations.set(iterator.previousIndex(), reservation);
            }
        }
        return savedReservation;
    }

    private String getRandomId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public Optional<Reservation> findById(String id) {
        return reservations.stream()
                .filter(reservation -> reservation.getId().equals(id))
                .findFirst();
    }
}
