// This file contains authentication-related API calls

const API_URL = "http://localhost:8080/api"

export async function login(username: string, password: string): Promise<boolean> {
  try {
    const response = await fetch(`${API_URL}/auth/signin`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ username, password }),
      credentials: "include",
    })

    if (!response.ok) {
      return false
    }

    const data = await response.json()
    localStorage.setItem("token", data.token)
    return true
  } catch (error) {
    console.error("Login error:", error)
    return false
  }
}

export async function register(username: string, email: string, password: string): Promise<boolean> {
  try {
    const response = await fetch(`${API_URL}/auth/signup`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ username, email, password }),
    })

    return response.ok
  } catch (error) {
    console.error("Registration error:", error)
    return false
  }
}

export async function getCurrentUser() {
  try {
    const token = localStorage.getItem("token")
    if (!token) {
      return null
    }

    const response = await fetch(`${API_URL}/auth/user`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })

    if (!response.ok) {
      throw new Error("Failed to get current user")
    }

    return await response.json()
  } catch (error) {
    console.error("Get current user error:", error)
    return null
  }
}

export function logout() {
  localStorage.removeItem("token")
}
