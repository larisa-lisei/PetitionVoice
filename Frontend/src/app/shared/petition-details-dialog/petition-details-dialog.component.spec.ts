import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PetitionDetailsDialogComponent } from './petition-details-dialog.component';

describe('PetitionDetailsDialogComponent', () => {
  let component: PetitionDetailsDialogComponent;
  let fixture: ComponentFixture<PetitionDetailsDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PetitionDetailsDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PetitionDetailsDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
