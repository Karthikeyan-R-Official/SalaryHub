import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  accessToken: string;
  tokenType: string;
  expiresIn: number;
  username: string;
  role: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly apiUrl = '/api/auth';
  private readonly tokenKey = 'salaryhub_access_token';
  private readonly usernameKey = 'salaryhub_username';
  private readonly roleKey = 'salaryhub_role';

  constructor(private readonly http: HttpClient) {}

  login(request: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(
      `${this.apiUrl}/login`,
      request
    ).pipe(
      tap(response => {
        sessionStorage.setItem(this.tokenKey, response.accessToken);
        sessionStorage.setItem(this.usernameKey, response.username);
        sessionStorage.setItem(this.roleKey, response.role);
      })
    );
  }

  getToken(): string | null {
    return sessionStorage.getItem(this.tokenKey);
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  getUsername(): string | null {
    return sessionStorage.getItem(this.usernameKey);
  }

  getRole(): string | null {
    return sessionStorage.getItem(this.roleKey);
  }

  logout(): void {
    sessionStorage.removeItem(this.tokenKey);
    sessionStorage.removeItem(this.usernameKey);
    sessionStorage.removeItem(this.roleKey);
  }
}
