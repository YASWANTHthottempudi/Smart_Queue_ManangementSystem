import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { api } from '../services/api'
import { BarChart, Bar, XAxis, YAxis, ResponsiveContainer, PieChart, Pie, Cell, Legend } from 'recharts'

interface StatsResponse {
  totalTokens: number
  waitingTokens: number
  servingTokens: number
  servedTokens: number
  activeCounters: number
  tokensByQueue: Record<string, number>
  averageWaitTimeByQueue: Record<string, number>
}

const COLORS = ['#00d4aa', '#64748b', '#fbbf24', '#34d399']

export default function AdminDashboard() {
  const [stats, setStats] = useState<StatsResponse | null>(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    api.get('/api/admin/stats')
      .then((r) => setStats(r.data))
      .finally(() => setLoading(false))
  }, [])

  if (loading) return <div className="page"><div className="loading">Loading stats...</div></div>
  if (!stats) return <div className="page"><div className="error-msg">Failed to load stats</div></div>

  const pieData = [
    { name: 'Waiting', value: stats.waitingTokens, color: COLORS[2] },
    { name: 'Serving', value: stats.servingTokens, color: COLORS[0] },
    { name: 'Served', value: stats.servedTokens, color: COLORS[3] },
  ].filter((d) => d.value > 0)

  const barData = Object.entries(stats.tokensByQueue).map(([name, value]) => ({ name, count: value }))

  return (
    <div className="page">
      <h1>Admin Dashboard</h1>
      <p className="page-sub">Overview of queue statistics.</p>
      <Link to="/admin/counters" className="admin-link">Manage Counters →</Link>
      <div className="stats-grid">
        <div className="stat-card">
          <span className="stat-value">{stats.totalTokens}</span>
          <span className="stat-label">Total Tokens</span>
        </div>
        <div className="stat-card">
          <span className="stat-value">{stats.waitingTokens}</span>
          <span className="stat-label">Waiting</span>
        </div>
        <div className="stat-card">
          <span className="stat-value">{stats.servingTokens}</span>
          <span className="stat-label">Serving</span>
        </div>
        <div className="stat-card">
          <span className="stat-value">{stats.servedTokens}</span>
          <span className="stat-label">Served</span>
        </div>
        <div className="stat-card">
          <span className="stat-value">{stats.activeCounters}</span>
          <span className="stat-label">Active Counters</span>
        </div>
      </div>
      <div className="charts-row">
        {pieData.length > 0 && (
          <div className="chart-card">
            <h3>Token Status</h3>
            <ResponsiveContainer width="100%" height={200}>
              <PieChart>
                <Pie data={pieData} dataKey="value" nameKey="name" cx="50%" cy="50%" outerRadius={70}>
                  {pieData.map((_, i) => <Cell key={i} fill={pieData[i].color} />)}
                </Pie>
                <Legend />
              </PieChart>
            </ResponsiveContainer>
          </div>
        )}
        {barData.length > 0 && (
          <div className="chart-card">
            <h3>Tokens by Queue</h3>
            <ResponsiveContainer width="100%" height={200}>
              <BarChart data={barData}>
                <XAxis dataKey="name" />
                <YAxis />
                <Bar dataKey="count" fill={COLORS[0]} />
              </BarChart>
            </ResponsiveContainer>
          </div>
        )}
      </div>
    </div>
  )
}
