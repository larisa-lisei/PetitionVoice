import { Component, OnInit } from '@angular/core';
import { PetitionResponse } from '../../services/models/petition.model';
import { PetitionService } from '../../services/petition.service';
import { FormControl } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { Page } from '../../services/models/page.model';
import { PetitionState } from '../../services/models/petition.model';
import { ActivatedRoute, Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { PetitionDetailsDialogComponent } from '../../shared/petition-details-dialog/petition-details-dialog.component';

@Component({
  selector: 'app-view-petitions-page',
  standalone: false,
  templateUrl: './view-petitions-page.component.html',
  styleUrl: './view-petitions-page.component.css'
})
export class ViewPetitionsPageComponent implements OnInit {

  petitions: PetitionResponse[] = [];

  keyword = '';
  orderBy = 'recentlyadded';

  page = 0;
  size = 16;
  hasNext = false;

  private openedForId: number | null = null;
  readonly API_URL = 'http://localhost:8079'; 

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private dialog: MatDialog,
    private petitionService: PetitionService, 
    private toastr: ToastrService) {}

  ngOnInit(): void {
    this.applyCategories();
    this.loadPetitions();

    this.route.queryParamMap.subscribe(q => {
      const openParam = q.get('open');
      if(!openParam) return;

      const id = Number(openParam);
      if(!id) return;

      this.openDialogFromQuery(id);
    });
  }

  openDialogFromQuery(id: number) {
    //prevent opening dialog more times at once
    if(this.openedForId === id) return;
    this.openedForId = id;

    this.petitionService.getPetitionDetails(id).subscribe({
    next: (petition) => {
      this.petitionService.getPetitionSignatures(id).subscribe({
        next: () => {
          let imagePath = '/img/implicit_petition_picture.jpg'; 
          if (petition.imageUrl) {
             imagePath = this.API_URL + petition.imageUrl;
          }

          const ref = this.dialog.open(PetitionDetailsDialogComponent, {
            minWidth: '80vw',
            maxHeight: '90vh',
            autoFocus: false,
            restoreFocus: false,
            data: {
              id: petition.id,
              title: petition.title,
              img: imagePath,
              category: petition.category,
              count: petition.currentSignatures,
              goal: petition.goal,
              description: petition.description,
              expirationDate: petition.expirationDate,
              creator: petition.creatorName ?? 'Unknown',
              creationDate: petition.creationDate,
            },
          });

          // go back to /petitions when closing popup
          ref.afterClosed().subscribe(() => {
            this.openedForId = null;

            //stay on page /petitions, but remove open query param
            this.router.navigate([],
              { relativeTo: this.route,
                queryParams: { open: null },
                queryParamsHandling: 'merge',
                replaceUrl: true,
              });
          });
        },
        error: () => this.toastr.error('Error getting petition details.'),
      });
    },
    error: () => {
      this.toastr.error('Petition not found.');
      this.router.navigate(['/petitions'], { replaceUrl: true });
      },
    });
  }

  selectedCategoriesControl = new FormControl<string[]>([], { nonNullable: true });

  applyCategories() {
    this.selectedCategoriesControl.valueChanges.subscribe(() => {
      this.page = 0;
      this.loadPetitions();
    });
  }

  loadPetitions() {
    const categories = this.selectedCategoriesControl.value;

    this.petitionService.getAllApprovedPetitions({
      keyword: this.keyword || undefined,
      categories: categories.length ? categories : undefined,
      orderBy: this.orderBy,
      page: this.page,
      size: this.size
    }).subscribe({
      next: (pageData: Page<PetitionResponse>) => {
        this.petitions = pageData.content.filter
        (p => p.state === PetitionState.APPROVED).map(petition => {
          if (petition.imageUrl) {
             petition.imageUrl = this.API_URL + petition.imageUrl;
          }
          return petition;
        });
        
        this.hasNext = !pageData.last; 
        
        console.log(`Total pagini: ${pageData.totalPages}`);
      },
      error: (err) => {
        this.petitions = [];
        this.hasNext = false;
        this.toastr.error('Error getting petitions');
      }
    });
  }


  onSearch() {
    this.page = 0;
    this.loadPetitions();
  }

  clearSearch() {
    this.keyword='';
    this.onSearch();
  }

  onOrderChange(orderBy: string) {
    this.orderBy = orderBy;
    this.page = 0;
    this.loadPetitions();
  }

  nextPage() {
    if(this.hasNext) {
      this.page++;
      this.loadPetitions();
    }
  }

  prevPage() {
    if(this.page > 0) {
      this.page--;
      this.loadPetitions();
    }
  }

}
