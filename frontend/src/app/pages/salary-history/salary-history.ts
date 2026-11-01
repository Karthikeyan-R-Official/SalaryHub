import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';

import {
  SalaryRecord,
  SalaryRequest,
  SalaryService
} from '../../services/salary';

@Component({
  selector: 'app-salary-history',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './salary-history.html',
  styleUrl: './salary-history.css'
})
export class SalaryHistoryComponent implements OnInit {

  employeeId!: number;

  salaryHistory: SalaryRecord[] = [];

  loading = false;
  submitting = false;

  errorMessage = '';
  successMessage = '';

  form = {
    amount: null as number | null,
    currency: 'INR',
    effectiveDate: '',
    notes: ''
  };

  constructor(
    private readonly route: ActivatedRoute,
    private readonly salaryService: SalaryService,
    private readonly cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    if (!Number.isInteger(id) || id <= 0) {
      this.errorMessage = 'Invalid employee ID.';
      return;
    }

    this.employeeId = id;
    this.loadSalaryHistory();
  }

  loadSalaryHistory(): void {
    this.loading = true;
    this.errorMessage = '';

    this.salaryService.getSalaryHistory(this.employeeId).subscribe({
      next: (records) => {
        this.salaryHistory = [...records].sort((a, b) =>
          b.effectiveDate.localeCompare(a.effectiveDate)
        );

        this.loading = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.loading = false;
        this.errorMessage =
          'Unable to load salary history. Please try again.';

        this.cdr.markForCheck();
      }
    });
  }

  addSalaryRevision(): void {
    this.errorMessage = '';
    this.successMessage = '';

    if (
      this.form.amount === null ||
      !Number.isFinite(this.form.amount) ||
      this.form.amount <= 0 ||
      !this.form.currency ||
      !this.form.effectiveDate
    ) {
      this.errorMessage =
        'Please enter a valid amount, currency and effective date.';
      return;
    }

    const request: SalaryRequest = {
      amount: this.form.amount,
      currency: this.form.currency.trim().toUpperCase(),
      effectiveDate: this.form.effectiveDate,
      notes: this.form.notes.trim() || undefined
    };

    this.submitting = true;

    this.salaryService.createSalary(this.employeeId, request).subscribe({
      next: () => {
        this.submitting = false;
        this.successMessage =
          'Salary revision added successfully.';

        this.form = {
          amount: null,
          currency: 'INR',
          effectiveDate: '',
          notes: ''
        };

        this.loadSalaryHistory();
        this.cdr.markForCheck();
      },
      error: (error) => {
        this.submitting = false;

        if (error.status === 400) {
          this.errorMessage =
            error.error?.detail ||
            'Invalid salary details. Please check the entered values.';
        } else if (error.status === 404) {
          this.errorMessage = 'Employee not found.';
        } else if (error.status === 409) {
          this.errorMessage =
            'A salary revision already exists for this effective date.';
        } else {
          this.errorMessage =
            'Unable to add salary revision. Please try again.';
        }

        this.cdr.markForCheck();
      }
    });
  }

  get currentSalary(): SalaryRecord | undefined {
    const today = this.getLocalDate();

    return this.salaryHistory.find(
      record => record.effectiveDate <= today
    );
  }

  private getLocalDate(): string {
    const now = new Date();

    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, '0');
    const day = String(now.getDate()).padStart(2, '0');

    return `${year}-${month}-${day}`;
  }
}
