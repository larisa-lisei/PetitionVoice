import { Component, Input } from '@angular/core';
import { PetitionResponse } from '../../services/models/petition.model';

@Component({
  selector: 'app-petition-cards-section',
  standalone: false,
  templateUrl: './petition-cards-section.component.html',
  styleUrl: './petition-cards-section.component.css'
})
export class PetitionCardsSectionComponent {
  @Input() petitions: PetitionResponse[] = [];
}
