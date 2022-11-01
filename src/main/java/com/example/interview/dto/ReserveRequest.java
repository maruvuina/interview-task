package com.example.interview.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class ReserveRequest {

    @NonNull
    private String username;

    @NotNull
    private String bookId;

    @NonNull
    private Integer copies;
}
