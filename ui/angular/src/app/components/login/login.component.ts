import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {AuthService} from '@services/auth/auth.service';
import {map} from 'rxjs/operators';
import {environment} from '@env';

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

  ngOnInit(): void {
  }

  login() {
    window.location.href = environment.api.login;
  }

  toProjects() {
    this.router.navigateByUrl('/projects');
  }
}
