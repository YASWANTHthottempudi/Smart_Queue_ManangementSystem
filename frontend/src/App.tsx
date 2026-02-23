import { Routes, Route, Navigate } from 'react-router-dom'
import { useAuth } from './context/AuthContext'
import Layout from './components/Layout'
import Login from './pages/Login'
import Register from './pages/Register'
import Dashboard from './pages/Dashboard'
import Queues from './pages/Queues'
import MyTokens from './pages/MyTokens'
import TokenStatus from './pages/TokenStatus'
import AdminDashboard from './pages/AdminDashboard'
import AdminCounters from './pages/AdminCounters'

function ProtectedRoute({ children, adminOnly = false }: { children: React.ReactNode; adminOnly?: boolean }) {
  const { user, loading } = useAuth()
  if (loading) return <div className="loading-screen">Loading...</div>
  if (!user) return <Navigate to="/login" replace />
  if (adminOnly && user.role !== 'ADMIN') return <Navigate to="/" replace />
  return <>{children}</>
}

export default function App() {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />
      <Route path="/" element={<Layout />}>
        <Route index element={<ProtectedRoute><Dashboard /></ProtectedRoute>} />
        <Route path="queues" element={<ProtectedRoute><Queues /></ProtectedRoute>} />
        <Route path="my-tokens" element={<ProtectedRoute><MyTokens /></ProtectedRoute>} />
        <Route path="token/:tokenId" element={<ProtectedRoute><TokenStatus /></ProtectedRoute>} />
        <Route path="admin" element={<ProtectedRoute adminOnly><AdminDashboard /></ProtectedRoute>} />
        <Route path="admin/counters" element={<ProtectedRoute adminOnly><AdminCounters /></ProtectedRoute>} />
      </Route>
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  )
}
