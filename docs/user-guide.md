\# SalaryHub – User Guide



\## 1. Introduction



SalaryHub is an employee salary management application designed for HR Managers.



It provides a centralized place to manage employee information, salary records, salary revisions, and salary analytics across multiple countries.



\## 2. Accessing the Application



Open the deployed application:



https://salaryhub-hr-management.vercel.app



Log in using the HR Manager account configured for the application.



After successful login, the dashboard will be displayed.



\## 3. Dashboard



The dashboard provides an overview of the organization's employee and salary information.



HR Managers can view:



\- Total number of employees.

\- Active employees.

\- Inactive employees.

\- Total salary records.

\- Salary breakdown by country.

\- Salary breakdown by department.



Salary analytics are grouped by currency. Different currencies are not added together.



\## 4. Employee Management



Open the Employees section from the application navigation.



The employee page allows HR Managers to:



\- View employee records.

\- Search for employees.

\- Filter employees by country and department.

\- Navigate through employee records using pagination.

\- View individual employee details.

\- Create new employee records.

\- Update existing employee information.



Employee information includes employee code, name, email, country, department, job title, hire date, and employment status.



\## 5. Salary Management



Open an employee's details and navigate to the salary section.



HR Managers can:



\- View the employee's salary records.

\- View salary revision history.

\- Add a new salary revision.

\- Specify the salary amount and currency.

\- Set the effective date of a salary revision.

\- Add optional notes.



Salary revisions are stored as separate records. Previous salary records are retained instead of being overwritten.



Future-dated salary revisions are supported.



\## 6. Global Salary Records



Open the Salary Records section.



This page provides a consolidated view of salary records across employees.



HR Managers can:



\- View salary records.

\- Search using employee information.

\- Filter by country.

\- Filter by department.

\- Filter by currency.

\- Navigate through records using pagination.



\## 7. Authentication and Logout



SalaryHub uses JWT-based authentication.



Protected application features require a valid access token. The application restricts access to authorized HR Managers.



Use the Logout option to end the current session and return to the login page.



\## 8. Important Notes



\- Salary amounts are maintained in their respective currencies.

\- Different currencies are not combined in analytics.

\- Salary history is retained with effective dates.

\- Do not share login credentials or sensitive authentication information.



\## 9. Application URL



Frontend: https://salaryhub-hr-management.vercel.app



Backend health check: https://salaryhub-backend.onrender.com/api/health

