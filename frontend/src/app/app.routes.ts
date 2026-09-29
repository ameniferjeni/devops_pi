import { Routes } from '@angular/router';
import { AppLayoutComponent } from './components/app-layout.component';
import { DashboardComponent } from './components/dashboard.component';
import { PfeComponent } from './components/pfe.component';
import { ChatbotComponent } from './components/chatbot.component';
import { LoginComponent } from './components/login.component';
import { AuthGuard } from './guards/auth.guard';

export const routes: Routes = [
  { 
    path: 'login', 
    component: LoginComponent 
  },
  {
    path: '',
    component: AppLayoutComponent,
    canActivate: [AuthGuard.isLoggedIn],
    children: [
      { path: '', component: DashboardComponent },
      { path: 'pfe', component: PfeComponent },
      { path: 'chatbot', component: ChatbotComponent }
    ]
  },
  { path: '**', redirectTo: 'login' }
];

