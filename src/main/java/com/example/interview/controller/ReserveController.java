package com.example.interview.controller;

import com.example.interview.dto.ReservationParameter;
import com.example.interview.dto.ReserveRequest;
import com.example.interview.dto.ReserveResponse;
import com.example.interview.dto.ReserveUpdate;
import com.example.interview.service.ReserveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;


@RestController
@RequestMapping("api/v1/reserves")
@RequiredArgsConstructor
public class ReserveController {

    private final ReserveService reserveService;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Mono<ReserveResponse> reserve(@RequestBody @Valid ReserveRequest reserveRequest) {
        return reserveService.reserve(reserveRequest);
    }

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public Flux<ReserveResponse> getAll(ReservationParameter reservationParameter) {
        return reserveService.getAll(reservationParameter);
    }

    @PutMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public Mono<ReserveResponse> update(@PathVariable("id") String id,
            @RequestBody @Valid ReserveUpdate reserveUpdate) {
        return reserveService.update(id, reserveUpdate);
    }
}
