import { Routes } from '@angular/router';

import { Dashboard } from './pages/dashboard/dashboard';
import { EmployeeComponent } from './pages/employee/employee';
import { EmployeeDetailsComponent } from './pages/employee-details/employee-details';
import { AddEmployeeComponent } from './pages/add-employee/add-employee';
import { SalaryHistoryComponent } from './pages/salary-history/salary-history';

export const routes: Routes = [
  {
    path: '',
    component: Dashboard
  },
  {
    path: 'employees/new',
    component: AddEmployeeComponent
  },
  {
    path: 'employees/:id/edit',
    component: AddEmployeeComponent
  },
  {
    path: 'employees/:id/salaries',
    component: SalaryHistoryComponent
  },
  {
    path: 'employees/:id',
    component: EmployeeDetailsComponent
  },
  {
    path: 'employees',
    component: EmployeeComponent
  },
  {
    path: '**',
    redirectTo: ''
  }
];
