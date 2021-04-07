import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

const routes: Routes = [
  {path: '', redirectTo: 'home', pathMatch: 'full'},
  {path: 'home', loadChildren: () => import('./home/home.module').then(m => m.HomeModule)},
  {path: 'events', loadChildren: () => import('./events/events.module').then(m => m.EventsModule)},
  {path: 'inquiry', loadChildren: () => import('./inquiry/inquiry.module').then(m => m.InquiryModule)},
  {path: 'structure', loadChildren: () => import('./structure/structure.module').then(m => m.StructureModule)},
  {path: 'message', loadChildren: () => import('./message/message.module').then(m => m.MessageModule)},
  {path: 'profile', loadChildren: () => import('./profile/profile.module').then(m => m.ProfileModule)},
  {path: 'administration', loadChildren: () => import('./administration/administration.module').then(m => m.AdministrationModule)},
  {path: 'imprint', loadChildren: () => import('./imprint/imprint.module').then(m => m.ImprintModule)},
  {path: '**', loadChildren: () => import('./error/error.module').then(m => m.ErrorModule)}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
