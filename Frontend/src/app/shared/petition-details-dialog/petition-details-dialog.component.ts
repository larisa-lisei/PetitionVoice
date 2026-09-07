import { Component, OnInit } from '@angular/core';
import { Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatProgressBar } from "@angular/material/progress-bar";
import {  MatIconModule } from '@angular/material/icon';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { PetitionService } from '../../services/petition.service';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';
import { PetitionSignRequest } from '../../services/models/petition.model';
import { MatDialogRef } from '@angular/material/dialog';
import { UserResponse } from '../../services/models/user.model';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import { UserActivityService } from '../../services/user_activity.service';

@Component({
  selector: 'app-petition-details-dialog',
  standalone: true,
  imports: [CommonModule, MatProgressBar, ReactiveFormsModule, FormsModule, MatIconModule],
  templateUrl: './petition-details-dialog.component.html',
  styleUrl: './petition-details-dialog.component.css'
})
export class PetitionDetailsDialogComponent implements OnInit {

  signForm!: FormGroup;
  fullUserData: any = null;

  isVerificationStep = false;
  verificationCode = '';

  constructor(@Inject(MAT_DIALOG_DATA) public data: any,
              private formBuilder: FormBuilder,
              private petitionService: PetitionService,
              private userActivityService: UserActivityService,
              public authService: AuthService,
              private dialogRef: MatDialogRef<PetitionDetailsDialogComponent>,
              private toastr : ToastrService,
              private router: Router){}

  

  ngOnInit(): void {
      this.signForm = this.formBuilder.group({
      firstName: ['', [Validators.required, Validators.minLength(2)]],
      lastName: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', [Validators.required, Validators.pattern('^[0-9]{10}$')]]
  });

    if (this.authService.isLoggedIn()) {
      const basicUser = this.authService.getCurrentUserValue();

      if (basicUser && basicUser.id) {

        //pentru ca in jwt nu avem telefonul apelam metoda getUserById sa luam 
        //UserResponse care contine UserDetails care contine telefonul.

        this.petitionService.getUserById(basicUser.id).subscribe({
          next: (response: UserResponse) => {
            this.fullUserData = response;
            console.log("Date profil încarcate:", this.fullUserData);
          },
          error: (err) => {
            console.error("Eroare la preluarea telefonului din DB:", err);
          }
        });
      }
    }
  }


  onSubmit(): void {
    //CAZ UTILIZATOR LOGAT
    if (this.authService.isLoggedIn()) {
        this.signAsUser();
    } 
    // CAZ GUEST
    else {
        if (this.isVerificationStep) {
            this.verifyAndSignGuest();
        } else {
            this.initiateGuestSigning();
        }
    }
  }

  private signAsUser() {
      const user = this.authService.getCurrentUserValue();
      if (!user) return;

      const payload: PetitionSignRequest = {
        firstName: user.firstName,
        lastName: user.lastName,
        email: user.email,
        telephone: this.fullUserData?.userDetails?.telephone || '', 
        idPetition: this.data.id
      };

      this.sendSignature(payload);
  }

  private initiateGuestSigning() {
      if (this.signForm.invalid) {
          this.signForm.markAllAsTouched();
          return;
      }

      const email = this.signForm.value.email;
      
      this.userActivityService.generateConfirmationCode(email).subscribe({
          next: () => {
              this.toastr.info('Verification code sent to ' + email);
              this.isVerificationStep = true;
          },
          error: (err) => {
              console.error('Error sending code:', err);
              this.toastr.error('Could not send verification email.');
          }
      });
  }

  private verifyAndSignGuest() {
      if (!this.verificationCode || this.verificationCode.length < 6) {
          this.toastr.warning('Please enter the valid code.');
          return;
      }

      const email = this.signForm.value.email;

      this.userActivityService.verifyConfirmationCode(email, this.verificationCode).subscribe({
          next: () => {
              const payload: PetitionSignRequest = {
                firstName: this.signForm.value.firstName,
                lastName: this.signForm.value.lastName,
                email: this.signForm.value.email,
                telephone: this.signForm.value.phone,
                idPetition: this.data.id
              };
              
              this.sendSignature(payload);
          },
          error: () => {
              this.toastr.error('Invalid or expired code.');
          }
      });
  }

  private sendSignature(payload: PetitionSignRequest) {
      this.petitionService.signPetition(this.data.id, payload).subscribe({
          next: (response) => {
            console.log('Succes:', response);
            this.toastr.success('You have signed with succes this petition!');
            this.dialogRef.close(true); // sa inchida fereastra automat cand s-a semnat
            this.router.navigate(['/petitions'], {
              queryParams: { open: this.data.id}
            });
          },
          error: (err) => {
            console.error('Eroare la trimitere:', err);
            if (err.status === 400 || err.status == 409) {
              this.toastr.error('You have already signed this petition!');
              this.dialogRef.close(true);
            } else {
              this.toastr.error('There has been an error. Try again later..');
            }
          }
      });
  }
}
