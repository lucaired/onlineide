import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '@env';
import {BehaviorSubject, Observable} from 'rxjs';
import {catchError, map} from 'rxjs/operators';

const USER = environment.api.user;

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  // /users/:id
  // curl --header "PRIVATE-TOKEN: <your_access_token>" "https://gitlab.lrz.de/api/v4/search?scope=users&search=doe"

  private _isAuthenticated$ = new BehaviorSubject(false);
  private userName$ = new BehaviorSubject(null);

  constructor(
    private http: HttpClient
  ) {
    this.fetchAuthenticated();
  }

  get isAuthenticated$(): Observable<boolean | any> {
    return this._isAuthenticated$;
  }

  public logout() {
    this.http.post(environment.api.logout, {}).subscribe(res => console.log(res));
    // this.isAuthenticated$.next(false);
    this.fetchAuthenticated();
  }

  public fetchAuthenticated() {
    this.http.get(environment.api.authenticated).subscribe((res: boolean) => {
      this._isAuthenticated$.next(res);
      console.log(`Is authenticated: ${res}`);
      if (res) {
        this.fetchUser();
      }
    });
  }

  public fetchUser() {
    this.http.get(environment.api.user).subscribe((user: { principal: boolean }) => {
      this.userName$.next(user.principal);
    });
  }
}
