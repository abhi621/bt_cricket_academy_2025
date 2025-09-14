# Cricket Academy Authentication API

This document describes the authentication and user approval flow implementation.

## API Endpoints

### 1. User Registration
**POST** `/auth/register`

Register new users (coaches/players require approval).

**Request Body:**
```json
{
  "username": "john_coach",
  "password": "coach123",
  "email": "john.coach@example.com",
  "role": "COACH"
}
```

**Response (Success):**
```json
{
  "message": "Registration successful. Your registration is pending approval. You will be notified once approved."
}
```

**Response (Error):**
```json
{
  "message": "Username already exists"
}
```

### 2. User Login
**POST** `/auth/login`

Authenticate users and return JWT token.

**Request Body:**
```json
{
  "username": "john_coach",
  "password": "coach123"
}
```

**Response (Success):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "username": "john_coach",
  "role": "COACH",
  "approved": true
}
```

**Response (Not Approved - 403):**
```json
{
  "message": "Your account is not approved yet. Please contact academy staff."
}
```

**Response (Invalid Credentials - 401):**
```json
{
  "message": "Invalid credentials"
}
```

### 3. Get Pending Approvals
**GET** `/auth/pending-approvals`

Get list of users awaiting approval (Admin/Support only).

**Headers:**
```
Authorization: Bearer <token>
```

**Response:**
```json
[
  {
    "id": 1,
    "username": "john_coach",
    "email": "john.coach@example.com",
    "role": "COACH",
    "active": false,
    "approved": false
  }
]
```

### 4. Approve User
**PATCH** `/auth/approve/{userId}`

Approve a pending user registration (Admin/Support only).

**Headers:**
```
Authorization: Bearer <token>
```

**Response:**
```json
{
  "id": 1,
  "username": "john_coach",
  "email": "john.coach@example.com",
  "role": "COACH",
  "active": true,
  "approved": true
}
```

## Frontend Routes

- `/login` - Login page
- `/register` - Self-registration page for coaches/players
- `/dashboard` - Role-based dashboard (requires authentication)
- `/approval-dashboard` - User approval dashboard (Admin/Support only)

## User Roles

- **ADMIN** - Can approve users, access all features
- **SUPPORT** - Can approve users, limited admin features
- **COACH** - Self-register with approval required
- **PLAYER** - Self-register with approval required

## Workflow

1. **Self-Registration**: Coach/Player fills registration form
2. **Pending State**: User account created with `approved=false`
3. **Admin Review**: Admin/Support sees pending registrations
4. **Approval**: Admin/Support approves user after fee collection
5. **Login**: User can now login with credentials

## Security

- Passwords are hashed using BCrypt
- JWT tokens expire after 1 hour
- Role-based access control for approval endpoints
- CORS enabled for frontend integration

## Testing

Use the provided Postman collection (`postman-collection.json`) to test the API endpoints.

1. Import the collection into Postman
2. Set the `baseUrl` variable to `http://localhost:8080`
3. Run the requests in order to test the complete workflow