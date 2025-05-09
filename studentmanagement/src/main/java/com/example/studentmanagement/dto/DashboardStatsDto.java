package com.example.studentmanagement.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DashboardStatsDto {
    private Long totalStudents;
    private Long totalDepartments;
    private Long totalUsers;
    private Long totalCourses;

}
