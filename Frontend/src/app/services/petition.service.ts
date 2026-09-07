import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import {
  PetitionResponse,
  PetitionCreateRequest,
  PetitionUpdateRequest,
  PetitionFeedbackRequest,
  PetitionSignRequest,
} from './models/petition.model';
import { Page } from './models/page.model';
import { UserResponse } from './models/user.model';

@Injectable({
  providedIn: 'root',
})
export class PetitionService {
  private apiUrl = 'http://localhost:8079/api/petitions';

  private userApiUrl = 'http://localhost:8079/api/users';
  
  private userAccountApiUrl = 'http://localhost:8079/api/accounts'

  constructor(private http: HttpClient) {}

  // GET /api/petitions
  getAllApprovedPetitions(filters?: {
    keyword?: string;
    categories?: string[];
    orderBy?: string;
    page?: number;
    size?: number; 
  }): Observable<Page<PetitionResponse>> {

    let params = new HttpParams();
    if (filters?.keyword) params = params.append('keyword', filters.keyword);

    if (filters?.categories?.length) {
      filters.categories.forEach((category) => {
        params = params.append('categories', category);
      });
    }

    if (filters?.orderBy) params = params.append('orderBy', filters.orderBy);

    if (filters?.page !== undefined)
      params = params.append('page', filters.page.toString());

    if (filters?.size !== undefined)
      params = params.append('size', filters.size.toString());

    return this.http.get<Page<PetitionResponse>>(this.apiUrl, { params });
  }

  // GET /api/petitions/all
  getAllPetitions(filters?: { page?: number; size?: number }): Observable<Page<PetitionResponse>> {
    let params = new HttpParams();
    if (filters?.page !== undefined) params = params.append('page', filters.page.toString());
    if (filters?.size !== undefined) params = params.append('size', filters.size.toString());

    return this.http.get<Page<PetitionResponse>>(`${this.apiUrl}/all`, { params });
  }

  // GET /api/petitions/{id}/details
  getPetitionDetails(id: number): Observable<PetitionResponse> {
    return this.http.get<PetitionResponse>(`${this.apiUrl}/${id}/details`);
  }

  // GET /api/petitions/{id}/signatures
  getPetitionSignatures(id: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/${id}/signatures`);
  }

  // GET /api/petitions/{id}/statistics
  getPetitionStatistics(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${id}/statistics`);
  }

  // POST /api/petitions
  createPetition(
    petition: PetitionCreateRequest
  ): Observable<PetitionResponse> {
    return this.http.post<PetitionResponse>(this.apiUrl, petition);
  }

  // POST /api/petitions/{id}/signatures
  signPetition(id: number, data: PetitionSignRequest): Observable<String> {
    return this.http.post(`${this.apiUrl}/${id}/signatures`, data, { responseType: 'text'});
  }

  // POST /api/petitions/{id}/feedback
  submitFeedback(
    id: number,
    feedback: PetitionFeedbackRequest
  ): Observable<string> {
    return this.http.post(`${this.apiUrl}/${id}/feedback`, feedback, {
      responseType: 'text'
    });
  }

  // PUT /api/petitions/{id}
  updatePetition(
    id: number,
    petition: PetitionUpdateRequest
  ): Observable<PetitionResponse> {
    return this.http.put<PetitionResponse>(`${this.apiUrl}/${id}`, petition);
  }

  // DELETE /api/petitions/{id}
  deletePetition(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  // GET /api/users/{id}/signed-petitions
  getSignedPetitions(userId: number): Observable<PetitionResponse[]> {
    return this.http.get<PetitionResponse[]>(
      `${this.userApiUrl}/${userId}/signed-petitions`
    );
  }
  //  GET /api/users/{id}/submitted-petitions
  getSubmittedPetitions(userId: number): Observable<PetitionResponse[]> {
    return this.http.get<PetitionResponse[]>(
      `${this.userApiUrl}/${userId}/submitted-petitions`
    );
  }
  getUserStatistics(userId: number): Observable<any> {
    return this.http.get<any>(`${this.userApiUrl}/${userId}/statistics`);
  }


  // GET /api/accounts/{id}
  getUserById(id: number): Observable<UserResponse>{
   return this.http.get<UserResponse>(`${this.userAccountApiUrl}/${id}`); 
  }

  // POST /api/petitions/{id}/image
  uploadImage(petitionId: number, file: File): Observable<string> {
    const formData = new FormData();
    formData.append('file', file); 

    return this.http.post(`${this.apiUrl}/${petitionId}/image`, formData, {
      responseType: 'text'
    });
  }
}
