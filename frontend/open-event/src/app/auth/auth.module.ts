import {APP_INITIALIZER, NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {KeycloakAngularModule, KeycloakService} from "keycloak-angular";
import {initializeKeycloak} from "./app-init";
import {AuthService} from "./auth.service";
import {AuthGuard} from "./auth.guard";


@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    KeycloakAngularModule,
  ],
  providers: [
    AuthService,
    {
      provide: APP_INITIALIZER,
      useFactory: initializeKeycloak,
      multi: true,
      deps: [KeycloakService]
    },
    AuthGuard
  ]
})
export class AuthModule {
}
