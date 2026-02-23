import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { api } from '../services/api'

interface CounterResponse {
  id: number
  counterNumber: number
  queueId: number
  status: string
  currentTokenId: number | null
  currentTokenNumber: number | null
}

export default function AdminCounters() {
  const [counters, setCounters] = useState<CounterResponse[]>([])
  const [loading, setLoading] = useState(true)
  const [actioning, setActioning] = useState<number | null>(null)
  const [error, setError] = useState('')

  const load = () => {
    api.get('/api/counters')
      .then((r) => setCounters(r.data))
      .catch((e) => setError(e.response?.data?.error || 'Failed to load'))
      .finally(() => setLoading(false))
  }

  useEffect(() => load(), [])

  const serveNext = (counterId: number) => {
    setActioning(counterId)
    setError('')
    api.post(`/api/counters/${counterId}/serve-next`)
      .then(() => load())
      .catch((e) => setError(e.response?.data?.error || 'Failed'))
      .finally(() => setActioning(null))
  }

  const completeToken = (counterId: number, tokenId: number) => {
    setActioning(counterId)
    setError('')
    api.post(`/api/counters/${counterId}/complete/${tokenId}`)
      .then(() => load())
      .catch((e) => setError(e.response?.data?.error || 'Failed'))
      .finally(() => setActioning(null))
  }

  if (loading) return <div className="page"><div className="loading">Loading counters...</div></div>

  return (
    <div className="page">
      <h1>Manage Counters</h1>
      <p className="page-sub">Serve next token or mark token as complete.</p>
      <Link to="/admin" className="admin-link">← Dashboard</Link>
      {error && <div className="error-msg">{error}</div>}
      <div className="counter-grid">
        {counters.map((c) => (
          <div key={c.id} className="card counter-card">
            <h3>Counter #{c.counterNumber}</h3>
            <span className={`counter-status counter-${c.status.toLowerCase()}`}>{c.status}</span>
            {c.currentTokenNumber != null ? (
              <div className="counter-current">
                <p>Serving token: <strong>#{c.currentTokenNumber}</strong></p>
                <button
                  className="btn btn-secondary"
                  onClick={() => c.currentTokenId && completeToken(c.id, c.currentTokenId)}
                  disabled={actioning !== null}
                >
                  {actioning === c.id ? '...' : 'Complete'}
                </button>
              </div>
            ) : (
              <button
                className="btn btn-primary"
                onClick={() => serveNext(c.id)}
                disabled={actioning !== null || c.status !== 'AVAILABLE'}
              >
                {actioning === c.id ? '...' : 'Serve Next'}
              </button>
            )}
          </div>
        ))}
      </div>
    </div>
  )
}
