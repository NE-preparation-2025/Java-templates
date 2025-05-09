package com.example.Ems_backend.service.impl;

import com.example.Ems_backend.dto.EmployeeDto;
import com.example.Ems_backend.entity.Employee;
import com.example.Ems_backend.exception.ResourceNotFoundException;
import com.example.Ems_backend.mapper.EmployeeMapper;
import com.example.Ems_backend.repository.EmployeeRepository;
import com.example.Ems_backend.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {



    private EmployeeRepository employeeRepository;



    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        Employee employee= EmployeeMapper.mapToEmployee(employeeDto);
       Employee savedEmployee= employeeRepository.save(employee);
        return EmployeeMapper.mapToEmployeeDto(savedEmployee);
    }

    @Override
    public EmployeeDto getEmployeeById(Long employeeId) {
       Employee employee= employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("No employee with a given id: " + employeeId));
    return EmployeeMapper.mapToEmployeeDto(employee);
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        List<Employee> employees=employeeRepository.findAll();
        return employees.stream().map((employee) -> EmployeeMapper.mapToEmployeeDto(employee) )
                .collect(Collectors.toList());

    }

    @Override
    public EmployeeDto updateEmployee(Long employeeId, EmployeeDto updateEmployee) {
       Employee employee= employeeRepository.findById(employeeId).orElseThrow(
                () -> new ResourceNotFoundException("No employee exist with this given id: " + employeeId)
        );
       employee.setFirstName(updateEmployee.getFirstName());
       employee.setLastName(updateEmployee.getLastName());
       employee.setEmail(updateEmployee.getEmail());

       Employee updateEmployeeDto=employeeRepository.save(employee);
        return EmployeeMapper.mapToEmployeeDto(updateEmployeeDto);
    }

    @Override
    public void deleteEmployee(Long employeeId) {
        Employee employee= employeeRepository.findById(employeeId)
                .orElseThrow(()-> new ResourceNotFoundException("Employee is not exists with a given id: " + employeeId));
       employeeRepository.deleteById(employeeId);
    }

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
}
