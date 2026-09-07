import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PetitionCardsSectionComponent } from './petition-cards-section.component';

describe('PetitionCardsSectionComponent', () => {
  let component: PetitionCardsSectionComponent;
  let fixture: ComponentFixture<PetitionCardsSectionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PetitionCardsSectionComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PetitionCardsSectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
