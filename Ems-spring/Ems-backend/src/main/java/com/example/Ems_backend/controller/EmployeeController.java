package com.example.Ems_backend.controller;

import com.example.Ems_backend.dto.EmployeeDto;
import com.example.Ems_backend.entity.Employee;
import com.example.Ems_backend.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    private EmployeeService employeeService;

    //Build add employee REST API
    @PostMapping
    public ResponseEntity<EmployeeDto> createEmployee( @RequestBody EmployeeDto employeeDto){
        EmployeeDto savedEmployee = employeeService.createEmployee(employeeDto);
         return  new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }


    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    @GetMapping("{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable Long employeeId){
      EmployeeDto employeeDto=  employeeService.getEmployeeById(employeeId);
      return ResponseEntity.ok(employeeDto);

    }

    //Build Get All Employees REST API
    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAllEmployees(){
        List <EmployeeDto> employees=employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);

    }
    //Build Update Employee REST API
    @PutMapping("{id}")
    public ResponseEntity<EmployeeDto> updateEmployee( @PathVariable("id") Long employeeId,@RequestBody EmployeeDto updateEmployee){
       EmployeeDto employeeDto= employeeService.updateEmployee(employeeId, updateEmployee);
       return ResponseEntity.ok(employeeDto);
    }

    //Build delete Employee Rest API
    @DeleteMapping("{id}")
    public  ResponseEntity<String> deleteEmployee( @PathVariable("id") Long employeeId){
        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.ok("Employee deleted successfully");

    }


}
