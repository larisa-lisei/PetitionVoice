import { CommonModule } from '@angular/common';
import { Component, NgZone, OnDestroy, OnInit } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressBarModule } from '@angular/material/progress-bar';

declare const chrome: any;

@Component({
  selector: 'app-popup',
  standalone: true,
  imports: [CommonModule, MatProgressBarModule, MatIconModule],
  templateUrl: './popup.component.html',
  styleUrl: './popup.component.css'
})
export class PopupComponent implements OnInit, OnDestroy {
  petition: any = null;
  lastUpdated: number | null = null;

  readonly API_URL = 'http://localhost:8079'; 


  private onChangedHandler: ((changes: any, areName: string) => void) | null = null;

  constructor(private zone: NgZone) {}

  ngOnInit(): void {
    //read current value from storage
    chrome.storage.local.get(['petitionOfTheDay', 'updatedAt'], (res: any) => {
      this.zone.run(() => {
         this.petition = res?.petitionOfTheDay ?? null;
         this.lastUpdated = res?.updatedAt ?? null;
      });
    });

    //listener for changes in storage while popup is open
    this.onChangedHandler = (changes: any, areName: string) => {
      if(areName !== 'local') return;

      if(changes.petitionOfTheDay) {
        this.zone.run(() => {
          this.petition = changes.petitionOfTheDay.newValue ?? null;
        });
      }

      if(changes.updatedAt) {
        this.zone.run(() => {
          this.lastUpdated = changes.updatedAt.newValue ?? null;
        });
      }
    };

    chrome.storage.onChanged.addListener(this.onChangedHandler);
  }

  ngOnDestroy(): void {
    if(this.onChangedHandler) {
      chrome.storage.onChanged.removeListener(this.onChangedHandler);
      this.onChangedHandler = null;
    }
  }

  openInApp(): void {
    if(!this.petition?.id) return;

    const url = `http://localhost:4200/petitions?open=${this.petition.id}`;
    chrome.tabs.create({ url });
  }

}
