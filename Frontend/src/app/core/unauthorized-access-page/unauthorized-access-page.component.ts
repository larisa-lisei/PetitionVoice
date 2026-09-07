import { Component } from '@angular/core';
import { RouterLink, RouterModule } from '@angular/router';

@Component({
  selector: 'app-unauthorized-access-page',
  standalone: true,
  imports: [RouterLink, RouterModule],
  templateUrl: './unauthorized-access-page.component.html',
  styleUrl: './unauthorized-access-page.component.css'
})
export class UnauthorizedAccessPageComponent {
}
