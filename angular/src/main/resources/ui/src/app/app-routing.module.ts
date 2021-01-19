import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {AuthGuard} from '@services/auth/auth.guard';

const routes: Routes = [
  {
    path: 'login',
    loadChildren: () =>
      import('./components/login/login.module').then((m) => m.LoginModule),
  },
  {
    path: 'projects',
    loadChildren: () =>
      import('./components/project/project.module').then((m) => m.ProjectModule),
    canActivate: [AuthGuard]
  },
  {path: '', redirectTo: 'projects', pathMatch: 'full'},
  // {path: '**', redirectTo: ''},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
