import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ProjectComponent} from './project.component';
import {ProjectRoutingModule} from '@components/project/project-routing.module';
import {NzIconModule} from "ng-zorro-antd/icon";
import {NzTableModule} from 'ng-zorro-antd/table';
import {NzDividerModule} from 'ng-zorro-antd/divider';
import {NzInputModule } from 'ng-zorro-antd/input';
import {NzModalModule} from "ng-zorro-antd/modal";
import {NzButtonModule} from "ng-zorro-antd/button";
import {FormsModule} from "@angular/forms";
import {NzLayoutModule} from 'ng-zorro-antd/layout';

@NgModule({
  declarations: [ProjectComponent],
    imports: [
        CommonModule,
        ProjectRoutingModule,
        NzIconModule,
        NzTableModule,
        NzDividerModule,
        NzInputModule,
        NzModalModule,
        NzButtonModule,
        NzIconModule,
        FormsModule,
        NzLayoutModule,
    ]
})
export class ProjectModule {
}
