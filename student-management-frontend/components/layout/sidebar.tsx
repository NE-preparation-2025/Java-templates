"use client"

import type React from "react"

import { useState } from "react"
import Link from "next/link"
import { usePathname } from "next/navigation"
import { cn } from "@/lib/utils"
import { Button } from "@/components/ui/button"
import { ScrollArea } from "@/components/ui/scroll-area"
import { Users, GraduationCap, Building, UserCog, BarChart, Settings, LogOut, Menu, X } from "lucide-react"
import { useAuth } from "@/lib/auth-context"


interface SidebarProps extends React.HTMLAttributes<HTMLDivElement> {
  className?: string
}

export function Sidebar({ className }: SidebarProps) {
  const pathname = usePathname()
  const { user, logout } = useAuth()
  const [isOpen, setIsOpen] = useState(false)

  const isAdmin = user?.roles?.includes("ROLE_ADMIN")
  const isModerator = user?.roles?.includes("ROLE_MODERATOR") || isAdmin

  const toggleSidebar = () => {
    setIsOpen(!isOpen)
  }

  const closeSidebar = () => {
    setIsOpen(false)
  }

  const navItems = [
    {
      title: "Dashboard",
      href: "/dashboard",
      icon: BarChart,
      access: true,
    },
    {
      title: "Students",
      href: "/dashboard/students",
      icon: GraduationCap,
      access: true,
    },
    {
      title: "Departments",
      href: "/dashboard/departments",
      icon: Building,
      access: true,
    },
    {
      title: "Users",
      href: "/dashboard/users",
      icon: Users,
      access: isAdmin,
    },
    {
      title: "User Profile",
      href: "/dashboard/profile",
      icon: UserCog,
      access: true,
    },
    {
      title: "Settings",
      href: "/dashboard/settings",
      icon: Settings,
      access: isModerator,
    },
  ]

  return (
    <>
      {/* Mobile menu button */}
      <Button variant="outline" size="icon" className="fixed top-4 left-4 z-50 md:hidden" onClick={toggleSidebar}>
        {isOpen ? <X className="h-5 w-5" /> : <Menu className="h-5 w-5" />}
      </Button>

      {/* Sidebar */}
      <div
        className={cn(
          "fixed inset-y-0 left-0 z-40 flex w-64 flex-col bg-card shadow-lg transition-transform duration-300 ease-in-out md:translate-x-0",
          isOpen ? "translate-x-0" : "-translate-x-full",
          className,
        )}
      >
        <div className="flex h-14 items-center border-b px-4">
          <Link href="/dashboard" className="flex items-center" onClick={closeSidebar}>
            <GraduationCap className="mr-2 h-6 w-6 text-primary" />
            <span className="text-lg font-bold">Student MS</span>
          </Link>
        </div>
        <ScrollArea className="flex-1 py-4">
          <nav className="grid gap-1 px-2">
            {navItems
              .filter((item) => item.access)
              .map((item) => (
                <Link
                  key={item.href}
                  href={item.href}
                  onClick={closeSidebar}
                  className={cn(
                    "flex items-center gap-3 rounded-md px-3 py-2 text-sm font-medium hover:bg-accent hover:text-accent-foreground",
                    pathname === item.href ? "bg-accent text-accent-foreground" : "transparent",
                  )}
                >
                  <item.icon className="h-5 w-5" />
                  {item.title}
                </Link>
              ))}
          </nav>
        </ScrollArea>
        <div className="border-t p-4">
          <Button variant="outline" className="w-full justify-start" onClick={logout}>
            <LogOut className="mr-2 h-5 w-5" />
            Logout
          </Button>
        </div>
      </div>
    </>
  )
}
