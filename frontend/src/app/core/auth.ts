import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { LoginResponse } from './models';

@Injectable({ providedIn: 'root' })
export class Auth {
  private http = inject(HttpClient);
  private router = inject(Router);
  private readonly apiUrl = 'http://localhost:8080/api/auth';

  login(username: string, password: string): Observable<LoginResponse> {
    return this.http
      .post<LoginResponse>(`${this.apiUrl}/login`, { username, password })
      .pipe(
        tap((res) => {
          localStorage.setItem('token', res.token);
          localStorage.setItem('username', res.username);
          localStorage.setItem('role', res.role);
        })
      );
  }

  logout() {
    localStorage.clear();
    this.router.navigate(['/login']);
  }

  getToken() { return localStorage.getItem('token'); }
  getUsername() { return localStorage.getItem('username'); }
  getRole() { return localStorage.getItem('role'); }
  isLoggedIn() { return !!this.getToken(); }
  hasRole(...roles: string[]) {
    const r = this.getRole();
    return !!r && roles.includes(r);
  }
}