import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthService, UserPayload } from '../../services/auth.service';
import { PetitionService } from '../../services/petition.service';
import { PetitionResponse } from '../../services/models/petition.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-user-dashboard-page',
  standalone: false,
  templateUrl: './user-dashboard-page.component.html',
  styleUrl: './user-dashboard-page.component.css',
})
export class UserDashboardPageComponent implements OnInit {
  currentUser$: Observable<UserPayload | null>;

  signedPetitions: PetitionResponse[] = [];
  createdPetitions: PetitionResponse[] = [];

  stats: any = { signedPetitionsCount: 0, createdPetitionsCount: 0 };
  signedPercent: number = 0;
  createdPercent: number = 0;

  isLoadingActivity: boolean = false;

  activeTab: 'signed' | 'pending' | 'approved' | 'rejected' = 'signed';

  expandedPetitionId: number | null = null;

  constructor(
    private authService: AuthService,
    private petitionService: PetitionService,
    private router: Router
  ) {
    this.currentUser$ = this.authService.currentUser$;
  }

  ngOnInit(): void {
    this.currentUser$.subscribe((user) => {
      if (user && user.id) {
        this.fetchAllData(user.id);
      }
    });
  }

  fetchAllData(userId: number) {
    this.isLoadingActivity = true;

    this.petitionService.getUserStatistics(userId).subscribe({
      next: (data) => {
        this.stats = data;
        this.calculatePercentages();
      },
      error: (err) => console.error('Stats error:', err),
    });

    this.petitionService.getSignedPetitions(userId).subscribe({
      next: (data) => {
        this.signedPetitions = data;
        this.petitionService.getSubmittedPetitions(userId).subscribe({
          next: (createdData) => {
            this.createdPetitions = createdData;
            this.isLoadingActivity = false;
          },
        });
      },
      error: () => (this.isLoadingActivity = false),
    });
  }

  calculatePercentages() {
    const signed = this.stats.signedPetitionsCount || 0;
    const created = this.stats.createdPetitionsCount || 0;
    const total = signed + created;

    if (total > 0) {
      this.signedPercent = Math.round((signed / total) * 100);
      this.createdPercent = Math.round((created / total) * 100);
    } else {
      this.signedPercent = 0;
      this.createdPercent = 0;
    }
  }
  setActiveTab(tab: 'signed' | 'pending' | 'approved' | 'rejected') {
    this.activeTab = tab;
    this.expandedPetitionId = null;
  }
  get filteredCreatedPetitions(): PetitionResponse[] {
    if (this.activeTab === 'signed') return [];
    const targetStatus = this.activeTab.toUpperCase();
    return this.createdPetitions.filter((p) => p.state === targetStatus);
  }

  toggleDetails(id: number) {
    if (this.expandedPetitionId === id) {
      this.expandedPetitionId = null;
    } else {
      this.expandedPetitionId = id;
    }
  }

  editRejectedPetition(petition: any) {
    this.router.navigate(['/create-petition'], {
      state: { petitionData: petition },
    });
  }

  getProgress(current: number, goal: number): number {
    if (!goal || goal === 0) return 0;
    return Math.min(Math.round((current / goal) * 100), 100);
  }

  getStatusClass(state: string): string {
    switch (state) {
      case 'PENDING':
        return 'badge-pending';
      case 'APPROVED':
        return 'badge-approved';
      case 'REJECTED':
        return 'badge-rejected';
      case 'COMPLETED':
        return 'badge-completed';
      default:
        return '';
    }
  }
}
