package com.example.studentmanagement.service;

import com.example.studentmanagement.dto.StudentDto;
import com.example.studentmanagement.exception.ResourceNotFoundException;
import com.example.studentmanagement.model.Department;
import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.repository.DepartmentRepository;
import com.example.studentmanagement.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    public List<StudentDto> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public StudentDto getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        return convertToDto(student);
    }

    public StudentDto getStudentByStudentId(String studentId) {
        Student student = studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with student ID: " + studentId));
        return convertToDto(student);
    }

    @Transactional
    public StudentDto createStudent(StudentDto studentDto) {
        Department department = null;
        if (studentDto.getDepartmentId() != null) {
            department = departmentRepository.findById(studentDto.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + studentDto.getDepartmentId()));
        }

        Student student = new Student();
        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());
        student.setStudentId(studentDto.getStudentId());
        student.setEmail(studentDto.getEmail());
        student.setPhoneNumber(studentDto.getPhoneNumber());
        student.setDateOfBirth(studentDto.getDateOfBirth());
        student.setGender(studentDto.getGender());
        student.setDepartment(department);

        Student savedStudent = studentRepository.save(student);
        return convertToDto(savedStudent);
    }

    @Transactional
    public StudentDto updateStudent(Long id, StudentDto studentDto) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));

        Department department = null;
        if (studentDto.getDepartmentId() != null) {
            department = departmentRepository.findById(studentDto.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + studentDto.getDepartmentId()));
        }

        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());
        student.setStudentId(studentDto.getStudentId());
        student.setEmail(studentDto.getEmail());
        student.setPhoneNumber(studentDto.getPhoneNumber());
        student.setDateOfBirth(studentDto.getDateOfBirth());
        student.setGender(studentDto.getGender());
        student.setDepartment(department);

        Student updatedStudent = studentRepository.save(student);
        return convertToDto(updatedStudent);
    }

    @Transactional
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        studentRepository.delete(student);
    }

    public List<StudentDto> searchStudents(String query) {
        return studentRepository.findByFirstNameContainingOrLastNameContaining(query, query).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<StudentDto> getStudentsByDepartment(Long departmentId) {
        return studentRepository.findByDepartmentId(departmentId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private StudentDto convertToDto(Student student) {
        StudentDto dto = new StudentDto();
        dto.setId(student.getId());
        dto.setFirstName(student.getFirstName());
        dto.setLastName(student.getLastName());
        dto.setStudentId(student.getStudentId());
        dto.setEmail(student.getEmail());
        dto.setPhoneNumber(student.getPhoneNumber());
        dto.setDateOfBirth(student.getDateOfBirth());
        dto.setGender(student.getGender());

        if (student.getDepartment() != null) {
            dto.setDepartmentId(student.getDepartment().getId());
            dto.setDepartmentName(student.getDepartment().getName());
        }

        return dto;
    }
}