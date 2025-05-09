package com.ne.template.vehicle;


import com.ne.template.owner.Owner;
import com.ne.template.platenumbers.PlateNumber;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
public class Vehicle {
    @Id
    @GeneratedValue
    private UUID id;

    private String chassisNumber;
    private String manufacturerCompany;
    private int manufactureYear;
    private double price;
    private String modelName;

    @ManyToOne
    @JoinColumn(name = "current_owner_id", nullable = false)
    private Owner currentOwner;

    @OneToOne
    @JoinColumn(name = "plate_number_id", unique = true)
    private PlateNumber plateNumber;
}
