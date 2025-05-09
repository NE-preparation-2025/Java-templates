package com.naome.template.vehicle;

import com.naome.template.commons.exceptions.ResourceNotFoundException;
import com.naome.template.history.HistoryRepository;
import com.naome.template.history.VehicleOwnershipHistory;
import com.naome.template.owner.Owner;
import com.naome.template.owner.OwnerRepository;

import com.naome.template.platenumbers.PlateNumber;
import com.naome.template.platenumbers.PlateRepository;
import com.naome.template.platenumbers.PlateService;
import com.naome.template.platenumbers.PlateStatus;
import com.naome.template.vehicle.dto.RegisterVehicleRequestDTO;
import com.naome.template.vehicle.dto.VehicleResponseDTO;
import com.naome.template.vehicle.dto.VehicleTransferRequest;
import com.naome.template.vehicle.mappers.VehicleMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final OwnerRepository ownerRepository;
    private final VehicleMapper vehicleMapper;
    private final PlateRepository plateRepository;
    private final PlateService plateService;
    private final HistoryRepository historyRepository;

    public VehicleResponseDTO registerVehicle(RegisterVehicleRequestDTO request) {
        var owner = ownerRepository.findById(request.currentOwnerId())
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found"));

        var plate = plateRepository.findById(request.plateNumberId())
                .orElseThrow(() -> new ResourceNotFoundException("Plate number not found"));

        Vehicle vehicle = vehicleMapper.toEntity(request);
        vehicle.setCurrentOwner(owner);
        vehicle.setPlateNumber(plate);

        vehicleRepository.save(vehicle);

        // Update plate number assignment after vehicle is saved
        plateService.assignPlateToVehicle(plate.getId(), vehicle);

        return vehicleMapper.toResponse(vehicle);
    }

    public VehicleResponseDTO getVehicleById(UUID id) {
        var vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));
        return vehicleMapper.toResponse(vehicle);
    }

    public List<VehicleResponseDTO> getAllVehicles() {
        return vehicleRepository.findAll().stream()
                .map(vehicleMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void transferVehicle(VehicleTransferRequest request) {
        Vehicle vehicle = vehicleRepository.findById(request.vehicleId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        Owner newOwner = ownerRepository.findById(request.newOwnerId())
                .orElseThrow(() -> new RuntimeException("New owner not found"));

        // 1. Release current plate
        PlateNumber currentPlate = plateRepository.findByVehicleId(vehicle.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Current plate not found"));
        currentPlate.setStatus(PlateStatus.AVAILABLE);
        currentPlate.setVehicle(null);
        currentPlate.setOwner(null);
        plateRepository.save(currentPlate);

        // 2. Find ANY available plate (not tied to owner)
        PlateNumber newPlate = plateRepository.findFirstByStatus(PlateStatus.AVAILABLE)
                .orElseThrow(() -> new ResourceNotFoundException("No available plate numbers in system"));

        // 3. Assign to new owner and vehicle
        newPlate.setStatus(PlateStatus.IN_USE);
        newPlate.setVehicle(vehicle);
        newPlate.setOwner(newOwner); // 🔥 Set the new owner
        plateRepository.save(newPlate);

        // 4. Save ownership history
        VehicleOwnershipHistory history = new VehicleOwnershipHistory();
        history.setVehicle(vehicle);
        history.setFromOwner(vehicle.getCurrentOwner());
        history.setToOwner(newOwner);
        history.setPurchasePrice(request.purchasePrice());
        history.setTransferDate(new Date());
        history.setOldPlateNumber(currentPlate.getPlateNumber());
        history.setNewPlateNumber(newPlate.getPlateNumber());
        historyRepository.save(history);
        // 5. Update vehicle
        vehicle.setCurrentOwner(newOwner);
        vehicle.setPlateNumber(newPlate);
        vehicleRepository.save(vehicle);
    }
    public List<Vehicle> searchByNationalId(String nationalId) {
        return vehicleRepository.findByCurrentOwnerNationalId(nationalId);
    }

}