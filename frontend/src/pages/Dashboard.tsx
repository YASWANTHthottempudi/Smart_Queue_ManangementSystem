import { Link } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

export default function Dashboard() {
  const { user } = useAuth()

  return (
    <div className="page">
      <div className="hero">
        <h1>Welcome, {user?.name}</h1>
        <p className="hero-sub">Get a digital token and track your queue position in real time.</p>
      </div>
      <div className="dashboard-actions">
        <Link to="/queues" className="card card-action">
          <span className="card-icon">📋</span>
          <h3>Get Token</h3>
          <p>Select a queue and get your digital token</p>
        </Link>
        <Link to="/my-tokens" className="card card-action">
          <span className="card-icon">🎫</span>
          <h3>My Tokens</h3>
          <p>View and track your active tokens</p>
        </Link>
      </div>
    </div>
  )
}
