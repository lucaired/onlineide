import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "@env";
import {Observable} from "rxjs";

const USER = environment.api.user;

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  // /users/:id
  // curl --header "PRIVATE-TOKEN: <your_access_token>" "https://gitlab.lrz.de/api/v4/search?scope=users&search=doe"
  constructor(
    private http: HttpClient
  ) {
    this.getUser().subscribe((res: any) => {
      console.log(res.principal);
    });
  }

  public getUser(): Observable<any> {
    return this.http.get(USER);
  }
}
