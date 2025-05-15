package com.ne.template.vehicle.mappers;

import com.ne.template.vehicle.Vehicle;
import com.ne.template.vehicle.dto.RegisterVehicleRequestDTO;
import com.ne.template.vehicle.dto.VehicleResponseDTO;
import com.ne.template.platenumbers.PlateNumber;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface VehicleMapper {

    @Mapping(source = "currentOwner.id", target = "currentOwnerId")
    @Mapping(source = "plateNumber.id", target = "plateNumber")
    VehicleResponseDTO toResponse(Vehicle vehicle);

    @Mapping(target = "currentOwner", ignore = true)
    @Mapping(target = "plateNumber", ignore = true)
    Vehicle toEntity(RegisterVehicleRequestDTO dto);

}
