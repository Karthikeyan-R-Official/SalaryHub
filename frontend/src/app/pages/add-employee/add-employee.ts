import { CommonModule } from '@angular/common';
import {
  ChangeDetectorRef,
  Component,
  OnInit
} from '@angular/core';
import { FormsModule } from '@angular/forms';
import {
  ActivatedRoute,
  Router,
  RouterLink
} from '@angular/router';

import {
  Employee,
  EmployeeRequest,
  EmployeeService
} from '../../services/employee';

@Component({
  selector: 'app-add-employee',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterLink
  ],
  templateUrl: './add-employee.html',
  styleUrl: './add-employee.css'
})
export class AddEmployeeComponent implements OnInit {

  employeeId: number | null = null;
  isEditMode = false;

  employee: EmployeeRequest = this.createEmptyEmployee();

  readonly countries = [
    'India',
    'USA',
    'UK',
    'Australia',
    'Germany',
    'Singapore'
  ];

  readonly departments = [
    'Engineering',
    'Finance',
    'Human Resources',
    'Legal',
    'Marketing',
    'Operations',
    'Sales',
    'Customer Support'
  ];

  readonly statuses = [
    'ACTIVE',
    'INACTIVE'
  ];

  loading = false;
  saving = false;
  errorMessage = '';

  constructor(
    private readonly employeeService: EmployeeService,
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');

    if (id !== null) {
      const parsedId = Number(id);

      if (!Number.isInteger(parsedId) || parsedId <= 0) {
        this.errorMessage = 'Invalid employee ID.';
        return;
      }

      this.employeeId = parsedId;
      this.isEditMode = true;
      this.loadEmployee(parsedId);
    }
  }

  private createEmptyEmployee(): EmployeeRequest {
    return {
      employeeCode: '',
      firstName: '',
      lastName: '',
      email: '',
      country: '',
      department: '',
      jobTitle: '',
      hireDate: '',
      employmentStatus: 'ACTIVE'
    };
  }

  private loadEmployee(id: number): void {
    this.loading = true;
    this.errorMessage = '';

    this.employeeService.getEmployee(id).subscribe({
      next: (response: Employee) => {
        this.employee = {
          employeeCode: response.employeeCode,
          firstName: response.firstName,
          lastName: response.lastName,
          email: response.email,
          country: response.country,
          department: response.department,
          jobTitle: response.jobTitle,
          hireDate: response.hireDate,
          employmentStatus: response.employmentStatus
        };

        this.loading = false;
        this.cdr.markForCheck();
      },
      error: (error) => {
        console.error('Failed to load employee for editing:', error);

        this.errorMessage =
          'Unable to load employee details. The employee may not exist.';

        this.loading = false;
        this.cdr.markForCheck();
      }
    });
  }

  submitForm(): void {
    this.errorMessage = '';

    if (
      !this.employee.employeeCode.trim() ||
      !this.employee.firstName.trim() ||
      !this.employee.lastName.trim() ||
      !this.employee.email.trim() ||
      !this.employee.country ||
      !this.employee.department ||
      !this.employee.jobTitle.trim() ||
      !this.employee.hireDate
    ) {
      this.errorMessage = 'Please complete all required fields.';
      return;
    }

    const request: EmployeeRequest = {
      ...this.employee,
      employeeCode: this.employee.employeeCode.trim(),
      firstName: this.employee.firstName.trim(),
      lastName: this.employee.lastName.trim(),
      email: this.employee.email.trim(),
      jobTitle: this.employee.jobTitle.trim()
    };

    this.saving = true;

    const saveRequest = this.isEditMode && this.employeeId !== null
      ? this.employeeService.updateEmployee(this.employeeId, request)
      : this.employeeService.createEmployee(request);

    saveRequest.subscribe({
      next: (response: Employee) => {
        this.saving = false;
        this.cdr.markForCheck();

        if (this.isEditMode) {
          this.router.navigate(['/employees', this.employeeId]);
        } else {
          this.router.navigate(['/employees']);
        }
      },
      error: (error) => {
        console.error('Failed to save employee:', error);

        this.saving = false;

        if (error.status === 400) {
          this.errorMessage =
            'Please check the entered information. Some fields may be invalid.';
        } else if (error.status === 409) {
          this.errorMessage =
            'An employee with this employee code or email may already exist.';
        } else {
          this.errorMessage =
            'Unable to save employee. Please try again.';
        }

        this.cdr.markForCheck();
      }
    });
  }
}
