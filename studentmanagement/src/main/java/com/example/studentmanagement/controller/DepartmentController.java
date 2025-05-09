package com.example.studentmanagement.controller;

import com.example.studentmanagement.dto.DepartmentDto;
import com.example.studentmanagement.model.Department;
import com.example.studentmanagement.repository.DepartmentRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    @Autowired
    private DepartmentRepository departmentRepository;

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<DepartmentDto>> getAllDepartments() {
        List<Department> departments = departmentRepository.findAll();
        List<DepartmentDto> departmentDtos = departments.stream()
                .map(department -> {
                    DepartmentDto dto = new DepartmentDto();
                    dto.setId(department.getId());
                    dto.setName(department.getName());
                    dto.setDescription(department.getDescription());
                    return dto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(departmentDtos);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<DepartmentDto> getDepartmentById(@PathVariable Long id) {
        return departmentRepository.findById(id)
                .map(department -> {
                    DepartmentDto dto = new DepartmentDto();
                    dto.setId(department.getId());
                    dto.setName(department.getName());
                    dto.setDescription(department.getDescription());
                    return ResponseEntity.ok(dto);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<DepartmentDto> createDepartment(@Valid @RequestBody DepartmentDto departmentDto) {
        // Check if name already exists
        if (departmentRepository.existsByName(departmentDto.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Department name already exists");
        }

        // Create department
        Department department = new Department();
        department.setName(departmentDto.getName());
        department.setDescription(departmentDto.getDescription());

        Department savedDepartment = departmentRepository.save(department);

        // Map to DTO
        DepartmentDto savedDto = new DepartmentDto();
        savedDto.setId(savedDepartment.getId());
        savedDto.setName(savedDepartment.getName());
        savedDto.setDescription(savedDepartment.getDescription());

        return ResponseEntity.status(HttpStatus.CREATED).body(savedDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<DepartmentDto> updateDepartment(@PathVariable Long id, @Valid @RequestBody DepartmentDto departmentDto) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found"));

        // Check if name already exists and it's not the current department's name
        if (!department.getName().equals(departmentDto.getName()) && departmentRepository.existsByName(departmentDto.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Department name already exists");
        }

        // Update department
        department.setName(departmentDto.getName());
        department.setDescription(departmentDto.getDescription());

        Department updatedDepartment = departmentRepository.save(department);

        // Map to DTO
        DepartmentDto updatedDto = new DepartmentDto();
        updatedDto.setId(updatedDepartment.getId());
        updatedDto.setName(updatedDepartment.getName());
        updatedDto.setDescription(updatedDepartment.getDescription());

        return ResponseEntity.ok(updatedDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        if (!departmentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        departmentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
