import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '@env';
import {BehaviorSubject, Observable} from 'rxjs';
import {getAuthHeaders} from '@services/auth/headers-util';
import {Router} from '@angular/router';

const USER = environment.api.user;

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private _isAuthenticated$ = new BehaviorSubject(false);
  private userName$ = new BehaviorSubject(null);

  constructor(
    private http: HttpClient,
    private router: Router,
  ) {
    this.fetchAuthenticated();
  }

  get isAuthenticated$(): Observable<boolean | any> {
    return this._isAuthenticated$;
  }

  public logout() {
    this.http.post(environment.api.logout, {}, {headers: getAuthHeaders()}).subscribe(res => console.log(res));
    // this.isAuthenticated$.next(false);
    this.router.navigateByUrl('/login');
    this.fetchAuthenticated();
  }

  public fetchAuthenticated() {
    this.http.get(environment.api.authenticated, {headers: getAuthHeaders()}).subscribe((res: boolean) => {
      this._isAuthenticated$.next(res);
      console.log(`Is authenticated: ${res}`);
      if (res) {
        this.fetchUser();
      }
    });
  }

  public fetchUser() {
    this.http.get(environment.api.user, {headers: getAuthHeaders()}).subscribe((user: { principal: boolean }) => {
      this.userName$.next(user.principal);
    });
  }
}
