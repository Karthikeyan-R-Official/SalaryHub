\# SalaryHub – API Documentation



\## 1. Overview



SalaryHub exposes REST APIs through the Spring Boot backend.



\*\*Base URL (production):\*\*

`https://salaryhub-backend.onrender.com`



\*\*API prefix:\*\* `/api`



\*\*Content type:\*\* `application/json`



Protected endpoints require a JWT access token.



Use this header for authenticated requests:



`Authorization: Bearer <accessToken>`



\## 2. Authentication APIs



\### Login



\*\*POST\*\* `/api/auth/login`



Authenticates the HR Manager and returns a JWT access token.



Request:

```json

{

&#x20; "username": "hrmanager",

&#x20; "password": "your-password"

}

```



Response:

```json

{

&#x20; "accessToken": "<jwt-token>",

&#x20; "tokenType": "Bearer",

&#x20; "expiresIn": 3600,

&#x20; "username": "hrmanager",

&#x20; "role": "HR\_MANAGER"

}

```



Both username and password are required.



\## 3. Employee APIs



\### Get employees



\*\*GET\*\* `/api/employees`



Returns employees with pagination and optional search and filters.



Query parameters:



| Parameter | Required | Default | Description |

|---|---|---|---|

| search | No | — | Search employee records |

| country | No | — | Filter by country |

| department | No | — | Filter by department |

| page | No | 0 | Page number |

| size | No | 20 | Number of records per page |



Example:



`GET /api/employees?page=0\&size=20\&country=India`



\### Get employee by ID



\*\*GET\*\* `/api/employees/{id}`



Returns one employee using the employee ID.



\### Create employee



\*\*POST\*\* `/api/employees`



Creates a new employee.



Request:

```json

{

&#x20; "employeeCode": "EMP10001",

&#x20; "firstName": "John",

&#x20; "lastName": "Smith",

&#x20; "email": "john@example.com",

&#x20; "country": "India",

&#x20; "department": "Engineering",

&#x20; "jobTitle": "Software Engineer",

&#x20; "hireDate": "2025-01-15",

&#x20; "employmentStatus": "ACTIVE"

}

```



Returns the created employee with HTTP 201 Created.



\### Update employee



\*\*PUT\*\* `/api/employees/{id}`



Updates an existing employee using the employee ID.



Uses the same request fields as employee creation.



\### Employee validation



\- Employee code is required and limited to 20 characters.

\- First and last names are required and limited to 100 characters.

\- Email is required and must be valid.

\- Country, department, and job title are required.

\- Hire date is required.

\- Employment status is limited to 20 characters.



\## 4. Salary APIs



\### Add salary revision



\*\*POST\*\* `/api/employees/{employeeId}/salaries`



Creates a salary record for an employee.



Request:

```json

{

&#x20; "amount": 75000.00,

&#x20; "currency": "INR",

&#x20; "effectiveDate": "2026-01-01",

&#x20; "notes": "Annual salary revision"

}

```



Returns the created salary record with HTTP 201 Created.



\### Get salary history



\*\*GET\*\* `/api/employees/{employeeId}/salaries`



Returns the salary revision history for an employee.



\### Salary validation



\- Salary amount is required and must be greater than zero.

\- Amount supports up to 17 integer digits and 2 decimal places.

\- Currency is required and must contain three letters.

\- Effective date is required.

\- Notes can contain up to 500 characters.

\- An employee must exist before a salary record can be created.

\- Duplicate salary records for the same employee and effective date are prevented.



\## 5. Global Salary Records API



\### Get all salary records



\*\*GET\*\* `/api/salaries`



Returns salary records with employee details, search, filters, and pagination.



Query parameters:



| Parameter | Required | Default | Description |

|---|---|---|---|

| search | No | — | Search salary records by employee information |

| country | No | — | Filter by country |

| department | No | — | Filter by department |

| currency | No | — | Filter by currency |

| page | No | 0 | Page number |

| size | No | 20 | Number of records per page |



Example:



`GET /api/salaries?country=India\&currency=INR\&page=0\&size=20`



\## 6. Analytics APIs



All analytics endpoints use the `/api/analytics` prefix.



\### Overview



\*\*GET\*\* `/api/analytics/overview`



Returns summary information.



Response:

```json

{

&#x20; "totalEmployees": 10000,

&#x20; "activeEmployees": 9500,

&#x20; "inactiveEmployees": 500,

&#x20; "totalSalaryRecords": 10000

}

```



\### Salary breakdown by country



\*\*GET\*\* `/api/analytics/salaries/by-country`



Returns salary breakdown grouped by country and currency.



\### Salary breakdown by department



\*\*GET\*\* `/api/analytics/salaries/by-department`



Returns salary breakdown grouped by department and currency.



The breakdown response contains:



```json

{

&#x20; "groupName": "India",

&#x20; "currency": "INR",

&#x20; "employeeCount": 100,

&#x20; "averageSalary": 75000.00

}

```



The group name represents the country or department, depending on the endpoint.



Different currencies are reported separately and are not added together.



\## 7. Health API



\*\*GET\*\* `/api/health`



Checks whether the backend application is running.



Production URL:



`https://salaryhub-backend.onrender.com/api/health`



Example response:

```json

{

&#x20; "application": "SalaryHub",

&#x20; "status": "UP"

}

```



This endpoint is available without login.



\## 8. HTTP Status Codes



| Status | Meaning |

|---|---|

| 200 | Request completed successfully |

| 201 | Resource created successfully |

| 400 | Invalid request or validation failure |

| 401 | Authentication required or invalid token |

| 403 | Access forbidden |

| 404 | Requested resource not found |

| 500 | Unexpected server error |



\## 9. Pagination



Employee and global salary record APIs return paginated results using Spring Data's `Page` response structure.



The response includes content and pagination information such as page number, page size, total elements, and total pages.



\## 10. Security



\- Login is handled through `/api/auth/login`.

\- Successful authentication returns a signed JWT.

\- Protected API requests require the bearer token.

\- Spring Security validates tokens before allowing access.

\- HR Manager role-based authorization is applied to protected application operations.



\---



\*\*Application:\*\* SalaryHub  

\*\*API Type:\*\* REST  

\*\*Backend:\*\* Java 21 and Spring Boot

