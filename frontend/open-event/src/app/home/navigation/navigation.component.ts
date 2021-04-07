import {AfterViewInit, ChangeDetectorRef, Component, OnInit, ViewChild} from '@angular/core';
import {BreakpointObserver, Breakpoints} from '@angular/cdk/layout';
import {Observable} from 'rxjs';
import {filter, map, shareReplay, withLatestFrom} from 'rxjs/operators';
import {NavMenuItem} from "./nav-menu-item";
import {AuthService} from "../../auth/auth.service";
import {MessageService} from "../../message/message.service";
import {User} from "../../auth/user";
import {TranslateService} from "@ngx-translate/core";
import {MatSidenav} from "@angular/material/sidenav";
import {NavigationEnd, Router} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import {MatSelectChange} from "@angular/material/select";
import {ConfirmLogoutDialogComponent} from "../confirm-logout-dialog/confirm-logout-dialog.component";

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.scss']
})
export class NavigationComponent implements OnInit, AfterViewInit {

  items: NavMenuItem[] = [
    new NavMenuItem('/home', 'MENU.Home', 'home'),
    new NavMenuItem('/event', 'EVENT.Type', 'event_note'),
    new NavMenuItem('/inquiry', 'INQUIRY.Type', 'question_answer'),
    new NavMenuItem('/structure', 'STRUCT.Type', 'ballot', AuthService.MANAGER),
    new NavMenuItem('/message', 'MENU.Message', 'mail', AuthService.EDITOR, true, this.messageService.getUserUnreadMessageCount()),
    new NavMenuItem('/profile', 'MENU.Profile', 'person'),
    new NavMenuItem('/administration', 'MENU.Administration', 'settings_applications', AuthService.ADMIN),
    new NavMenuItem('/imprint', 'MENU.Imprint', 'contact_support'),
  ];

  accessibleItems: NavMenuItem[] = [];
  collapsed: boolean = false;
  user: User = null;
  lang: string = 'de';
  @ViewChild('drawer') drawer: MatSidenav;

  isHandset$: Observable<boolean> = this.breakpointObserver.observe(Breakpoints.Handset)
    .pipe(
      map(result => result.matches),
      shareReplay()
    );

  constructor(
    private authService: AuthService,
    private messageService: MessageService,
    private translate: TranslateService,
    private breakpointObserver: BreakpointObserver,
    private changeDetectorRef: ChangeDetectorRef,
    private router: Router,
    public dialog: MatDialog
  ) {
    translate.setDefaultLang('en');
    translate.use(this.lang);

    router.events.pipe(
      withLatestFrom(this.isHandset$),
      filter(([a, b]) => b && a instanceof NavigationEnd)
    ).subscribe(_ => this.drawer.close());
  }

  ngOnInit() {
    this.accessibleItems = this.items.filter(item => item.isAccessible(this.authService));
  }

  ngAfterViewInit() {
    this.changeDetectorRef.detectChanges();
    this.authService.getUserObservable().subscribe(user => this.user = user);
  }

  toggleCollapsed() {
    this.collapsed = !this.collapsed;
  }

  logout() {
    const dialogRef = this.dialog.open(ConfirmLogoutDialogComponent, {
      width: '250px',
      data: ''
    });
  }

  changeLang(event: MatSelectChange) {
    let value = event.value;
    this.translate.use(value)
  }

  selectLang(value: string) {
    this.lang = value;
    this.translate.use(this.lang)
  }

  submitIssue() {
    (window as any).showCollectorDialog()
  }
}
