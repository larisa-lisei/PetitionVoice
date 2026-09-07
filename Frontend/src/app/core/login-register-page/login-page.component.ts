import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { LoginRequest, RegisterRequest } from '../../services/models/auth.model';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { UserActivityService } from '../../services/user_activity.service';


@Component({
  selector: 'app-login-page',
  imports: [CommonModule, FormsModule],
  standalone: true,
  templateUrl: './login-page.component.html',
  styleUrl: './login-page.component.css'

  
})
export class LoginPageComponent {

  modCurent = 'login'; // 'login' sau 'register'

  isVerificationStep = false; 
  verificationCode = '';

  loginData: LoginRequest = {
    email: '',
    password: ''
  };

  registerData: RegisterRequest = {
    email: '',
    password: '',
    firstName: '',
    lastName: '',
    telephone: ''
  };

  constructor(
    private authService: AuthService, 
    private userActivityService: UserActivityService,
    private router: Router,
    private tostr: ToastrService
  ) {}

  schimbaModul(modNou: string){
    this.modCurent = modNou;
    this.isVerificationStep = false; 
    this.verificationCode = '';
  }


  private isElectron(): boolean{
    return window.navigator.userAgent.toLowerCase().includes('electron');
  }

  submitFormular(){
    if(this.modCurent === 'login'){
      this.handleLogin();
    }
    else{
      if (this.isVerificationStep) {
        this.verifyAndRegister();
      } else {
        this.initiateRegistration();
      }
    }
  }

  private handleLogin() {
    this.authService.login(this.loginData).subscribe({
      next: (token) => {

        console.log('Login reusit', token);
        this.router.navigate(['/']); 
      },
      error: (err) => {
        this.tostr.error('Wrong email or password!');
        console.error('Login esuat', err);
      }
    });
  }

  private initiateRegistration() {
    if (!this.registerData.email || !this.registerData.password) {
      this.tostr.error('Please fill in all required fields first.');
      return;
    }

    console.log('Sending verification code to:', this.registerData.email);
    
    this.userActivityService.generateConfirmationCode(this.registerData.email).subscribe({
      next: () => {
        this.tostr.info('Verification code sent to your email.');
        this.isVerificationStep = true;
      },
      error: (err) => {
        console.error('Error sending code:', err);
        this.tostr.error('Could not send verification email. Try again.');
      }
    });
  }

  private verifyAndRegister() {
    if (!this.verificationCode) {
      this.tostr.error('Please enter the verification code.');
      return;
    }

    this.userActivityService.verifyConfirmationCode(this.registerData.email, this.verificationCode).subscribe({
      next: (res) => {
        console.log('Cod validat:', res);
        this.handleRegister();
      },
      error: (err) => {
        console.error('Cod invalid:', err);
        this.tostr.error('Invalid or expired verification code.');
      }
    });
  }

  private handleRegister() {
    this.authService.register(this.registerData).subscribe({
      next: (response) => {
        console.log('Inregistrare reusita', response);
        this.tostr.success('Your account has been created!');
        this.schimbaModul('login');
      },
      error: (err: any) => {
        
        console.error('Eroare la inregistrare', err);
        this.tostr.error('Registration Error');
        this.isVerificationStep = false; 

      }
    });
  }
}
