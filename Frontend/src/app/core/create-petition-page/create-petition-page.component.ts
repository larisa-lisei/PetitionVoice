import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  Validators,
  ReactiveFormsModule,
} from '@angular/forms';
import { CommonModule } from '@angular/common';
import { PetitionState } from '../../services/models/petition.model';
import { PetitionService } from '../../services/petition.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-create-petition-page',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './create-petition-page.component.html',
  styleUrl: './create-petition-page.component.css',
})
export class CreatePetitionPageComponent implements OnInit {
  petitionForm!: FormGroup;
  selectedFile: File | null = null;
  previewUrl: string | null = null;

  constructor(
    private formBuilder: FormBuilder,
    private petitionService: PetitionService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.petitionForm = this.formBuilder.group({
      title: ['', Validators.minLength(2)],
      category: ['', Validators.required],
      description: ['', [Validators.required, Validators.minLength(20)]],
      goal: [100, [Validators.required, Validators.min(1)]],
      expirationDate: ['', Validators.required],
    });
    const state = history.state;
    if (state && state.petitionData) {
      this.populateForm(state.petitionData);
    }
  }

  populateForm(data: any) {
    let formattedDate = '';
    if (data.expirationDate) {
      const dateObj = new Date(data.expirationDate);
      formattedDate = dateObj.toISOString().split('T')[0];
    }

    this.petitionForm.patchValue({
      title: data.title,
      category: data.category,
      description: data.description,
      goal: data.goal,
      expirationDate: formattedDate,
    });
  }

  onFileSelected(event: any) {
    const file: File = event.target.files[0];
    if (file) {
      this.selectedFile = file;
      
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.previewUrl = e.target.result;
      };
      reader.readAsDataURL(file);
    }
  }

  clearImage() {
    this.selectedFile = null;
    this.previewUrl = null;
  }
  
  onSubmit(): void {
    if (this.petitionForm.valid && this.selectedFile){
      const petitionData = this.petitionForm.value;

      this.petitionService.createPetition(petitionData).subscribe({
        next: (response) => {
          console.log('Petitia s-a creat cu succes!', response);
          const newPetitionId = response.id;

          this.petitionService.uploadImage(newPetitionId, this.selectedFile!).subscribe({
              next: () => {
                this.router.navigate(['/petitions']);
              },
              error: (err) => {
                console.error('Eroare imagine', err);
                alert('Petitia a fost creata, dar imaginea a avut o eroare.');
                this.router.navigate(['/petitions']);
              }
          });
        },

        error: (err) =>{
          console.error('Eroare la creare petitiei: ', err);
          alert('Something went wrong. Try again.');
        }
      });
    }
    else{
      alert('This petition is not valid');
    }
  }
}
