import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewPetitionsPageComponent } from './view-petitions-page.component';

describe('ViewPetitionsPageComponent', () => {
  let component: ViewPetitionsPageComponent;
  let fixture: ComponentFixture<ViewPetitionsPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ViewPetitionsPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ViewPetitionsPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
