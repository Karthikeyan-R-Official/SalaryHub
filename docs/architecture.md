# SalaryHub – Architecture and Design



## 1. Overview



SalaryHub is an employee salary management application built using Angular, Spring Boot, and PostgreSQL.



The application follows a frontend-backend architecture. The Angular frontend communicates with the Spring Boot backend through REST APIs. The backend handles business logic, authentication, validation, and database operations.



The application uses a layered backend structure to separate responsibilities and make the code easier to maintain.



## 2. Technology Stack



| Layer | Technology |

|---|---|

| Frontend | Angular 22 |

| Backend | Java 21, Spring Boot 4.1.2 |

| API Communication | REST APIs |

| Security | Spring Security, JWT |

| ORM | Spring Data JPA, Hibernate |

| Database | PostgreSQL |

| Build Tool | Maven |

| Frontend Hosting | Vercel |

| Backend Hosting | Render |

| Database Hosting | Neon |



## 3. High-Level Architecture



```text

&#x20;                 HR Manager

&#x20;                     |

&#x20;                     v

&#x20;              Angular Frontend

&#x20;                 (Vercel)

&#x20;                     |

&#x20;                     | HTTP / REST

&#x20;                     |

&#x20;                     v

&#x20;               Spring Boot

&#x20;                 Backend

&#x20;                 (Render)

&#x20;                     |

&#x20;         +-----------+-----------+

&#x20;         |           |           |

&#x20;     Controllers  Security   Validation

&#x20;         |

&#x20;         v

&#x20;      Services

&#x20;         |

&#x20;         v

&#x20;     Repositories

&#x20;         |

&#x20;         v

&#x20;      Hibernate

&#x20;         |

&#x20;         v

&#x20;     PostgreSQL

&#x20;        (Neon)

```



### Component Responsibilities



**Angular Frontend**

\- Displays application pages and forms.

\- Handles user interactions.

\- Communicates with backend REST APIs.

\- Manages login and logout.

\- Attaches JWT tokens to protected API requests.



**Spring Boot Backend**

\- Exposes REST endpoints.

\- Processes business operations.

\- Validates incoming requests.

\- Handles authentication and authorization.

\- Communicates with PostgreSQL.



**PostgreSQL Database**

\- Stores employee information.

\- Stores salary records and revision history.

\- Stores application user information.



## 4. Backend Architecture



The backend follows a layered architecture.



### 4.1 Controller Layer



The controller layer receives HTTP requests and returns HTTP responses.



Responsibilities:

\- Define REST API endpoints.

\- Accept request data.

\- Call the appropriate service methods.

\- Return responses with suitable HTTP status codes.



### 4.2 Service Layer



The service layer contains the application's business logic.



Responsibilities:

\- Process employee operations.

\- Manage salary revisions.

\- Validate business rules.

\- Handle effective dates.

\- Coordinate repository operations.

\- Maintain transaction boundaries.



### 4.3 Repository Layer



The repository layer communicates with the database.



Spring Data JPA is used to perform database operations without writing repetitive SQL for standard CRUD operations.



Responsibilities:

\- Save employee and salary records.

\- Retrieve records.

\- Execute custom queries.

\- Support filtering and pagination.



### 4.4 Entity Layer



Entities represent database tables.



The main entities represent:

\- Employees

\- Salary records

\- Application users



### 4.5 DTO Layer



Data Transfer Objects are used to transfer data between the frontend and backend.



They help separate API request and response structures from database entity structures.



### 4.6 Security Layer



The security layer manages:

\- User authentication.

\- JWT token generation and validation.

\- Authorization.

\- Access restrictions for protected endpoints.



## 5. Frontend Architecture



The frontend is developed using Angular.



Its main responsibilities are:



\- Displaying dashboard information.

\- Managing employee records.

\- Managing salary records.

\- Communicating with backend APIs.

\- Handling authentication state.

\- Protecting application routes.



### Authentication Components



**Authentication Service**

\- Handles login and logout.

\- Maintains authentication information in session storage.



**HTTP Interceptor**

\- Adds the JWT token to protected API requests.

\- Handles unauthorized responses.



**Route Guard**

\- Prevents unauthenticated users from accessing protected application pages.



## 6. Request Flow



A typical employee request follows this sequence:



1\. HR Manager opens the employee page.

2\. Angular sends an HTTP request to the backend.

3\. The HTTP interceptor attaches the JWT token.

