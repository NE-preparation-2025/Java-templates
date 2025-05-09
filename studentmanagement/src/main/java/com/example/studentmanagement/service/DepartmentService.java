package com.example.studentmanagement.service;

import com.example.studentmanagement.dto.DepartmentDto;
import com.example.studentmanagement.exception.ResourceAlreadyExistsException;
import com.example.studentmanagement.exception.ResourceNotFoundException;
import com.example.studentmanagement.model.Department;
import com.example.studentmanagement.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    public List<DepartmentDto> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public DepartmentDto getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
        return convertToDto(department);
    }

    public DepartmentDto getDepartmentByName(String name) {
        Department department = departmentRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with name: " + name));
        return convertToDto(department);
    }

    @Transactional
    public DepartmentDto createDepartment(DepartmentDto departmentDto) {
        if (departmentRepository.existsByName(departmentDto.getName())) {
            throw new ResourceAlreadyExistsException("Department already exists with name: " + departmentDto.getName());
        }

        Department department = new Department();
        department.setName(departmentDto.getName());
        department.setDescription(departmentDto.getDescription());

        Department savedDepartment = departmentRepository.save(department);
        return convertToDto(savedDepartment);
    }

    @Transactional
    public DepartmentDto updateDepartment(Long id, DepartmentDto departmentDto) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));

        // Check if name is being changed and if new name already exists
        if (!department.getName().equals(departmentDto.getName()) &&
                departmentRepository.existsByName(departmentDto.getName())) {
            throw new ResourceAlreadyExistsException("Department already exists with name: " + departmentDto.getName());
        }

        department.setName(departmentDto.getName());
        department.setDescription(departmentDto.getDescription());

        Department updatedDepartment = departmentRepository.save(department);
        return convertToDto(updatedDepartment);
    }

    @Transactional
    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
        departmentRepository.delete(department);
    }

    private DepartmentDto convertToDto(Department department) {
        DepartmentDto dto = new DepartmentDto();
        dto.setId(department.getId());
        dto.setName(department.getName());
        dto.setDescription(department.getDescription());
        dto.setStudentCount(department.getStudents().size());
        return dto;
    }
}