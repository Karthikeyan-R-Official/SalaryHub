import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Employee {
  id: number;
  employeeCode: string;
  firstName: string;
  lastName: string;
  email: string;
  country: string;
  department: string;
  jobTitle: string;
  hireDate: string;
  employmentStatus: string;
}

export interface EmployeeRequest {
  employeeCode: string;
  firstName: string;
  lastName: string;
  email: string;
  country: string;
  department: string;
  jobTitle: string;
  hireDate: string;
  employmentStatus?: string;
}

export interface EmployeePage {
  content: Employee[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
  first: boolean;
  last: boolean;
  numberOfElements: number;
}

export interface EmployeeSearchParams {
  search?: string;
  country?: string;
  department?: string;
  page: number;
  size: number;
}

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {

  private readonly apiUrl = '/api/employees';

  constructor(private readonly http: HttpClient) {}

  getEmployees(params: EmployeeSearchParams): Observable<EmployeePage> {
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

    return this.http.get<EmployeePage>(this.apiUrl, {
      params: httpParams
    });
  }

  getEmployee(id: number): Observable<Employee> {
    return this.http.get<Employee>(`${this.apiUrl}/${id}`);
  }

  createEmployee(request: EmployeeRequest): Observable<Employee> {
    return this.http.post<Employee>(this.apiUrl, request);
  }

  updateEmployee(
    id: number,
    request: EmployeeRequest
  ): Observable<Employee> {
    return this.http.put<Employee>(`${this.apiUrl}/${id}`, request);
  }
}
