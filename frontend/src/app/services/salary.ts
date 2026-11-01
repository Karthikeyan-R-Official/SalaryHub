import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface SalaryRecord {
  id: number;
  employeeId: number;
  employeeCode: string;
  amount: number;
  currency: string;
  effectiveDate: string;
  notes: string | null;
  createdAt: string;
}

export interface SalaryRequest {
  amount: number;
  currency: string;
  effectiveDate: string;
  notes?: string;
}

@Injectable({
  providedIn: 'root'
})
export class SalaryService {

  private readonly apiUrl = '/api/employees';

  constructor(private readonly http: HttpClient) {}

  getSalaryHistory(employeeId: number): Observable<SalaryRecord[]> {
    return this.http.get<SalaryRecord[]>(
      `${this.apiUrl}/${employeeId}/salaries`
    );
  }

  createSalary(
    employeeId: number,
    request: SalaryRequest
  ): Observable<SalaryRecord> {
    return this.http.post<SalaryRecord>(
      `${this.apiUrl}/${employeeId}/salaries`,
      request
    );
  }
}
