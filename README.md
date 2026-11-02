# SalaryHub – Employee Salary Management System

SalaryHub is a web-based employee salary management application designed for organizations managing employees across multiple countries.

It allows HR Managers to manage employee information, maintain salary records, track salary revisions, and view salary analytics through a centralized application.

## Technology Stack

### Backend
- Java 21
- Spring Boot 4.1.2
- Spring Data JPA and Hibernate
- Spring Security
- JWT Authentication
- REST APIs
- Maven

### Frontend
- Angular 22
- TypeScript
- HTML and CSS

### Database
- PostgreSQL 18

### Deployment
- Backend: Render
- Frontend: Vercel
- Database: Neon PostgreSQL

## Main Features

### 1. Employee Management
- Create and update employee information.
- View employee details.
- Search employees.
- Filter by country and department.
- Pagination for employee records.

### 2. Salary Management
- Maintain employee salary records in their respective currencies.
- Add salary revisions with effective dates.
- View complete salary revision history.
- Support future-dated salary revisions.

### 3. Salary Records
- View salary records across all employees.
- Search salary records using employee information.
- Filter by country, department, and currency.
- Navigate to individual employee salary history.

### 4. Dashboard and Analytics
- View employee statistics.
- View salary analytics by country and department.
- Display salary information grouped by currency.

### 5. Authentication and Authorization
- HR Manager login.
- JWT-based authentication.
- Role-based access control.
- Protected frontend routes.
- Secure API access.

## Project Structure

```text
SalaryHub/
│
├── backend/
│   ├── src/
│   │   ├── main/
│   │   └── test/
│   ├── scripts/
│   │   └── seed.sql
│   ├── Dockerfile
│   ├── .dockerignore
│   ├── pom.xml
│   └── mvnw
│
├── frontend/
│   ├── src/
│   ├── public/
│   ├── package.json
│   ├── proxy.conf.json
│   └── vercel.json
│
├── docs/
│   ├── requirements.md
│   └── seed-data.md
│
└── README.md
```

## Database Design

SalaryHub uses PostgreSQL to store employee information, salary records, and application user details.

The main database tables are:

| Table | Purpose |
|---|---|
| `employees` | Stores employee information. |
| `salary_records` | Stores salary details and revision history. |
| `app_users` | Stores application user credentials and roles. |

### Salary Handling

- Each employee's salary is maintained in their local currency.
- Salary analytics are grouped by currency.
- Salary revisions are maintained with effective dates.
- Future-dated revisions do not replace the currently effective salary before their effective date.

## Sample Data

The application database contains 10,000 fictional employee records.

The seed script is available at:

`backend/scripts/seed.sql`

Refer to `docs/seed-data.md` for additional information.

## Local Development Setup

### Prerequisites

Install the following:

- Java 21
- Node.js and npm
- PostgreSQL
- Git

### 1. Clone the repository

```bash
git clone https://github.com/Karthikeyan-R-Official/SalaryHub.git
cd SalaryHub
```

### 2. Backend Setup

Navigate to the backend directory:

```powershell
cd backend
```

Configure the required environment variables before starting the application:

| Variable | Purpose |
|---|---|
| `DB_URL` | PostgreSQL JDBC connection URL |
| `DB_USERNAME` | Database username |
| `DB_PASSWORD` | Database password |
| `HR_MANAGER_USERNAME` | HR Manager username |
| `HR_MANAGER_PASSWORD` | HR Manager password |
| `JWT_SECRET` | Secret used for JWT signing |

Start the backend:

```powershell
.\mvnw.cmd spring-boot:run
```

The backend runs on:

`http://localhost:8080`

### 3. Frontend Setup

Open a separate terminal and navigate to the frontend directory:

```powershell
cd frontend
```

Install dependencies:

```powershell
npm.cmd install
```

Start the Angular development server:

```powershell
npm.cmd start
```

The frontend runs on:

`http://localhost:4200`

The frontend development proxy forwards API requests to the local backend.

## API Overview

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/login` | HR Manager login |
| GET | `/api/health` | Application health check |
| GET | `/api/employees` | List employees |
| GET | `/api/employees/{id}` | Get employee details |
| POST | `/api/employees` | Create employee |
| PUT | `/api/employees/{id}` | Update employee |
| GET | `/api/employees/{id}/salaries` | Get employee salary history |
| POST | `/api/employees/{id}/salaries` | Add salary revision |
| GET | `/api/salaries` | List salary records |
| GET | `/api/analytics/overview` | Dashboard overview |
| GET | `/api/analytics/salaries/by-country` | Salary analytics by country |
| GET | `/api/analytics/salaries/by-department` | Salary analytics by department |

Protected endpoints require a valid JWT access token.

## Testing

### Backend Tests

Run the backend test suite:

```powershell
.\mvnw.cmd clean test
```

The backend tests cover service logic, repository operations, and security integration.

### Frontend Tests

Navigate to the frontend directory and run:

```powershell
npm.cmd test
```

## Production Deployment

SalaryHub is deployed using free-tier hosting services.

| Component | Platform |
|---|---|
| Frontend | Vercel |
| Backend | Render |
| Database | Neon PostgreSQL |

### Live Application

- **Frontend:** https://salaryhub-hr-management.vercel.app
- **Backend Health:** https://salaryhub-backend.onrender.com/api/health

The backend uses environment variables for database credentials and JWT configuration. Sensitive credentials are not stored in the Git repository.

## Future Improvements

- Add more detailed audit logging.
- Improve reporting and export capabilities.
- Add additional automated frontend tests.
- Introduce more advanced employee and salary analytics.

## Author

Developed as part of the Incubyte Engineering Assessment.