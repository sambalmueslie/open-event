import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {HomeRoutingModule} from './home-routing.module';
import {NavigationComponent} from './navigation/navigation.component';
import {LayoutModule} from '@angular/cdk/layout';
import {MaterialModule} from "../material/material.module";
import {NavItemComponent} from './nav-item/nav-item.component';
import {TranslateModule} from "@ngx-translate/core";
import {FlexLayoutModule} from "@angular/flex-layout";
import { ConfirmLogoutDialogComponent } from './confirm-logout-dialog/confirm-logout-dialog.component';


@NgModule({
  declarations: [NavigationComponent, NavItemComponent, ConfirmLogoutDialogComponent],
  imports: [
    CommonModule,
    HomeRoutingModule,
    LayoutModule,
    MaterialModule,
    TranslateModule,
    FlexLayoutModule
  ]
})
export class HomeModule {
}
