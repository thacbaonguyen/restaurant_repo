import { Injectable } from '@angular/core';

export interface Menu {
  state: string;
  name: string;
  type: string;
  icon: string;
  role: string;
}
const MENU_ITEMS= [
    {state: 'dashboard', name: 'Dashboard', type: 'link', icon: 'dashboard', role: ''},
    {state: 'category', name: 'Manage Category', type: 'link', icon: 'category', role: 'admin'},
    {state: 'product', name: 'Manage Product', type: 'link', icon: 'product', role: 'admin'},
    {state: 'order', name: 'Manage Order', type: 'link', icon: 'shopping_cart', role: ''},
    {state: 'bill', name: 'Inspect Order', type: 'link', icon: '', role: ''}
]

@Injectable()
export class MenuItems{
    getMenuItem(): Menu[]{
        return MENU_ITEMS;
    }
} 
