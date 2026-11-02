import { Routes } from '@angular/router';

import { Dashboard } from './pages/dashboard/dashboard';
import { EmployeeComponent } from './pages/employee/employee';
import { EmployeeDetailsComponent } from './pages/employee-details/employee-details';
import { AddEmployeeComponent } from './pages/add-employee/add-employee';
import { SalaryHistoryComponent } from './pages/salary-history/salary-history';
import { SalaryRecordsComponent } from './pages/salary-records/salary-records';
import { LoginComponent } from './pages/login/login';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [

  {
    path: 'login',
    component: LoginComponent
  },

  {
    path: '',
    component: Dashboard,
    canActivate: [authGuard]
  },

  {
    path: 'employees/new',
    component: AddEmployeeComponent,
    canActivate: [authGuard]
  },

  {
    path: 'employees/:id/edit',
    component: AddEmployeeComponent,
    canActivate: [authGuard]
  },

  {
    path: 'employees/:id/salaries',
    component: SalaryHistoryComponent,
    canActivate: [authGuard]
  },

  {
    path: 'salary-records',
    component: SalaryRecordsComponent,
    canActivate: [authGuard]
  },

  {
    path: 'employees/:id',
    component: EmployeeDetailsComponent,
    canActivate: [authGuard]
  },

  {
    path: 'employees',
    component: EmployeeComponent,
    canActivate: [authGuard]
  },

  {
    path: '**',
    redirectTo: ''
  }
];
