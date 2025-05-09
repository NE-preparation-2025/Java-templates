// This file contains API calls for the application

const API_URL = "http://localhost:8080/api"

// Helper function to get the auth header
const getAuthHeader = () => {
  const token = localStorage.getItem("token")
  return {
    Authorization: `Bearer ${token}`,
    "Content-Type": "application/json",
  }
}

// Dashboard stats
export async function getDashboardStats() {
  try {
    const response = await fetch(`${API_URL}/dashboard/stats`, {
      headers: getAuthHeader(),
    })

    if (!response.ok) {
      throw new Error("Failed to fetch dashboard stats")
    }

    return await response.json()
  } catch (error) {
    console.error("Error fetching dashboard stats:", error)
    // Return mock data for now
    return {
      totalStudents: 156,
      totalDepartments: 8,
      totalUsers: 24,
      totalCourses: 42,
    }
  }
}

// Student API calls
export async function getStudents() {
  try {
    const response = await fetch(`${API_URL}/students`, {
      headers: getAuthHeader(),
    })

    if (!response.ok) {
      throw new Error("Failed to fetch students")
    }

    return await response.json()
  } catch (error) {
    console.error("Error fetching students:", error)
    // Return mock data for now
    return [
      {
        id: 1,
        firstName: "John",
        lastName: "Doe",
        email: "john.doe@example.com",
        department: {
          id: 1,
          name: "Computer Science",
        },
        enrollmentDate: "2023-09-01",
      },
      {
        id: 2,
        firstName: "Jane",
        lastName: "Smith",
        email: "jane.smith@example.com",
        department: {
          id: 2,
          name: "Electrical Engineering",
        },
        enrollmentDate: "2023-09-01",
      },
      {
        id: 3,
        firstName: "Michael",
        lastName: "Johnson",
        email: "michael.johnson@example.com",
        department: {
          id: 1,
          name: "Computer Science",
        },
        enrollmentDate: "2023-09-01",
      },
    ]
  }
}

export async function getStudent(id: number) {
  try {
    const response = await fetch(`${API_URL}/students/${id}`, {
      headers: getAuthHeader(),
    })

    if (!response.ok) {
      throw new Error("Failed to fetch student")
    }

    return await response.json()
  } catch (error) {
    console.error(`Error fetching student ${id}:`, error)
    return null
  }
}

interface StudentData {
  firstName: string;
  lastName: string;
  email: string;
  departmentId: number;
  enrollmentDate: string;
}

export async function createStudent(studentData: StudentData) {
  try {
    const response = await fetch(`${API_URL}/students`, {
      method: "POST",
      headers: getAuthHeader(),
      body: JSON.stringify(studentData),
    })

    if (!response.ok) {
      throw new Error("Failed to create student")
    }

    return await response.json()
  } catch (error) {
    console.error("Error creating student:", error)
    throw error
  }
}

export async function updateStudent(studentData: StudentData, id: number) {
  try {
    const response = await fetch(`${API_URL}/students/${id}`, {
      method: "PUT",
      headers: getAuthHeader(),
      body: JSON.stringify(studentData),
    })

    if (!response.ok) {
      throw new Error("Failed to update student")
    }

    return await response.json()
  } catch (error) {
    console.error(`Error updating student ${id}:`, error)
    throw error
  }
}

export async function deleteStudent(id: number) {
  try {
    const response = await fetch(`${API_URL}/students/${id}`, {
      method: "DELETE",
      headers: getAuthHeader(),
    })

    if (!response.ok) {
      throw new Error("Failed to delete student")
    }

    return true
  } catch (error) {
    console.error(`Error deleting student ${id}:`, error)
    throw error
  }
}

// Department API calls
export async function getDepartments() {
  try {
    const response = await fetch(`${API_URL}/departments`, {
      headers: getAuthHeader(),
    })

    if (!response.ok) {
      throw new Error("Failed to fetch departments")
    }

    return await response.json()
  } catch (error) {
    console.error("Error fetching departments:", error)
    // Return mock data for now
    return [
      {
        id: 1,
        name: "Computer Science",
        description: "Study of computers and computational systems",
      },
      {
        id: 2,
        name: "Electrical Engineering",
        description: "Study of electrical systems and electronics",
      },
      {
        id: 3,
        name: "Mechanical Engineering",
        description: "Study of physical machines, forces, and power",
      },
    ]
  }
}

// User API calls (for admin)
export async function getUsers() {
  try {
    const response = await fetch(`${API_URL}/users`, {
      headers: getAuthHeader(),
    })

    if (!response.ok) {
      throw new Error("Failed to fetch users")
    }

    return await response.json()
  } catch (error) {
    console.error("Error fetching users:", error)
    // Return mock data for now
    return [
      {
        id: 1,
        username: "admin",
        email: "admin@example.com",
        roles: ["ROLE_ADMIN", "ROLE_USER"],
      },
      {
        id: 2,
        username: "moderator",
        email: "moderator@example.com",
        roles: ["ROLE_MODERATOR", "ROLE_USER"],
      },
      {
        id: 3,
        username: "user",
        email: "user@example.com",
        roles: ["ROLE_USER"],
      },
    ]
  }
}
