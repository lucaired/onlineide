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


  constructor(private http: HttpClient) {

  }


  public getDarkMode(): Observable<IDarkMode | any> {
    return this.http.get(`${DARK_MODE}`);
  }
}
