import { createContext, useContext, useState, useEffect, ReactNode } from 'react'

export interface User {
  id: number
  email: string
  name: string
  role: string
}

interface AuthContextType {
  user: User | null
  token: string | null
  loading: boolean
  login: (email: string, password: string) => Promise<void>
  register: (name: string, email: string, password: string) => Promise<void>
  logout: () => void
}

const AuthContext = createContext<AuthContextType | null>(null)

const STORAGE_KEY = 'smart_queue_auth'

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<User | null>(null)
  const [token, setToken] = useState<string | null>(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const stored = localStorage.getItem(STORAGE_KEY)
    if (stored) {
      try {
        const { user: u, token: t } = JSON.parse(stored)
        setUser(u)
        setToken(t)
      } catch {
        localStorage.removeItem(STORAGE_KEY)
      }
    }
    setLoading(false)
  }, [])

  const persist = (u: User, t: string) => {
    localStorage.setItem(STORAGE_KEY, JSON.stringify({ user: u, token: t }))
    setUser(u)
    setToken(t)
  }

  const login = async (email: string, password: string) => {
    const { api } = await import('../services/api')
    const res = await api.post('/auth/login', { email, password })
    const { token: t, userId, email: e, name: n, role: r } = res.data
    persist({ id: userId, email: e, name: n, role: r }, t)
  }

  const register = async (name: string, email: string, password: string) => {
    const { api } = await import('../services/api')
    const res = await api.post('/auth/register', { name, email, password })
    const { token: t, userId, email: e, name: n, role: r } = res.data
    persist({ id: userId, email: e, name: n, role: r }, t)
  }

  const logout = () => {
    localStorage.removeItem(STORAGE_KEY)
    setUser(null)
    setToken(null)
  }

  return (
    <AuthContext.Provider value={{ user, token, loading, login, register, logout }}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  const ctx = useContext(AuthContext)
  if (!ctx) throw new Error('useAuth must be used within AuthProvider')
  return ctx
}
