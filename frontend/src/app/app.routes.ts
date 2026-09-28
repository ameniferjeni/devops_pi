import { Routes } from '@angular/router';
import { AppLayoutComponent } from './components/app-layout.component';
import { DashboardComponent } from './components/dashboard.component';
import { PfeComponent } from './components/pfe.component';
import { ChatbotComponent } from './components/chatbot.component';

export const routes: Routes = [
  {
    path: '',
    component: AppLayoutComponent,
    children: [
      { path: '', component: DashboardComponent },
      { path: 'pfe', component: PfeComponent },
      { path: 'chatbot', component: ChatbotComponent }
    ]
  },
  { path: '**', redirectTo: '' }
];
