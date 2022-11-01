package com.example.interview.dto;

import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class ReserveUpdate {

    @NotNull
    private String bookId;

    @NonNull
    private Integer copies;

}
