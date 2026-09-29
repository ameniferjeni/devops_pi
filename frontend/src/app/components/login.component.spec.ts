import { TestBed } from '@angular/core/testing';
import { LoginComponent } from './login.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { AuthService } from '../services/auth.service';

describe('LoginComponent', () => {
  let component: LoginComponent;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LoginComponent, HttpClientTestingModule, RouterTestingModule],
      providers: [AuthService]
    }).compileComponents();

    const fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create login component', () => {
    expect(component).toBeTruthy();
  });

  it('should fill admin credentials on loginAsAdmin()', () => {
    component.loginAsAdmin();
    expect(component.loginData.email).toBe('admin@codingfactory.tn');
    expect(component.loginData.password).toBe('admin123');
  });

  it('should fill candidat credentials on loginAsCandidat()', () => {
    component.loginAsCandidat();
    expect(component.loginData.email).toBe('candidat@codingfactory.tn');
    expect(component.loginData.password).toBe('candidat123');
  });
});
