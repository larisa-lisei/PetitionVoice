import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../auth.service';



// il putem folosi sau nu


export const AdminGuard = () => {
  let isDesktop = window.navigator.userAgent.toLowerCase().includes('electron');

  const authService = inject(AuthService);
  const router = inject(Router);
  const user = authService.getCurrentUserValue();

  // daca se vrea sa se modifice sa adaugam rol de Admin adaugam user?.role === 'ADMIN' la if

  if (isDesktop) {
    return true; 
  }

  alert("Acces denied! You cannot access this via webpage");
  router.navigate(['/']);
  return false;
};