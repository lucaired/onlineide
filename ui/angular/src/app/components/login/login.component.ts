import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {AuthService} from '@services/auth/auth.service';
import {map} from 'rxjs/operators';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  constructor(
    private router: Router,
    public authService: AuthService
  ) {
  }

  public login() {
    this.authService.isAuthenticated$.subscribe((authenticated) => {
      console.log(authenticated);
      if (authenticated) {
        this.router.navigate(['/projects']);
      } else {
        this.authService.login();
      }
    });
  }

  ngOnInit(): void {
  }
}
