import {Component, Input, OnInit} from '@angular/core';
import {NavMenuItem} from "../navigation/nav-menu-item";

@Component({
  selector: 'app-nav-item',
  templateUrl: './nav-item.component.html',
  styleUrls: ['./nav-item.component.scss']
})
export class NavItemComponent implements OnInit {

  @Input()
  collapsed: boolean = false;

  @Input("item")
  set data(value: NavMenuItem) {
    this.item = value;
    this.updateSubscription()
  }

  item: NavMenuItem;
  badgeValue: number = 0;
  private subscription: any;

  constructor() {
  }

  ngOnInit() {
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  private updateSubscription() {
    if (!this.item.hasBadge) return;
    // this.subscription = polling(this.item.badgeObservable, {interval: 300000})
    //   .subscribe(data => this.badgeValue = data);
  }

}
