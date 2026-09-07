import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreatePetitionPageComponent } from './create-petition-page.component';

describe('CreatePetitionPageComponent', () => {
  let component: CreatePetitionPageComponent;
  let fixture: ComponentFixture<CreatePetitionPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreatePetitionPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreatePetitionPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
