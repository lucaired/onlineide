import {Component, OnInit} from '@angular/core';
import {DarkModeService} from '@services/dark-mode/dark-mode.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {



  constructor(
    public darkModeService: DarkModeService,
  ) {

  }

  ngOnInit(): void {

  }
}
