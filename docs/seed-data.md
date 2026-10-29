\# SalaryHub — Seed Data Implementation



\## 1. Purpose



SalaryHub is designed for an organization with 10,000 employees across multiple countries. Manually creating this many employees would take too much time.



We created a PostgreSQL seed script to generate fictional employees and sample salary records automatically.



\## 2. File Created



`backend/scripts/seed.sql`



This file contains SQL statements that generate employees and salary records.



\## 3. Employee Data Generation



The script generates 10,000 employees using PostgreSQL's `generate\_series()` function.



Each employee has:



\- A unique employee code.

\- A first name and last name.

\- A unique email address.

\- A country.

\- A department.

\- A job title.

\- A hire date.

\- An employment status.

\- Creation and update timestamps.



The employee codes begin with `SEED-`, followed by a five-digit number. For example, `SEED-00001`.



This naming convention makes the generated employees easy to identify and distinguish from manually created employees.



\## 4. Salary Data Generation



The script creates one salary record for each seeded employee.



Each record contains the employee ID, salary amount, currency, effective date, notes, and creation timestamp.



The currency is selected based on the employee's country. For example, employees in India receive sample records in INR, while employees in the USA receive sample records in USD.



These records are fictional and exist only for testing and demonstration.



\## 5. Duplicate Prevention



The script uses PostgreSQL's `ON CONFLICT DO NOTHING`.



This prevents the same employee code from being inserted twice. It also prevents a duplicate salary record for the same employee and effective date.



The script can therefore be run again without intentionally duplicating the seeded data.



\## 6. How to Run the Script



From the `backend` directory, run:



`psql -U postgres -h localhost -d salaryhub -f "./scripts/seed.sql"`



The command connects to the SalaryHub PostgreSQL database and executes the script.



The PostgreSQL client must be installed, and the database must already contain the employee and salary tables.



\## 7. How to Verify the Data



Run a count query against the employees table, filtering employee codes that start with `SEED-`. The expected result is 10,000.



Run a second count query for salary records belonging to seeded employees. The expected result is also 10,000.



These checks confirm that the seed process generated the expected data.



\## 8. Why This Matters



The seed data allows us to test the application with a realistic number of records.



It supports testing employee search, country and department filters, pagination, salary history, and salary analytics.



It also makes it easier to demonstrate the application without manually entering large amounts of data.



\## 9. Important Limitation



The seed script generates fictional sample data. It does not represent real employees or verified salary benchmarks.



The script assumes that the database tables have already been created by the running application's schema setup.

