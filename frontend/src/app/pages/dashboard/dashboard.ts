import {
  CommonModule,
  CurrencyPipe,
  DecimalPipe
} from '@angular/common';

import {
  ChangeDetectorRef,
  Component,
  OnInit
} from '@angular/core';

import { HttpClient } from '@angular/common/http';
import { forkJoin } from 'rxjs';

interface AnalyticsOverview {
  totalEmployees: number;
  activeEmployees: number;
  inactiveEmployees: number;
  totalSalaryRecords: number;
}

interface SalaryBreakdown {
  groupName: string;
  currency: string;
  employeeCount: number;
  averageSalary: number | string | null;
}

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, CurrencyPipe, DecimalPipe],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class Dashboard implements OnInit {

  overview: AnalyticsOverview | null = null;

  countryBreakdown: SalaryBreakdown[] = [];
  departmentBreakdown: SalaryBreakdown[] = [];

  loading = true;
  errorMessage = '';

  constructor(
    private readonly http: HttpClient,
    private readonly cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadDashboard();
  }

  loadDashboard(): void {
    this.loading = true;
    this.errorMessage = '';

    forkJoin({
      overview: this.http.get<AnalyticsOverview>(
        '/api/analytics/overview'
      ),
      countries: this.http.get<SalaryBreakdown[]>(
        '/api/analytics/salaries/by-country'
      ),
      departments: this.http.get<SalaryBreakdown[]>(
        '/api/analytics/salaries/by-department'
      )
    }).subscribe({
      next: (data) => {
        this.overview = data.overview;
        this.countryBreakdown = data.countries;
        this.departmentBreakdown = data.departments;

        this.loading = false;
        this.cdr.markForCheck();
      },
      error: (error) => {
        console.error('Failed to load dashboard data:', error);

        this.errorMessage =
          'Unable to load dashboard data. Check that the backend is running and try again.';

        this.loading = false;
        this.cdr.markForCheck();
      }
    });
  }

  getEmployeeBarWidth(
    count: number,
    rows: SalaryBreakdown[]
  ): number {
    const maximum = Math.max(
      ...rows.map((row) => row.employeeCount),
      1
    );

    return Math.max((count / maximum) * 100, 2);
  }

  trackByGroupName(
    _index: number,
    row: SalaryBreakdown
  ): string {
    return `${row.groupName}-${row.currency}`;
  }

  isNumeric(value: unknown): value is number {
    return typeof value === 'number' && Number.isFinite(value);
  }
}
