package com.ne.template.owner.mappers;

import com.ne.template.owner.Owner;
import com.ne.template.owner.dto.RegisterOwnerRequestDTO;
import com.ne.template.owner.dto.OwnerResponseDTO;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface OwnerMapper {

    @Mapping(target = "fullNames", source = "fullNames")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    Owner toEntity(RegisterOwnerRequestDTO request);
    OwnerResponseDTO toResponse(Owner owner);
}
