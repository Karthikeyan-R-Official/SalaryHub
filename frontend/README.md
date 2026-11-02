# SalaryHub – Frontend

SalaryHub is an employee salary management application built using Angular. It provides an interface for HR Managers to manage employee information, salary records, salary revisions, and salary analytics.

## Technology Stack

- Angular 22
- TypeScript
- HTML and CSS
- Angular Router
- Angular HTTP Client
- JWT-based authentication

## Features

- HR Manager login and logout
- Protected application routes
- Dashboard with employee and salary statistics
- Employee listing, search, filtering, and pagination
- Create and update employee information
- View employee details
- Manage employee salary records
- Add salary revisions with effective dates
- View salary revision history
- Global salary records across employees
- Filter salary records by country, department, and currency
- Dashboard salary analytics grouped by country and department

## Project Structure

```text
frontend/
├── public/
├── src/
│   ├── app/
│   │   ├── components/
│   │   ├── services/
│   │   ├── guards/
│   │   └── ...
│   ├── index.html
│   └── styles.css
├── angular.json
├── package.json
├── proxy.conf.json
└── vercel.json
```

## Prerequisites

Install the following before running the application:

- Node.js
- npm

## Local Development Setup

### 1. Navigate to the frontend directory

```bash
cd frontend
```

### 2. Install dependencies

```bash
npm install
```

### 3. Start the development server

```bash
npm start
```

The application will be available at:

```text
http://localhost:4200
```

The development server uses `proxy.conf.json` to forward `/api` requests to the local Spring Boot backend.

Make sure the backend is running before testing application features that require API access.

## Build

To create a production build:

```bash
npm run build
```

The generated production files are stored in:

```text
dist/frontend/browser/
```

## Testing

To run the frontend unit tests:

```bash
npm test
```

## Backend Integration

The frontend communicates with the Spring Boot backend through REST APIs.

During local development, API requests are forwarded through the Angular development proxy.

In production, Vercel rewrites `/api` requests to the deployed backend.

## Deployment

The frontend is deployed using Vercel.

- **Frontend:** https://salaryhub-hr-management.vercel.app
- **Backend:** https://salaryhub-backend.onrender.com

The application uses the deployed backend APIs for authentication, employee management, salary management, and analytics.

## Important Notes

- Authentication uses JWT tokens.
- Protected API requests include the JWT token in the Authorization header.
- The application uses relative API paths, allowing the same frontend code to work in local and production environments.
- Do not commit passwords, JWT secrets, database credentials, or other sensitive information.

