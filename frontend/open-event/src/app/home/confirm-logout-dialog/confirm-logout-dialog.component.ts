import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {KeycloakService} from "keycloak-angular";

@Component({
  selector: 'app-confirm-logout-dialog',
  templateUrl: './confirm-logout-dialog.component.html',
  styleUrls: ['./confirm-logout-dialog.component.scss']
})
export class ConfirmLogoutDialogComponent {

  constructor(
    public dialogRef: MatDialogRef<ConfirmLogoutDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: string,
    private service: KeycloakService
  ) {

  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  onYesClick() {
    this.service.logout().then();
  }

}
