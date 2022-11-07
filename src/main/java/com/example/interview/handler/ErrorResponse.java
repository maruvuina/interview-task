package com.example.interview.handler;

import java.time.Instant;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ErrorResponse {

    private Instant timestamp;

    private String error;

    private String message;

    private String trace;

}
