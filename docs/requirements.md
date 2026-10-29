\# SalaryHub — Employee Salary Management



\## 1. Goal



Build a web-based application to help an organization manage salary information for 10,000 employees across multiple countries. The application will replace spreadsheet-based salary management and help HR understand how the organization pays its employees.



\## 2. Target User



The primary user is an HR Manager who needs to view, search, manage, and analyze employee and salary information.



\## 3. Features Included



\### Employee Management

\- View employees in a searchable and paginated table.

\- Add new employees and update existing employee information.

\- Filter employees by country, department, job title, and employment status.

\- View individual employee details.



\### Salary Management

\- Record salaries with currency and effective dates.

\- Update salaries while preserving previous salary records.

\- View an employee's salary history.

\- Validate salary values and required fields.



\### Salary Analytics

\- Display the total number of employees.

\- Show employee counts and salary summaries by country and department.

\- Display average salaries and salary distributions.

\- Display financial totals separately by currency.



\### Security and Quality

\- Provide login access for the HR Manager.

\- Validate requests and return clear error responses.

\- Use a relational database and server-side pagination.

\- Seed the database with 10,000 repeatable sample employee records.

\- Include automated tests for important business rules and API behavior.



\### Delivery

\- Build a complete frontend and backend.

\- Deploy the application so it can be accessed online.

\- Provide setup instructions, technical documentation, and a video demonstration.



\## 4. Features Deliberately Excluded



\- Actual payroll processing, tax calculations, and bank transfers.

\- Integration with external HR or payroll systems.

\- Automatic currency conversion.

\- Complex multi-level approval workflows.

\- Multiple permission levels beyond the initial HR Manager role.

\- Advanced salary forecasting and AI-based recommendations.



These features are excluded to keep the initial implementation focused on the main business requirements and achievable within the assessment timeline.



\## 5. Design Decisions



\- Use Angular for the frontend, Java with Spring Boot for the backend, and PostgreSQL for the database.

\- Use a modular monolith with separate employee, salary, analytics, and security modules.

\- Store monetary amounts using a suitable decimal database type.

\- Store the currency alongside each salary and report totals separately by currency.

\- Preserve salary history with effective dates.

\- Use server-side pagination and filtering.

\- Use Spring Security and JWT to protect the application.

\- Generate repeatable sample data to support consistent testing and demonstrations.



\## 6. Success Criteria



The application must support employee and salary management, preserve salary history, provide useful salary analytics, handle 10,000 seeded employees, pass meaningful automated tests, and be available through a live deployment.

