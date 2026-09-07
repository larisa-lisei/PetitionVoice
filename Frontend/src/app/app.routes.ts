import { Routes } from '@angular/router';
import { LandingPageComponent } from './core/landing-page/landing-page.component';
import { ViewPetitionsPageComponent } from './core/view-petitions-page/view-petitions-page.component';
import { AboutUsPageComponent } from './core/about-us-page/about-us-page.component';
import { LoginPageComponent } from './core/login-register-page/login-page.component';
import { CreatePetitionPageComponent } from './core/create-petition-page/create-petition-page.component';
import { AuthGuard } from './services/guards/auth.guard';
import { UnauthorizedAccessPageComponent } from './core/unauthorized-access-page/unauthorized-access-page.component';
import { UserDashboardPageComponent } from './core/user-dashboard-page/user-dashboard-page.component';
import { AdminDashboardComponent } from './core/admin-dashboard-page/admin-dashboard-page.component';
import { AdminGuard } from './services/guards/admin.guard';

export const routes: Routes = [
  { path: 'home', component: LandingPageComponent },
  { path: '', component: LandingPageComponent },
  { path: 'petitions', component: ViewPetitionsPageComponent },
  { path: 'about-us', component: AboutUsPageComponent },
  { path: 'login', component: LoginPageComponent },
  

  // rute protejate
  {
    path: 'create-petition',
    component: CreatePetitionPageComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'user-dashboard',
    component: UserDashboardPageComponent,
    canActivate: [AuthGuard],
  },

  { path: 'unauthorized-access', component: UnauthorizedAccessPageComponent },

  {
    path: 'admin-dashboard',
    component: AdminDashboardComponent,
    canActivate: [AdminGuard]
  }

];
