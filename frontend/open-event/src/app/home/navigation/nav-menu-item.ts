import {EMPTY, Observable} from "rxjs";
import {AuthService} from "../../auth/auth.service";

export class NavMenuItem {
  constructor(
    public url: string,
    public text: string,
    public icon: string,
    public role: string = AuthService.USER,
    public hasBadge: boolean = false,
    public badgeObservable: Observable<number> = EMPTY,
  ) {
  }

  isAccessible(service: AuthService): boolean {
    return service.hasRole(this.role);
  }
}
