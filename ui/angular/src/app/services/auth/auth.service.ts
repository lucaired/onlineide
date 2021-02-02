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
    this.http.get(USER).pipe(
      map(res => res),
    ).subscribe((res: any) => {
      if (res?.ok && res.principal) {
        this.userName$.next(res.principal);
        this._isAuthenticated$.next(true);
      }
    });
  }

  get isAuthenticated$(): BehaviorSubject<boolean> {
    return this._isAuthenticated$;
  }

  public login() {
    const redirectUrl = `${window.location.origin}/login`;
    console.log(redirectUrl);
    window.location.href = redirectUrl;
  }

  public logout() {
    this.http.post(environment.api.logout, {}).subscribe(res => console.log(res));
    this._isAuthenticated$.next(false);
  }
}
