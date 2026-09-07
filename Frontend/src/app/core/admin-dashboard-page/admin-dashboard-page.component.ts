import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PetitionService } from '../../services/petition.service';
import { PetitionResponse, PetitionState, PetitionFeedbackRequest } from '../../services/models/petition.model';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, MatIconModule],
  templateUrl: './admin-dashboard-page.component.html',
  styleUrl: './admin-dashboard-page.component.css'
})
export class AdminDashboardComponent implements OnInit {
  allPetitions: PetitionResponse[] = [];
  filteredPetitions: PetitionResponse[] = [];
  loading = false;
  currentFilter: string = 'PENDING';
  
  selectedPetition: PetitionResponse | null = null;
  showModal = false;
  adminFeedback: { [key: number]: string } = {};

  readonly API_URL = 'http://localhost:8079'; 

  constructor(private petitionService: PetitionService) {}

  ngOnInit(): void {
    this.loadPetitions();
  }

  loadPetitions(): void {
    this.loading = true;
    this.petitionService.getAllPetitions({ size: 100 }).subscribe({
      next: (page) => {
        this.allPetitions = page.content;
        this.applyFilter(this.currentFilter);
        this.loading = false;
      },
      error: () => this.loading = false
    });
  }

  applyFilter(status: string): void {
    this.currentFilter = status;
    this.filteredPetitions = this.allPetitions.filter(p => p.state === status);
  }

  openDetails(p: PetitionResponse): void {
    this.selectedPetition = p;
    this.showModal = true;
  }

  closeModal(): void {
    this.selectedPetition = null;
    this.showModal = false;
  }

  onApprove(id: number): void {
    const request: PetitionFeedbackRequest = { 
      state: PetitionState.APPROVED, 
      feedback: this.adminFeedback[id] || 'Approved by administrator.' 
    };
    this.petitionService.submitFeedback(id, request).subscribe({
      next: () => { alert('Approved!'); this.loadPetitions(); this.closeModal(); }
    });
  }

  onReject(id: number): void {
    if (!this.adminFeedback[id]) { alert('Please provide a reason!'); return; }
    const request: PetitionFeedbackRequest = { 
      state: PetitionState.REJECTED, 
      feedback: this.adminFeedback[id] 
    };
    this.petitionService.submitFeedback(id, request).subscribe({
      next: () => { alert('Rejected!'); this.loadPetitions(); this.closeModal(); }
    });
  }
}