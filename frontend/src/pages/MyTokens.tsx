import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { api } from '../services/api'

interface TokenResponse {
  id: number
  tokenNumber: number
  queueId: number
  queueName: string
  status: string
  createdTime: string
  positionInQueue: number
  estimatedWaitTimeMinutes: number
}

export default function MyTokens() {
  const [tokens, setTokens] = useState<TokenResponse[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    api.get('/api/tokens/my')
      .then((r) => setTokens(r.data))
      .finally(() => setLoading(false))
  }, [])

  const statusColor = (s: string) => {
    if (s === 'SERVED') return 'var(--success)'
    if (s === 'SERVING') return 'var(--accent)'
    if (s === 'WAITING') return 'var(--warning)'
    return 'var(--text-muted)'
  }

  if (loading) return <div className="page"><div className="loading">Loading your tokens...</div></div>

  return (
    <div className="page">
      <h1>My Tokens</h1>
      <p className="page-sub">Track all your queue tokens.</p>
      {tokens.length === 0 ? (
        <div className="empty-card">
          <p>You have no tokens yet.</p>
          <Link to="/queues" className="btn btn-primary">Get a Token</Link>
        </div>
      ) : (
        <div className="token-list">
          {tokens.map((t) => (
            <Link key={t.id} to={`/token/${t.id}`} className="card token-card">
              <div className="token-number">#{t.tokenNumber}</div>
              <div className="token-info">
                <h3>{t.queueName}</h3>
                <span className="token-status" style={{ color: statusColor(t.status) }}>{t.status}</span>
                {t.status === 'WAITING' && (
                  <p>Position: {t.positionInQueue} · ~{t.estimatedWaitTimeMinutes} min wait</p>
                )}
              </div>
            </Link>
          ))}
        </div>
      )}
    </div>
  )
}
