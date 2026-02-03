# Backend Setup Instructions

## Prerequisites

1. **Java 17 or higher** - Download from [Oracle](https://www.oracle.com/java/technologies/downloads/) or use [OpenJDK](https://openjdk.org/)
2. **Maven 3.6+** - Download from [Apache Maven](https://maven.apache.org/download.cgi)
3. **MySQL 8.0+** - Download from [MySQL](https://dev.mysql.com/downloads/mysql/)

## Installation Steps

### 1. Install Java
```bash
# Check Java version
java -version

# If not installed, install via Homebrew (macOS)
brew install openjdk@17

# Or download from Oracle/OpenJDK website
```

### 2. Install Maven
```bash
# Check Maven version
mvn -version

# If not installed, install via Homebrew (macOS)
brew install maven

# Or download from Apache Maven website
```

### 3. Setup MySQL Database
```bash
# Start MySQL service
brew services start mysql  # macOS
# or
sudo systemctl start mysql  # Linux

# Create database (or it will be auto-created on first run)
mysql -u root -p
CREATE DATABASE smart_queue_db;
```

### 4. Configure Database Connection
Edit `backend/src/main/resources/application.properties` and update:
```properties
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 5. Build and Run the Project
```bash
# Navigate to backend directory
cd /Users/yaswanththottempudi/Documents/Smart_Queue/backend

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## Project Structure

```
backend/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/queue/
│   │   │       ├── SmartQueueApplication.java
│   │   │       ├── controller/     # REST Controllers
│   │   │       ├── service/        # Business Logic
│   │   │       ├── repository/     # Data Access Layer
│   │   │       ├── model/          # Entity Classes
│   │   │       ├── config/         # Configuration Classes
│   │   │       ├── dto/            # Data Transfer Objects
│   │   │       └── exception/      # Exception Handlers
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-dev.properties
│   │       └── application-prod.properties
│   └── test/
│       └── java/
│           └── com/example/queue/
```

## Dependencies Included

- **Spring Boot 3.2.0** - Main framework
- **Spring Data JPA** - Database access
- **Spring Security** - Authentication & Authorization
- **Spring WebSocket** - Real-time updates
- **MySQL Connector** - Database driver
- **Lombok** - Reduce boilerplate code
- **JWT (jjwt 0.12.3)** - Token-based authentication
- **Spring Validation** - Input validation
- **Spring Boot DevTools** - Development tools

## Next Steps

1. Create entity models (User, Queue, Counter, Token)
2. Create repositories
3. Implement services
4. Create REST controllers
5. Configure Spring Security
6. Set up WebSocket for real-time updates

## Troubleshooting

### Java not found
- Ensure JAVA_HOME is set: `export JAVA_HOME=$(/usr/libexec/java_home)`
- Add to `~/.zshrc` or `~/.bash_profile`

### Maven not found
- Ensure Maven is in PATH
- Add to `~/.zshrc`: `export PATH="/usr/local/bin:$PATH"`

### Database connection issues
- Verify MySQL is running
- Check username/password in `backend/src/main/resources/application.properties`
- Ensure database exists or auto-create is enabled
