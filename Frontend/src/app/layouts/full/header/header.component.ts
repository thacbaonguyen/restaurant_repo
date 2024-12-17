import { Component } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { ChangePasswordComponent } from 'src/app/material-component/dialog/change-password/change-password.component';
import { ConfirmationComponent } from 'src/app/material-component/dialog/confirmation/confirmation.component';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: []
})
export class AppHeaderComponent {
  role:any;

  constructor(private router: Router,
    private dialog: MatDialog
  ) {
  }

  logout(){
    const matDialogConfig = new MatDialogConfig();
    matDialogConfig.data = {
      message: 'đăng xuất!',
      confirmation: true
    }

    const dialogRef = this.dialog.open(ConfirmationComponent, matDialogConfig);
    const sub = dialogRef.componentInstance.onEmitStatusChange.subscribe((response: any)=>{
      dialogRef.close();
      localStorage.clear();
      this.router.navigate(['/']);
    })
  }

  changePassword(){
    const matDialogConfig = new MatDialogConfig();
    matDialogConfig.width = '800px';
    this.dialog.open(ChangePasswordComponent, matDialogConfig);
  }
}
