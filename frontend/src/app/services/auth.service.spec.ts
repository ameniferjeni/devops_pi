import { TestBed } from '@angular/core/testing';
import { AuthService } from './auth.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AuthService]
    });
    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
    localStorage.clear();
  });

  afterEach(() => {
    httpMock.verify();
    localStorage.clear();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should store session on successful login', () => {
    const dummyResponse = {
      token: 'jwt.token.valide',
      email: 'admin@codingfactory.tn',
      role: 'ADMIN' as const,
      prenom: 'Admin'
    };

    service.login('admin@codingfactory.tn', 'admin123').subscribe(res => {
      expect(res.token).toEqual('jwt.token.valide');
      expect(service.isLoggedIn()).toBeTrue();
      expect(service.isAdmin()).toBeTrue();
    });

    const req = httpMock.expectOne('/api/auth/login');
    expect(req.request.method).toBe('POST');
    req.flush(dummyResponse);
  });

  it('should fallback gracefully to demo mode on network error', () => {
    service.login('candidat@codingfactory.tn', 'candidat123').subscribe(res => {
      expect(res.role).toEqual('CANDIDAT');
      expect(service.isLoggedIn()).toBeTrue();
    });

    const req = httpMock.expectOne('/api/auth/login');
    req.error(new ProgressEvent('Network error'));
  });

  it('should clear session on logout', () => {
    localStorage.setItem('codingfactory_jwt_token', 'token_123');
    service.logout();
    expect(service.isLoggedIn()).toBeFalse();
    expect(service.getToken()).toBeNull();
  });
});
