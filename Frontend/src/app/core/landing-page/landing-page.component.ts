import { Component, OnInit } from '@angular/core';
import { PetitionService } from '../../services/petition.service';
import { PetitionResponse } from '../../services/models/petition.model';
import { FormControl } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-landing-page',
  standalone: false,
  templateUrl: './landing-page.component.html',
  styleUrl: './landing-page.component.css'
})
export class LandingPageComponent implements OnInit {
  petitions: PetitionResponse[] = [];

  readonly API_URL = 'http://localhost:8079'; 

  activeOrder: 'recentlyadded' | 'trending' | 'neargoal' = 'recentlyadded';

  constructor(private petitionService: PetitionService, private toastr: ToastrService) {}

  ngOnInit(): void {
    this.loadPetitions();
  }

  changeOrder(orderBy: 'recentlyadded' | 'trending' | 'neargoal'): void {
    this.activeOrder = orderBy;
    this.loadPetitions();
  }

  private loadPetitions(): void {
    this.petitionService.getAllApprovedPetitions({
      orderBy: this.activeOrder,
      size: 8
    }).subscribe({
      next: (pageData) => {
        this.petitions = pageData.content
          .filter(p => p.state === 'APPROVED')
          .map(petition => {
             if (petition.imageUrl) {
               petition.imageUrl = this.API_URL + petition.imageUrl;
             }
             return petition;
          });
      },
      
      error: (err) => {
        this.petitions = [];
        this.toastr.error('Error getting petitions');
      }
    });
  }

}
