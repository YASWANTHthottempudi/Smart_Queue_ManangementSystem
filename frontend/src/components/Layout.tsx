import { Outlet } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import Nav from './Nav'

export default function Layout() {
  const { user } = useAuth()

  return (
    <div className="layout">
      <Nav user={user} />
      <main className="main">
        <Outlet />
      </main>
    </div>
  )
}
