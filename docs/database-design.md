\# SalaryHub – Database Design



\## 1. Overview



SalaryHub uses PostgreSQL to store employee information, salary revision history, and application user details.



The database contains three main tables:



1\. `employees`

2\. `salary\_records`

3\. `app\_users`



\## 2. Employees Table



The `employees` table stores employee information.



| Column | Data Type | Constraint / Description |

|---|---|---|

| id | BIGINT | Primary key, generated automatically |

| employee\_code | VARCHAR(20) | Required, unique |

| first\_name | VARCHAR(100) | Required |

| last\_name | VARCHAR(100) | Required |

| email | VARCHAR(150) | Required, unique |

| country | VARCHAR(100) | Required |

| department | VARCHAR(100) | Required |

| job\_title | VARCHAR(100) | Required |

| hire\_date | DATE | Required |

| employment\_status | VARCHAR(20) | Required, defaults to ACTIVE |

| created\_at | TIMESTAMP | Required, set during creation |

| updated\_at | TIMESTAMP | Required, updated during modification |



\### Important constraints



\- Employee code must be unique.

\- Email must be unique.

\- Required employee fields cannot be null.

\- Creation and update timestamps are maintained by the application.



\## 3. Salary Records Table



The `salary\_records` table stores employee salary information and revision history.



| Column | Data Type | Constraint / Description |

|---|---|---|

| id | BIGINT | Primary key, generated automatically |

| employee\_id | BIGINT | Required foreign key to employees |

| amount | NUMERIC(19,2) | Required salary amount |

| currency | VARCHAR(3) | Required currency code |

| effective\_date | DATE | Required salary effective date |

| notes | VARCHAR(500) | Optional notes |

| created\_at | TIMESTAMP | Required, set during creation |



\### Important constraints



\- Every salary record belongs to an employee.

\- Salary amount must be greater than zero through request validation.

\- Currency uses a three-letter code.

\- An employee cannot have multiple salary records with the same effective date.

\- An index is created on `employee\_id` and `effective\_date` to support salary history queries.



\## 4. Application Users Table



The `app\_users` table stores application authentication information.



| Column | Data Type | Constraint / Description |

|---|---|---|

| id | BIGINT | Primary key, generated automatically |

| username | VARCHAR(100) | Required, unique |

| password\_hash | VARCHAR(255) | Required, stores encoded password |

| role | VARCHAR(30) | Required, stored as an enum string |

| enabled | BOOLEAN | Required, defaults to true |

| created\_at | TIMESTAMP | Required, set during creation |



\### Roles



The application currently defines:



\- `HR\_MANAGER`



Passwords are stored as encoded hashes, not plain text.



\## 5. Entity Relationships



An employee can have multiple salary records.



Each salary record belongs to exactly one employee.



```text

+----------------------+

|      employees       |

+----------------------+

| id (PK)              |

| employee\_code        |

| first\_name           |

| last\_name            |

| email                |

| country              |

| department           |

| job\_title            |

| hire\_date            |

| employment\_status    |

| created\_at           |

| updated\_at           |

+----------------------+

&#x20;          |

&#x20;          | 1

&#x20;          |

&#x20;          | \*

+----------------------+

|    salary\_records    |

+----------------------+

| id (PK)              |

| employee\_id (FK)     |

| amount               |

| currency             |

| effective\_date       |

| notes                |

| created\_at           |

+----------------------+



+----------------------+

|       app\_users      |

+----------------------+

| id (PK)              |

| username             |

| password\_hash        |

| role                 |

| enabled              |

| created\_at           |

+----------------------+

```



The `app\_users` table stores authentication details separately from employee records.



\## 6. Salary Revision Design



Salary revisions are stored as separate records instead of overwriting an employee's previous salary.



For example:



| Employee | Amount | Currency | Effective Date |

|---|---:|---|---|

| EMP10001 | 60000 | INR | 2025-01-01 |

| EMP10001 | 70000 | INR | 2026-01-01 |

| EMP10001 | 75000 | INR | 2027-01-01 |



This approach preserves salary history.



The application determines the currently applicable salary using the effective date. A future-dated revision does not replace the current salary before its effective date.



\## 7. Currency Handling



Salary amounts are stored with their respective currency codes.



For example:

\- INR – Indian Rupee

\- USD – US Dollar

\- EUR – Euro



Salary analytics group results by currency. Amounts in different currencies are not combined into a single total.



\## 8. Sample Database Statistics



The hosted database was populated with fictional assessment data.



| Metric | Count |

|---|---:|

| Employees | 10,000 |

| Active employees | 9,500 |

| Inactive employees | 500 |

| Salary records | 10,000 |

| Application users | 1 |



\## 9. Database Technology



\- Database: PostgreSQL 18

\- ORM: Hibernate

\- Data access: Spring Data JPA

\- Hosted database: Neon PostgreSQL



The application uses environment variables for database connection configuration.



Database schema management is handled by the application's JPA configuration.



\---



\*\*Application:\*\* SalaryHub  

\*\*Database:\*\* PostgreSQL  

\*\*Design:\*\* Relational database

