import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { Router } from '@angular/router';
import { LoginRequest, RegisterRequest } from './models/auth.model';
import { jwtDecode } from 'jwt-decode';

export interface UserPayload {
  id: number;
  sub: string;
  email: string;
  firstName: string;
  lastName: string;
  role: string;
  exp: number;
  telephone?: string;
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = 'http://localhost:8079/api/accounts';
  private tokenKey = 'auth_token';
  private userKey = 'auth_user_data';

  private isAuthenticatedSubject = new BehaviorSubject<boolean>(
    this.hasToken()
  );
  public isAuthenticated$ = this.isAuthenticatedSubject.asObservable();

  private currentUserSubject = new BehaviorSubject<UserPayload | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient, private router: Router) {
    this.checkInitialToken();
  }

  private checkInitialToken() {
    const token = this.getToken();
    if (token) {
      if (this.isTokenExpired(token)) {
        this.logout();
      } else {
        this.isAuthenticatedSubject.next(true);

        const storedUser = localStorage.getItem(this.userKey);
        if (storedUser) {
          try {
            const user = JSON.parse(storedUser);
            this.currentUserSubject.next(user);
          } catch {
            this.decodeAndNotify(token);
          }
        } else {
          this.decodeAndNotify(token);
        }
      }
    }
  }

  register(data: RegisterRequest): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, data);
  }

  login(data: LoginRequest): Observable<string> {
    return this.http
      .post(`${this.apiUrl}/login`, data, { responseType: 'text' })
      .pipe(
        tap((token) => {
          localStorage.setItem(this.tokenKey, token);
          this.isAuthenticatedSubject.next(true);
          this.decodeAndNotify(token);
        })
      );
  }

  updateProfile(
    id: number,
    data: { firstName: string; lastName: string; telephone?: string }
  ): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/${id}`, data).pipe(
      tap(() => {
        const currentUser = this.currentUserSubject.value;
        if (currentUser) {
          const updatedUser = {
            ...currentUser,
            firstName: data.firstName,
            lastName: data.lastName,
            telephone: data.telephone ? data.telephone : currentUser.telephone,
          };

          this.currentUserSubject.next(updatedUser);
          localStorage.setItem(this.userKey, JSON.stringify(updatedUser));
        }
      })
    );
  }

  logout(): void {
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem(this.userKey);
    this.isAuthenticatedSubject.next(false);
    this.currentUserSubject.next(null);
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  isLoggedIn(): boolean {
    const token = this.getToken();
    if (!token) return false;
    if (this.isTokenExpired(token)) {
      this.logout();
      return false;
    }
    return true;
  }

  private hasToken(): boolean {
    return !!localStorage.getItem(this.tokenKey);
  }

  private isTokenExpired(token: string): boolean {
    try {
      const decoded: any = jwtDecode(token);
      if (!decoded.exp) return true;
      const expirationDate = decoded.exp * 1000;
      const now = Date.now();
      return now > expirationDate;
    } catch (e) {
      return true;
    }
  }

  private decodeAndNotify(token: string): void {
    try {
      const decoded: any = jwtDecode(token);

      const user: UserPayload = {
        id: decoded.id || decoded.userId,
        sub: decoded.sub,
        email: decoded.email,
        firstName: decoded.firstName,
        lastName: decoded.lastName,
        role: decoded.role,
        exp: decoded.exp,
        telephone: decoded.telephone,
      };

      localStorage.setItem(this.userKey, JSON.stringify(user));
      this.currentUserSubject.next(user);
    } catch (e) {
      this.currentUserSubject.next(null);
    }
  }

  getCurrentUserValue(): UserPayload | null {
    return this.currentUserSubject.value;
  }
}
