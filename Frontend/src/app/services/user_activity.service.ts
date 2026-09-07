import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class UserActivityService {
  
  private apiUrl = 'http://localhost:8079/api/users';

  constructor(private http: HttpClient) {}

  // POST /api/users/generate-confirmation-code
  generateConfirmationCode(email: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/generate-confirmation-code`, { email }, { responseType: 'text' });
  }

  // POST /api/users/verify-confirmation-code
  verifyConfirmationCode(email: string, code: string): Observable<string> {
    return this.http.post(
      `${this.apiUrl}/verify-confirmation-code`, 
      { email, code }, 
      { responseType: 'text' }
    );
  }
}