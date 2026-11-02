import { CommonModule } from '@angular/common';
import {
  ChangeDetectorRef,
  Component,
  OnInit
} from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';

import {
  GlobalSalaryRecord,
  SalaryRecordPage,
  SalaryService
} from '../../services/salary';

@Component({
  selector: 'app-salary-records',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './salary-records.html',
  styleUrl: './salary-records.css'
})
export class SalaryRecordsComponent implements OnInit {

  records: GlobalSalaryRecord[] = [];

  search = '';
  country = '';
  department = '';
  currency = '';

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

  readonly currencies = [
    'INR',
    'USD',
    'GBP',
    'AUD',
    'EUR',
    'SGD'
  ];

  constructor(
    private readonly salaryService: SalaryService,
    private readonly cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadSalaryRecords();
  }

  loadSalaryRecords(): void {
    this.loading = true;
    this.errorMessage = '';

    this.salaryService.getAllSalaryRecords({
      search: this.search.trim(),
      country: this.country,
      department: this.department,
      currency: this.currency,
      page: this.currentPage,
      size: this.pageSize
    }).subscribe({
      next: (response: SalaryRecordPage) => {
        this.records = response.content;
        this.totalElements = response.totalElements;
        this.totalPages = response.totalPages;

        this.loading = false;
        this.cdr.markForCheck();
      },

      error: (error) => {
        console.error('Failed to load salary records:', error);

        this.errorMessage =
          'Unable to load salary records. Please try again.';

        this.loading = false;
        this.cdr.markForCheck();
      }
    });
  }

  applyFilters(): void {
    this.currentPage = 0;
    this.loadSalaryRecords();
  }

  clearFilters(): void {
    this.search = '';
    this.country = '';
    this.department = '';
    this.currency = '';
    this.currentPage = 0;

    this.loadSalaryRecords();
  }

  goToPage(page: number): void {
    if (page < 0 || page >= this.totalPages) {
      return;
    }

    this.currentPage = page;
    this.loadSalaryRecords();
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

  trackBySalaryId(
    _index: number,
    record: GlobalSalaryRecord
  ): number {
    return record.id;
  }
}
