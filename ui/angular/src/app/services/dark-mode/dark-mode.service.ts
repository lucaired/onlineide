import {Injectable} from '@angular/core';
import {environment} from '@env';
import {HttpClient} from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';
import {Theme} from '@models/theme.model';
import {IDarkMode} from '@models/dark-mode.model';


const DARK_MODE = environment.api.darkMode;

@Injectable({
  providedIn: 'root'
})
export class DarkModeService {

  private _theme$: BehaviorSubject<Theme> = new BehaviorSubject<Theme>(Theme.LIGHT);

  constructor(private http: HttpClient) {
    setInterval(() => {
      this.getDarkMode().subscribe((res: IDarkMode) => {
        if (res?.enabled) {
          this._theme$.next(Theme.DARK);
        } else {
          this._theme$.next(Theme.LIGHT);
        }
      });
    }, 3000);
  }

  get theme$(): BehaviorSubject<Theme> {
    return this._theme$;
  }


  public getDarkMode(): Observable<IDarkMode | any> {
    return this.http.get(`${DARK_MODE}`);
  }
}
