\# SalaryHub – Application Testing Guide



\## 1. Purpose



This document explains how to manually verify the main features of SalaryHub.



The tests are intended for reviewers and developers who want to check the application's functionality through its user interface.



\## 2. Test Environment



\*\*Deployed application:\*\*



https://salaryhub-hr-management.vercel.app



\*\*Backend health check:\*\*



https://salaryhub-backend.onrender.com/api/health



Use the HR Manager credentials configured for the application.



Do not include actual passwords, JWT tokens, or database credentials in this document.



\## 3. Login and Authentication



\### Test 1: Successful Login



Steps:

1\. Open the application.

2\. Enter valid HR Manager credentials.

3\. Click Login.



Expected result:

\- The user should be authenticated successfully.

\- The dashboard should be displayed.



\### Test 2: Invalid Login



Steps:

1\. Open the login page.

2\. Enter invalid credentials.

3\. Click Login.



Expected result:

\- Login should fail.

\- The user should not gain access to protected pages.



\### Test 3: Protected Page Access



Steps:

1\. Log out.

2\. Try to access a protected application page.



Expected result:

\- An unauthenticated user should not be allowed to access protected pages.



\## 4. Dashboard Testing



\### Test 4: Dashboard Statistics



Steps:

1\. Log in.

2\. Open the dashboard.

3\. Review the employee and salary statistics.



Expected result:

\- Employee counts and salary record counts should be displayed.

\- Salary analytics should be grouped by country and department.

\- Different currencies should remain separate.



\## 5. Employee Management Testing



\### Test 5: Employee Listing



Steps:

1\. Open the Employees page.

2\. Review the employee list.



Expected result:

\- Employee records should be displayed.



\### Test 6: Employee Search



Steps:

1\. Enter an employee name, code, or supported search value.

2\. Review the results.



Expected result:

\- Matching employee records should be displayed.



\### Test 7: Employee Filtering



Steps:

1\. Select a country or department filter.

2\. Review the results.



Expected result:

\- The list should show employees matching the selected filters.



\### Test 8: Employee Pagination



Steps:

1\. Navigate between employee list pages.

2\. Change the page size if the interface provides that option.



Expected result:

\- The employee list should display the corresponding page of records.



\### Test 9: Create Employee



Steps:

1\. Open the employee creation form.

2\. Enter valid employee information.

3\. Save the employee.



Expected result:

\- The employee should be created successfully.

\- The new employee should appear in the employee list.



\### Test 10: Update Employee



Steps:

1\. Open an existing employee.

2\. Edit one or more employee details.

3\. Save the changes.



Expected result:

\- The updated employee information should be displayed.



\## 6. Salary Management Testing



\### Test 11: View Salary History



Steps:

1\. Open an employee's details.

2\. Navigate to salary management.

3\. Review the salary records.



Expected result:

\- The employee's salary history should be displayed.



\### Test 12: Add Salary Revision



Steps:

1\. Open an employee's salary section.

2\. Enter a valid salary amount.

3\. Select a three-letter currency code.

4\. Enter an effective date.

5\. Add optional notes.

6\. Save the revision.



Expected result:

\- A new salary record should be created.

\- The salary history should contain the new record.

\- Existing salary records should remain available.



\### Test 13: Future-Dated Salary Revision



Steps:

1\. Open an employee's salary section.

2\. Enter a valid salary revision with a future effective date.

3\. Save the revision.

4\. Review the salary history.



Expected result:

\- The future-dated revision should be stored with its effective date.

\- It should not replace the currently effective salary before that date.



\## 7. Global Salary Records Testing



\### Test 14: View Salary Records



Steps:

1\. Open the Salary Records page.

2\. Review the displayed records.



Expected result:

\- Salary records across employees should be displayed.



\### Test 15: Salary Record Search and Filters



Steps:

1\. Search using employee information.

2\. Apply country, department, and currency filters.

3\. Review the results.



Expected result:

\- The results should match the selected search criteria and filters.



\### Test 16: Salary Record Pagination



Steps:

1\. Navigate between pages of salary records.



Expected result:

\- The corresponding salary records should be displayed.



\## 8. Logout Testing



\### Test 17: Logout



Steps:

1\. Click Logout.

2\. Try to access a protected page.



Expected result:

\- The application should return to the login page.

\- Protected pages should require authentication.



\## 9. Testing Notes



\- Use dedicated test records when creating or updating employees.

\- Avoid modifying important production employee or salary records during manual testing.

\- Use the local development environment for tests that create, update, or modify data unless production testing has been explicitly approved.

\- Expected results describe the intended behavior. Record actual results separately when executing these tests.



\## 10. Conclusion



This guide covers the main manual verification scenarios for authentication, employee management, salary management, salary history, analytics, filtering, pagination, and logout.

