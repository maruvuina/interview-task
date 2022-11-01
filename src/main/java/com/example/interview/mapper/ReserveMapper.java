package com.example.interview.mapper;

import com.example.interview.dto.ReserveResponse;
import com.example.interview.model.Reservation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReserveMapper {

    ReserveResponse toReserveResponse(Reservation reservation);
}
