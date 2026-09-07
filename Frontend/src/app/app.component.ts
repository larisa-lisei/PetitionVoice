import { Component } from '@angular/core';
import { AuthService, UserPayload } from './services/auth.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-root',
  standalone: false,
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent {
  title = 'Frontend';

  isDesktop = window.navigator.userAgent.toLowerCase().includes('electron');

  currentUser$: Observable<UserPayload | null>;

  showUserDetails: boolean = false;
  isEditMode: boolean = false;

  editFirstName: string = '';
  editLastName: string = '';

  constructor(private authService: AuthService) {
    this.currentUser$ = this.authService.currentUser$;
  }

  toggleUserMenu() {
    this.showUserDetails = !this.showUserDetails;
    if (!this.showUserDetails) {
      this.isEditMode = false;
    }
  }

  enableEdit(user: UserPayload) {
    this.isEditMode = true;
    this.editFirstName = user.firstName;
    this.editLastName = user.lastName;
  }

  cancelEdit() {
    this.isEditMode = false;
  }

  saveChanges(user: UserPayload) {
    if (!user.id) {
      console.error('User ID missing');
      return;
    }

    const updateData = {
      firstName: this.editFirstName,
      lastName: this.editLastName,
    };

    this.authService.updateProfile(user.id, updateData).subscribe({
      next: () => {
        this.isEditMode = false;
      },
      error: (err) => {
        console.error('Failed to update user', err);
      },
    });
  }

  logout() {
    this.showUserDetails = false;
    this.authService.logout();
  }
}
