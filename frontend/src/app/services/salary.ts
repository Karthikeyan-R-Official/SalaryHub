import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
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

export interface GlobalSalaryRecord extends SalaryRecord {
  employeeName: string;
  country: string;
  department: string;
  jobTitle: string;
}

export interface SalaryRecordPage {
  content: GlobalSalaryRecord[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
  first: boolean;
  last: boolean;
  numberOfElements: number;
}

export interface SalarySearchParams {
  search?: string;
  country?: string;
  department?: string;
  currency?: string;
  page: number;
  size: number;
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
  private readonly globalApiUrl = '/api/salaries';

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

  getAllSalaryRecords(
    params: SalarySearchParams
  ): Observable<SalaryRecordPage> {
    let httpParams = new HttpParams()
      .set('page', params.page)
      .set('size', params.size);

    if (params.search) {
      httpParams = httpParams.set('search', params.search);
    }

    if (params.country) {
      httpParams = httpParams.set('country', params.country);
    }

    if (params.department) {
      httpParams = httpParams.set('department', params.department);
    }

    if (params.currency) {
      httpParams = httpParams.set('currency', params.currency);
    }

    return this.http.get<SalaryRecordPage>(
      this.globalApiUrl,
      { params: httpParams }
    );
  }
}
