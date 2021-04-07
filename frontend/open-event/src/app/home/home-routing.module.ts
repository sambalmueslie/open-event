import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {NavigationComponent} from "./navigation/navigation.component";

@NgModule({
  imports: [RouterModule.forChild(
    [
      {path: '', component: NavigationComponent},
    ]
  )],
  exports: [RouterModule]
})
export class HomeRoutingModule {
}
