import { bootstrapApplication } from '@angular/platform-browser';
import { PopupComponent } from './app/extension/popup/popup.component';

bootstrapApplication(PopupComponent)
  .catch(err => console.error(err));
