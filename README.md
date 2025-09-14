# Cricket Academy Management System

A production-ready, microservices-based Cricket Academy Management system with modern frontend and comprehensive features.

## Tech Stack

### Backend
- **Java 17** with **Spring Boot 3+**
- **Spring Cloud** (Eureka Service Discovery, API Gateway)
- **Spring Security** with JWT authentication
- **Spring Data JPA** with MySQL
- **Docker & Docker Compose** for containerization

### Frontend
- **Vue.js 3** with Composition API
- **Vite** for build tooling
- **Tailwind CSS** for styling
- **Pinia** for state management
- **Vue Router** with navigation guards

## Features

### 🏏 Core Functionality
- **Role-based Authentication** (Admin, Support, Coach, Player)
- **User Approval Workflow** for Coach/Player registrations
- **Fee Management** with payment tracking and reporting
- **Dashboard System** with role-specific views
- **Responsive Design** with stadium theme

### 🔐 Security
- JWT-based authentication with role-based access control
- Route guards for frontend protection
- Secure API endpoints with authorization
- User approval workflow for new registrations

### 📊 Dashboard Features
- **Admin Dashboard**: Complete system management, user approval, fee oversight
- **Support Dashboard**: Registration approvals, fee collection, attendance tracking
- **Coach Dashboard**: Batch management, attendance marking, player feedback
- **Player Dashboard**: Schedule viewing, fee payments, performance tracking

## Modules

- **gateway-service**: API Gateway with global CORS and OpenAPI/Swagger
- **discovery-service**: Eureka Service Registry
- **auth-service**: JWT authentication, user management, role-based access
- **player-service**: Player CRUD and attendance tracking
- **coach-service**: Coach CRUD and batch mapping
- **batch-service**: Batch creation, scheduling, and management
- **fee-service**: Comprehensive fee management, payment tracking, reporting
- **notification-service**: Email/SMS notifications
- **frontend**: Vue.js 3 SPA with role-based dashboards

## Running the Application

### Prerequisites
- **Java 17** or higher
- **Node.js 18** or higher
- **MySQL 8** or higher
- **Docker & Docker Compose** (optional)

### Option 1: Docker Compose (Recommended)

1. **Clone the repository:**
   ```bash
   git clone <repository-url>
   cd bt_cricket_academy_2025
   ```

2. **Build all services:**
   ```bash
   mvn clean package -DskipTests
   ```

3. **Start with Docker Compose:**
   ```bash
   docker-compose up --build
   ```

4. **Access the application:**
   - **Frontend**: [http://localhost:5173](http://localhost:5173)
   - **API Gateway**: [http://localhost:8080](http://localhost:8080)
   - **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
   - **Eureka Dashboard**: [http://localhost:8761](http://localhost:8761)

### Option 2: Local Development

1. **Start MySQL Database:**
   ```bash
   # Using Docker
   docker run --name cricket-mysql -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=cricket_academy -p 3306:3306 -d mysql:8
   ```

2. **Start Backend Services (in separate terminals):**
   ```bash
   # Discovery Service
   cd discovery-service && mvn spring-boot:run

   # Gateway Service
   cd gateway-service && mvn spring-boot:run

   # Auth Service
   cd auth-service && mvn spring-boot:run

   # Fee Service
   cd fee-service && mvn spring-boot:run

   # Other services as needed...
   ```

3. **Start Frontend:**
   ```bash
   cd frontend
   npm install
   npm run dev
   ```

## Environment Variables

Configure the following environment variables for production:

### Database Configuration
- `DB_HOST`: Database host (default: localhost)
- `DB_PORT`: Database port (default: 3306)
- `DB_USER`: Database username (default: root)
- `DB_PASSWORD`: Database password (default: root)

### Service Discovery
- `EUREKA_HOST`: Eureka server host (default: localhost)
- `EUREKA_PORT`: Eureka server port (default: 8761)

### Security
- `JWT_SECRET`: JWT signing secret (required for production)

### Notifications
- `MAIL_HOST`: Email server host
- `MAIL_PORT`: Email server port
- `MAIL_USER`: Email username
- `MAIL_PASSWORD`: Email password

## API Endpoints

### Authentication Service
- `POST /auth/login` - User login
- `POST /auth/register` - User registration
- `PATCH /auth/approve/{userId}` - Approve user (Admin/Support)
- `GET /auth/pending-approvals` - Get pending approvals

### Fee Service
- `GET /fees` - Get all fees (with filtering)
- `POST /fees` - Create fee record
- `PATCH /fees/{id}/pay` - Mark fee as paid
- `GET /fees/summary` - Get fee summary
- `GET /fees/my-fees` - Get player's fees

### Role Impersonation
- `GET /auth/impersonate?role=ROLE_X` - Switch roles (for testing)

## User Roles & Permissions

### Admin
- Complete system access
- User management and approval
- Fee management and reporting
- System configuration

### Support
- User registration approval
- Fee collection and tracking
- Attendance management
- Customer support tasks

### Coach
- Batch management
- Attendance marking
- Player feedback
- Schedule viewing

### Player
- View training schedule
- Fee payment
- Attendance history
- Performance tracking

## Development Guidelines

### Frontend Development
```bash
cd frontend

# Install dependencies
npm install

# Start development server
npm run dev

# Build for production
npm run build

# Preview production build
npm run serve
```

### Backend Development
```bash
# Build all services
mvn clean package

# Run specific service
cd <service-name>
mvn spring-boot:run

# Run tests
mvn test
```

---

**Built with ❤️ for BT Cricket Academy**
