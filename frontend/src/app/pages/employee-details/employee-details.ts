import { CommonModule } from '@angular/common';
import {
  ChangeDetectorRef,
  Component,
  OnInit
} from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';

import {
  Employee,
  EmployeeService
} from '../../services/employee';

@Component({
  selector: 'app-employee-details',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './employee-details.html',
  styleUrl: './employee-details.css'
})
export class EmployeeDetailsComponent implements OnInit {

  employee: Employee | null = null;

  loading = true;
  errorMessage = '';

  constructor(
    private readonly route: ActivatedRoute,
    private readonly employeeService: EmployeeService,
    private readonly cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = Number(params.get('id'));

      if (!Number.isInteger(id) || id <= 0) {
        this.errorMessage = 'Invalid employee ID.';
        this.loading = false;
        this.cdr.markForCheck();
        return;
      }

      this.loadEmployee(id);
    });
  }

  loadEmployee(id: number): void {
    this.loading = true;
    this.errorMessage = '';
    this.employee = null;

    this.employeeService.getEmployee(id).subscribe({
      next: (response) => {
        this.employee = response;
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: (error) => {
        console.error('Failed to load employee:', error);

        this.errorMessage =
          'Unable to load employee details. The employee may not exist.';

        this.loading = false;
        this.cdr.markForCheck();
      }
    });
  }
}
