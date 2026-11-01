import { CommonModule } from '@angular/common';
import {
  ChangeDetectorRef,
  Component,
  OnInit
} from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';

import {
  Employee,
  EmployeePage,
  EmployeeService
} from '../../services/employee';

@Component({
  selector: 'app-employee',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './employee.html',
  styleUrl: './employee.css'
})
export class EmployeeComponent implements OnInit {

  employees: Employee[] = [];

  search = '';
  country = '';
  department = '';

  currentPage = 0;
  pageSize = 10;

  totalElements = 0;
  totalPages = 0;

  loading = false;
  errorMessage = '';

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

  constructor(
    private readonly employeeService: EmployeeService,
    private readonly cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadEmployees();
  }

  loadEmployees(): void {
    this.loading = true;
    this.errorMessage = '';

    this.employeeService.getEmployees({
      search: this.search.trim(),
      country: this.country,
      department: this.department,
      page: this.currentPage,
      size: this.pageSize
    }).subscribe({
      next: (response: EmployeePage) => {
        console.log('Employee API response:', response);

        this.employees = response.content;
        this.totalElements = response.totalElements;
        this.totalPages = response.totalPages;

        this.loading = false;
        this.cdr.markForCheck();
      },

      error: (error) => {
        console.error('Failed to load employees:', error);

        this.errorMessage =
          'Unable to load employees. Please try again.';

        this.loading = false;
        this.cdr.markForCheck();
      }
    });
  }

  applyFilters(): void {
    this.currentPage = 0;
    this.loadEmployees();
  }

  clearFilters(): void {
    this.search = '';
    this.country = '';
    this.department = '';
    this.currentPage = 0;

    this.loadEmployees();
  }

  goToPage(page: number): void {
    if (page < 0 || page >= this.totalPages) {
      return;
    }

    this.currentPage = page;
    this.loadEmployees();
  }

  get startRecord(): number {
    return this.totalElements === 0
      ? 0
      : this.currentPage * this.pageSize + 1;
  }

  get endRecord(): number {
    return Math.min(
      (this.currentPage + 1) * this.pageSize,
      this.totalElements
    );
  }

  trackByEmployeeId(
    _index: number,
    employee: Employee
  ): number {
    return employee.id;
  }
}
