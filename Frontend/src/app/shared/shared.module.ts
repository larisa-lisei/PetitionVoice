import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PetitionCardsSectionComponent } from './petition-cards-section/petition-cards-section.component';
import { PetitionCardComponent } from "./petition-card/petition-card.component";

@NgModule({
  declarations: [
    PetitionCardsSectionComponent,
    PetitionCardComponent
  ],
  imports: [
    CommonModule
],
  exports: [
    PetitionCardsSectionComponent,
  ]
})
export class SharedModule { }
