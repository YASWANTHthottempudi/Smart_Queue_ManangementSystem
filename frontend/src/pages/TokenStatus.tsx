import { useEffect, useState } from 'react'
import { useParams, Link } from 'react-router-dom'
import { api } from '../services/api'

interface StatusData {
  tokenId: number
  tokenNumber: number
  status: string
  positionInQueue: number
  estimatedWaitTimeMinutes: number
  currentServingToken: number | null
}

export default function TokenStatus() {
  const { tokenId } = useParams<{ tokenId: string }>()
  const [data, setData] = useState<StatusData | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    if (!tokenId) return
    const fetchStatus = () => {
      api.get(`/api/queues/status/${tokenId}`)
        .then((r) => setData(r.data))
        .catch((e) => setError(e.response?.data?.error || 'Failed to load status'))
        .finally(() => setLoading(false))
    }
    fetchStatus()
    const id = setInterval(fetchStatus, 5000)
    return () => clearInterval(id)
  }, [tokenId])

  if (loading && !data) return <div className="page"><div className="loading">Loading...</div></div>
  if (error) return <div className="page"><div className="error-msg">{error}</div><Link to="/my-tokens">Back to My Tokens</Link></div>
  if (!data) return null

  const statusColor = data.status === 'SERVED' ? 'var(--success)' : data.status === 'SERVING' ? 'var(--accent)' : 'var(--warning)'

  return (
    <div className="page">
      <Link to="/my-tokens" className="back-link">← My Tokens</Link>
      <div className="token-status-card">
        <div className="token-display">#{data.tokenNumber}</div>
        <span className="token-badge" style={{ background: statusColor }}>{data.status}</span>
        {data.status === 'WAITING' && (
          <>
            <p>Position in queue: <strong>{data.positionInQueue}</strong></p>
            <p>Est. wait: <strong>~{data.estimatedWaitTimeMinutes} min</strong></p>
            {data.currentServingToken != null && (
              <p className="current-serving">Currently serving: #{data.currentServingToken}</p>
            )}
          </>
        )}
        {data.status === 'SERVING' && <p>You're being served. Please proceed to the counter.</p>}
        {data.status === 'SERVED' && <p>Thank you! Your turn is complete.</p>}
      </div>
    </div>
  )
}
