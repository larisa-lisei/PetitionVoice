import { Component, Input } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { PetitionDetailsDialogComponent } from '../petition-details-dialog/petition-details-dialog.component';
import { PetitionResponse } from '../../services/models/petition.model';
import { PetitionService } from '../../services/petition.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-petition-card',
  standalone: false,
  templateUrl: './petition-card.component.html',
  styleUrl: './petition-card.component.css'
})
export class PetitionCardComponent {

  @Input() petition!: PetitionResponse;

  constructor(private dialog: MatDialog, private petitionService: PetitionService, private toastr: ToastrService) {}

  openDetailsDialog(){
    this.petitionService.getPetitionSignatures(this.petition.id).subscribe({
      next: () => {
        this.dialog.open(PetitionDetailsDialogComponent, {
          minWidth: '80vw',
          maxHeight: '90vh',
          autoFocus: false,
          restoreFocus: false,
          data: {
            id: this.petition.id ,
            title: this.petition.title,
            img: this.petition.imageUrl ? this.petition.imageUrl : '/img/implicit_petition_picture.jpg',
            category: this.petition.category,
            count: this.petition.currentSignatures,
            goal: this.petition.goal,
            description: this.petition.description,
            expirationDate: this.petition.expirationDate,
            creator: this.petition.creatorName != null ? this.petition.creatorName : 'Unknown',
            creationDate: this.petition.creationDate,
          }
      });
    },
    error: () => {
        this.toastr.error('Error getting petition details.');
      }
    });
  }
}
