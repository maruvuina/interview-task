package com.example.interview.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Objects;

import com.example.interview.dto.ReservationParameter;
import com.example.interview.dto.ReserveRequest;
import com.example.interview.dto.ReserveResponse;
import com.example.interview.service.ReserveService;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReserveControllerTest {

    private static final String URL_TEMPLATE = "/api/v1/reserves";

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ReserveService reserveService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void reserve() {
        // given
        ReserveResponse reserveResponse = new ReserveResponse();
        reserveResponse.setId("1");
        reserveResponse.setUsername("anna");
        reserveResponse.setReserveNumber("1111");
        reserveResponse.setBookId("1");
        reserveResponse.setCopies(2);
        when(reserveService.reserve(any())).thenReturn(Mono.just(reserveResponse));

        // when
        webTestClient.post()
                .uri(URL_TEMPLATE)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(buildRequest())
                .exchange()
                // then
                .expectStatus()
                .isCreated();
    }

    @Test
    void getAll_with_parameter() throws IOException {
        // given
        Flux<ReserveResponse> responseFlux = Flux.just(
                ReserveResponse.builder()
                        .id("1")
                        .username("anna")
                        .reserveNumber("1111")
                        .bookId("1")
                        .copies(2)
                        .createDate(Instant.parse("2022-11-04T07:41:55.062804900Z"))
                        .lastUpdateDate(Instant.parse("2022-11-04T07:41:55.062804900Z"))
                        .build(),
                ReserveResponse.builder()
                        .id("2")
                        .username("inna")
                        .reserveNumber("2222")
                        .bookId("2")
                        .copies(3)
                        .createDate(Instant.parse("2022-11-04T08:55:59.051703800Z"))
                        .lastUpdateDate(Instant.parse("2022-11-04T08:55:59.051703800Z"))
                        .build());

        ReservationParameter reservationParameter = new ReservationParameter();
        reservationParameter.setUsername("anna");
        when(reserveService.getAll(reservationParameter)).thenReturn(responseFlux);

        final var expectedJson = IOUtils
                .toString(Objects.requireNonNull(this.getClass()
                        .getClassLoader()
                        .getResourceAsStream("reserves.json")), StandardCharsets.UTF_8);

        // when
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(URL_TEMPLATE)
                        .queryParam("username", "anna")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                // then
                .expectStatus()
                .isOk()
                .expectBody()
                .json(expectedJson);
    }

    @Test
    void update() {
        // given
        String id = "1";
        ReserveResponse reserveResponse = new ReserveResponse();
        reserveResponse.setId(id);
        reserveResponse.setUsername("anna");
        reserveResponse.setReserveNumber("1111");
        reserveResponse.setBookId("1");
        reserveResponse.setCopies(2);
        when(reserveService.update(any(), any())).thenReturn(Mono.just(reserveResponse));

        // when
        webTestClient.put()
                .uri(URL_TEMPLATE + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(buildRequest())
                .exchange()
                // then
                .expectStatus()
                .isOk();
    }

    private ReserveRequest buildRequest() {
        ReserveRequest reserveRequest = new ReserveRequest();
        reserveRequest.setUsername("anna");
        reserveRequest.setBookId("1");
        reserveRequest.setCopies(1);
        return reserveRequest;
    }
}
