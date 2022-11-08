package com.example.interview.controller;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.interview.dto.ReserveResponse;
import com.example.interview.service.ReserveService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({ReserveController.class, ReserveService.class})
class ReserveControllerTest {

    private static final String URL_TEMPLATE = "/api/v1/reserves";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReserveService reserveService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void reserve() throws Exception {
        // given
        ReserveResponse reserveResponse = new ReserveResponse();
        reserveResponse.setUsername("anna");
        reserveResponse.setReserveNumber("1111");
        reserveResponse.setBookId("1");
        reserveResponse.setCopies(2);
        Map<String, Object> body = new HashMap<>();
        body.put("username", "anna");
        body.put("reserveNumber", "1111");
        body.put("bookId", "1");
        body.put("copies", 2);

        when(reserveService.reserve(any())).thenReturn(reserveResponse);

        // when
        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getAll_without_parameter() throws Exception {
        // given
        List<ReserveResponse> reserveList = new ArrayList<>(
                Arrays.asList(
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
                                .build())
        );

        when(reserveService.getAll(any())).thenReturn(reserveList);

        // then
        mockMvc.perform(get(URL_TEMPLATE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(reserveList.size()))
                .andDo(print());
    }

    @Test
    void update() throws Exception {
        // given
        String id = "1";
        ReserveResponse reserveResponse = new ReserveResponse();
        reserveResponse.setId(id);
        reserveResponse.setUsername("anna");
        reserveResponse.setReserveNumber("1111");
        reserveResponse.setBookId("1");
        reserveResponse.setCopies(2);
        Map<String, Object> body = new HashMap<>();
        body.put("bookId", "1");
        body.put("copies", 2);
        when(reserveService.update(any(), any())).thenReturn(reserveResponse);

        // when
        mockMvc.perform(put(URL_TEMPLATE + "/{id}", id )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
