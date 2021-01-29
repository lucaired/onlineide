import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {IdeComponent} from './ide.component';
import {RouterModule} from '@angular/router';
import {MonacoEditorModule} from 'ngx-monaco-editor';
import {FormsModule} from '@angular/forms';
import {NzLayoutModule} from 'ng-zorro-antd/layout';
import {NzMenuModule} from 'ng-zorro-antd/menu';
import {NzButtonModule} from 'ng-zorro-antd/button';
import {NzModalModule} from 'ng-zorro-antd/modal';
import {NzInputModule} from 'ng-zorro-antd/input';
import {NzTabsModule} from 'ng-zorro-antd/tabs';
import {NzGridModule} from 'ng-zorro-antd/grid';
import {NzIconModule} from 'ng-zorro-antd/icon';
import {NzMessageModule} from 'ng-zorro-antd/message';


@NgModule({
  declarations: [IdeComponent],
  imports: [
    CommonModule,
    RouterModule.forChild([
      {
        path: '',
        component: IdeComponent,
      },
    ]),
    FormsModule,
    MonacoEditorModule,
    NzLayoutModule,
    NzMenuModule,
    NzButtonModule,
    NzModalModule,
    NzInputModule,
    NzTabsModule,
    NzGridModule,
    NzIconModule,
    NzMessageModule,
  ]
})
export class IdeModule {
}
