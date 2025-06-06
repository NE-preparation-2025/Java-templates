package com.ne.template.owner.dto;

import com.ne.template.commons.validation.ValidRwandaId;
import com.ne.template.commons.validation.ValidRwandanPhoneNumber;
import jakarta.validation.constraints.NotBlank;

public record RegisterOwnerRequestDTO(
        @NotBlank(message = "Owner names are required")
        String fullNames,

        @NotBlank(message = "National ID is required")
        @ValidRwandaId
        String nationalId,

        @NotBlank(message ="PhoneNumber is required")
        @ValidRwandanPhoneNumber
        String phoneNumber,

        @NotBlank(message = "Address is required")
        String address
) {
}
