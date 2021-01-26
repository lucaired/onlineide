import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {LoginComponent} from './login.component';
import {RouterModule} from '@angular/router';
import {NzButtonModule} from "ng-zorro-antd/button";
import {NzLayoutModule} from 'ng-zorro-antd/layout';



@NgModule({
  declarations: [
    LoginComponent
  ],
    imports: [
        CommonModule,
        RouterModule.forChild([
            {
                path: '',
                component: LoginComponent,
            },
        ]),
        NzButtonModule,
        NzLayoutModule,
    ]
})
export class LoginModule {
}
