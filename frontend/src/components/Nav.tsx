import { Link } from 'react-router-dom'
import type { User } from '../context/AuthContext'

export default function Nav({ user }: { user: User | null }) {
  if (!user) return null

  const logout = () => {
    localStorage.removeItem('smart_queue_auth')
    window.location.href = '/login'
  }

  return (
    <nav className="nav">
      <Link to="/" className="nav-brand">
        <span className="nav-logo">◎</span>
        Smart Queue
      </Link>
      <div className="nav-links">
        <Link to="/">Home</Link>
        <Link to="/queues">Queues</Link>
        <Link to="/my-tokens">My Tokens</Link>
        {user.role === 'ADMIN' && (
          <>
            <Link to="/admin">Admin</Link>
            <Link to="/admin/counters">Counters</Link>
          </>
        )}
      </div>
      <div className="nav-user">
        <span className="nav-user-name">{user.name}</span>
        <button type="button" className="btn btn-ghost" onClick={logout}>
          Logout
        </button>
      </div>
    </nav>
  )
}