4\. Spring Security validates the token.

5\. The request reaches the employee controller.

6\. The controller calls the employee service.

7\. The service executes business logic.

8\. The repository retrieves the required data.

9\. Hibernate communicates with PostgreSQL.

10\. The backend returns the response.

11\. Angular displays the result.



```text

HR Manager

&#x20;   |

&#x20;   v

Angular Component

&#x20;   |

&#x20;   v

HTTP Service

&#x20;   |

&#x20;   v

HTTP Interceptor

&#x20;   |

&#x20;   v

Spring Security

&#x20;   |

&#x20;   v

Controller

&#x20;   |

&#x20;   v

Service

&#x20;   |

&#x20;   v

Repository

&#x20;   |

&#x20;   v

PostgreSQL

&#x20;   |

&#x20;   v

Response to Angular

```



## 7. Authentication and Authorization Flow



SalaryHub uses JWT-based authentication.



The authentication process works as follows:



1\. HR Manager enters the username and password.

2\. Angular sends the login request to the backend.

3\. Spring Security authenticates the supplied credentials.

4\. The backend generates a signed JWT access token.

5\. The token is returned to the frontend.

6\. Angular stores the authentication information in session storage.

7\. The HTTP interceptor attaches the token to subsequent protected API requests.

8\. Spring Security validates the token before allowing access.



The application uses role-based authorization for HR Manager operations.



The JWT signing secret is configured through an environment variable and is not stored in source control.



## 8. Database Design



SalaryHub uses PostgreSQL.



The main database tables are:



| Table | Responsibility |

|---|---|

| `employees` | Stores employee information. |

| `salary\_records` | Stores employee salary records and revision history. |

| `app\_users` | Stores application user credentials and roles. |



### Employee and Salary Relationship



One employee can have multiple salary records.



Each salary record belongs to one employee.



```text

employees

&#x20;   |

&#x20;   | One-to-Many

&#x20;   |

&#x20;   v

salary\_records

```



### Salary Revision Handling



Salary records contain effective dates.



The application uses effective dates to determine which salary is currently applicable.



Future-dated salary revisions are maintained without replacing the currently effective salary prematurely.



### Currency Handling



Employees may belong to different countries and have different salary currencies.



Salary analytics are grouped by currency to avoid combining amounts that represent different currencies.



## 9. Validation and Error Handling



The application uses request validation and centralized exception handling.



Important practices include:



\- Validate incoming request data.

\- Check whether employee records exist.

\- Prevent duplicate salary revisions for the same employee and effective date.

\- Return suitable HTTP status codes.

\- Handle unexpected application errors consistently.



## 10. Pagination and Search



Pagination is used when retrieving employee and salary records.



This avoids loading all records into the frontend at once.



Search and filtering allow HR Managers to locate records using relevant employee information and business fields.



## 11. Deployment Architecture



SalaryHub is deployed using three hosting services.



| Component | Hosting Platform |

|---|---|

| Angular Frontend | Vercel |

| Spring Boot Backend | Render |

| PostgreSQL Database | Neon |



### Production Request Flow



1\. The HR Manager accesses the Angular application through Vercel.

2\. The frontend sends API requests using relative `/api` paths.

3\. Vercel rewrites API requests to the Render backend.

4\. The Spring Boot backend processes the requests.

5\. The backend accesses PostgreSQL hosted on Neon.

6\. The response is returned to the frontend.



Environment variables are used for database credentials, HR Manager configuration, and JWT signing configuration.



Sensitive configuration values are not committed to GitHub.



## 12. Design Decisions



### Layered Architecture



Controllers, services, and repositories have separate responsibilities. This improves maintainability and makes business logic easier to test.



### Salary Revision History



Instead of simply replacing an employee's salary, salary changes are stored as separate records with effective dates. This preserves salary history.



### Local Currency



Salary is maintained in the employee's local currency. Analytics are grouped by currency to avoid misleading totals.



### Pagination



Pagination reduces unnecessary database retrieval and frontend processing when handling large employee and salary datasets.



### JWT Authentication



JWT provides stateless authentication for protected REST APIs.



### Modular Application



The application uses a modular layered structure within a single Spring Boot deployment. This keeps deployment and maintenance manageable without introducing unnecessary distributed-system complexity.



\---



**Document:** SalaryHub Architecture and Design  

**Application:** Employee Salary Management System
