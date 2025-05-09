package com.ne.template.vehicle.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record VehicleResponseDTO(
        UUID id,
        String chassisNumber,
        String manufacturerCompany,
        Integer manufactureYear,
        Double price,
        String modelName,
        UUID currentOwnerId,
        String plateNumber
) {
}
