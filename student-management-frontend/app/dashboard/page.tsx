"use client"

import type React from "react"

import { useEffect, useState } from "react"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { GraduationCap, Building, Users, BookOpen } from "lucide-react"
import { useAuth } from "@/lib/auth-context"
import { getDashboardStats } from "@/lib/api-service"

interface DashboardStats {
  totalStudents: number
  totalDepartments: number
  totalUsers: number
  totalCourses: number
}

export default function DashboardPage() {
  const { user } = useAuth()
  const [stats, setStats] = useState<DashboardStats>({
    totalStudents: 0,
    totalDepartments: 0,
    totalUsers: 0,
    totalCourses: 0,
  })
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const fetchStats = async () => {
      try {
        const data = await getDashboardStats()
        setStats(data)
      } catch (error) {
        console.error("Failed to fetch dashboard stats:", error)
      } finally {
        setLoading(false)
      }
    }

    fetchStats()
  }, [])

  return (
    <div className="space-y-6">
      <div className="flex flex-col space-y-2">
        <h1 className="text-3xl font-bold tracking-tight">Dashboard</h1>
        <p className="text-muted-foreground">Welcome back, {user?.username || "User"}!</p>
      </div>

      {loading ? (
        <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
          {[...Array(4)].map((_, i) => (
            <Card key={i} className="animate-pulse">
              <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                <CardTitle className="text-sm font-medium">Loading...</CardTitle>
                <div className="h-4 w-4 rounded-full bg-muted"></div>
              </CardHeader>
              <CardContent>
                <div className="text-2xl font-bold">--</div>
              </CardContent>
            </Card>
          ))}
        </div>
      ) : (
        <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
          <DashboardCard
            title="Total Students"
            value={stats.totalStudents}
            description="Active students in the system"
            icon={<GraduationCap className="h-5 w-5 text-primary" />}
          />
          <DashboardCard
            title="Departments"
            value={stats.totalDepartments}
            description="Academic departments"
            icon={<Building className="h-5 w-5 text-primary" />}
          />
          <DashboardCard
            title="Users"
            value={stats.totalUsers}
            description="Registered system users"
            icon={<Users className="h-5 w-5 text-primary" />}
          />
          <DashboardCard
            title="Courses"
            value={stats.totalCourses}
            description="Available courses"
            icon={<BookOpen className="h-5 w-5 text-primary" />}
          />
        </div>
      )}

      <div className="grid gap-4 md:grid-cols-2">
        <Card>
          <CardHeader>
            <CardTitle>Recent Activities</CardTitle>
            <CardDescription>Your recent actions in the system</CardDescription>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              <ActivityItem
                title="Student Added"
                description="You added a new student: John Doe"
                timestamp="2 hours ago"
              />
              <ActivityItem
                title="Department Updated"
                description="You updated Computer Science department details"
                timestamp="Yesterday"
              />
              <ActivityItem
                title="User Role Changed"
                description="You changed role for user: jane_smith"
                timestamp="3 days ago"
              />
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle>Quick Actions</CardTitle>
            <CardDescription>Common tasks you can perform</CardDescription>
          </CardHeader>
          <CardContent className="grid gap-2">
            <QuickActionLink
              href="/dashboard/students/new"
              title="Add New Student"
              icon={<GraduationCap className="h-4 w-4" />}
            />
            <QuickActionLink
              href="/dashboard/departments/new"
              title="Create Department"
              icon={<Building className="h-4 w-4" />}
            />
            <QuickActionLink href="/dashboard/profile" title="Update Profile" icon={<Users className="h-4 w-4" />} />
          </CardContent>
        </Card>
      </div>
    </div>
  )
}

function DashboardCard({
  title,
  value,
  description,
  icon,
}: {
  title: string
  value: number
  description: string
  icon: React.ReactNode
}) {
  return (
    <Card>
      <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
        <CardTitle className="text-sm font-medium">{title}</CardTitle>
        {icon}
      </CardHeader>
      <CardContent>
        <div className="text-2xl font-bold">{value}</div>
        <p className="text-xs text-muted-foreground">{description}</p>
      </CardContent>
    </Card>
  )
}

function ActivityItem({
  title,
  description,
  timestamp,
}: {
  title: string
  description: string
  timestamp: string
}) {
  return (
    <div className="flex items-center">
      <div className="mr-4 rounded-full bg-primary/10 p-2">
        <div className="h-2 w-2 rounded-full bg-primary"></div>
      </div>
      <div className="flex-1 space-y-1">
        <p className="text-sm font-medium leading-none">{title}</p>
        <p className="text-sm text-muted-foreground">{description}</p>
      </div>
      <div className="text-xs text-muted-foreground">{timestamp}</div>
    </div>
  )
}

function QuickActionLink({
  href,
  title,
  icon,
}: {
  href: string
  title: string
  icon: React.ReactNode
}) {
  return (
    <a href={href} className="flex items-center rounded-md border p-3 hover:bg-accent">
      <div className="mr-3 rounded-full bg-primary/10 p-2">{icon}</div>
      <div className="font-medium">{title}</div>
    </a>
  )
}
