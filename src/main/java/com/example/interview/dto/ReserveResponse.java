package com.example.interview.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReserveResponse {

    private String id;

    private String username;

    private String reserveNumber;

    private String bookId;

    private Integer copies;

    private Instant createDate;

    private Instant lastUpdateDate;
}
