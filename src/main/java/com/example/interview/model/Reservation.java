package com.example.interview.model;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    private String id;

    private String reserveNumber;

    private String username;

    private String bookId;

    private Integer copies;

    private Instant createDate;

    private Instant lastUpdateDate;
}
