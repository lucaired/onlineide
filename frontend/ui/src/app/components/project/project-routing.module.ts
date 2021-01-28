import {RouterModule, Routes} from '@angular/router';
import {ProjectComponent} from '@components/project/project.component';
import {NgModule} from '@angular/core';

const routes: Routes = [
  {
    path: '',
    component: ProjectComponent,
  },
  {
    path: ':id/edit',
    loadChildren: () =>
      import('../ide/ide.module').then((m) => m.IdeModule),
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ProjectRoutingModule {
}
