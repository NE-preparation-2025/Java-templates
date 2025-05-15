package com.ne.template.platenumbers.mappers;

import com.ne.template.platenumbers.PlateNumber;
import com.ne.template.platenumbers.dto.PlateResponseDTO;
import com.ne.template.platenumbers.dto.RegisterPlateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlateMapper {
    @Mapping(source = "issuedDate", target = "issuedDate", dateFormat = "yyyy-MM-dd")
    PlateResponseDTO toResponse(PlateNumber plate);

    @Mapping(target = "issuedDate", source = "issueDate")
    PlateNumber toEntity(RegisterPlateRequest request);
}