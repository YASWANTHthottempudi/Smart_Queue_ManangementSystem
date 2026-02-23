import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { api } from '../services/api'

interface Queue {
  id: number
  name: string
  department: string
}

export default function Queues() {
  const [queues, setQueues] = useState<Queue[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [creating, setCreating] = useState<number | null>(null)
  const navigate = useNavigate()

  useEffect(() => {
    api.get('/api/queues')
      .then((r) => setQueues(r.data))
      .catch((e) => setError(e.response?.data?.error || 'Failed to load queues'))
      .finally(() => setLoading(false))
  }, [])

  const getToken = (queueId: number) => {
    setCreating(queueId)
    api.post('/api/tokens', { queueId })
      .then((r) => navigate(`/token/${r.data.id}`))
      .catch((e) => setError(e.response?.data?.error || 'Failed to get token'))
      .finally(() => setCreating(null))
  }

  if (loading) return <div className="page"><div className="loading">Loading queues...</div></div>
  if (queues.length === 0) return <div className="page"><div className="empty">No queues available.</div></div>

  return (
    <div className="page">
      <h1>Get a Token</h1>
      <p className="page-sub">Select a queue to receive your digital token.</p>
      {error && <div className="error-msg">{error}</div>}
      <div className="queue-grid">
        {queues.map((q) => (
          <div key={q.id} className="card queue-card">
            <h3>{q.name}</h3>
            <p className="queue-dept">{q.department}</p>
            <button
              className="btn btn-primary"
              onClick={() => getToken(q.id)}
              disabled={creating !== null}
            >
              {creating === q.id ? 'Creating...' : 'Get Token'}
            </button>
          </div>
        ))}
      </div>
    </div>
  )
}
