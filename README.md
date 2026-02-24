# Smart Queue

A queue management system for places like hospitals, banks, or service centers. Instead of standing in line, users get a digital token and can track their position from their phone or a screen. Staff can manage counters and serve customers in order.

## What it does

**For users:** Pick a queue, get a token, and see your position and estimated wait time. When it's your turn, you'll know.

**For admins:** View stats, manage counters, call the next person, and mark people as served when done.

## Tech stack

- **Backend:** Spring Boot, MySQL, JWT auth
- **Frontend:** React, TypeScript, Vite

## How to run it

You'll need Java 17+ and MySQL running locally.

**1. Start the backend**

```bash
cd backend
./mvnw spring-boot:run
```

Make sure MySQL has a database (it'll auto-create `smart_queue_db` on first run). Default credentials in `application.properties` are `root`/`root` — change those if needed.

**2. Start the frontend**

```bash
cd frontend
npm install
npm run dev
```


**3. Log in**

Seed data creates two accounts:

- Admin: `admin@smartqueue.com` / `admin123`
- User: `user@smartqueue.com` / `user123`

There's also a "General" queue with 2 counters you can use right away.

## Project structure

```
Smart_Queue/
├── backend/     # Spring Boot REST API
└── frontend/    # React app
```
