import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { SnackbarService } from 'src/app/services/snackbar.service';
import { UserService } from 'src/app/services/user/user.service';
import { GlobalConstant } from 'src/app/shared/accordion/global-constanst';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.scss']
})
export class ChangePasswordComponent implements OnInit {
  oldPasswordShow = true;
  newPasswordShow = true;
  retypeNewPasswordShow = true;
  responseMessage:any;
  changePasswordForm:any = FormGroup;
  constructor(private userService: UserService,
    private router: Router,
    private dialogRef: MatDialogRef<ChangePasswordComponent>,
    private snackBarService: SnackbarService,
    private ngxLoader: NgxUiLoaderService,
    private formBuilder: FormBuilder
  ) { }

  ngOnInit(): void {
    this.changePasswordForm = this.formBuilder.group({
      oldPassword: [null, Validators.required],
      newPassword: [null, Validators.required],
      retypeNewPassword: [null, Validators.required]
    })
  }

  validatePassword(){
    if(this.changePasswordForm.controls['newPassword'].value != this.changePasswordForm.controls['retypeNewPassword'].value){
      return true;
    }
    else{
      return false;
    }
  }

  handleChangePasswordSubmit(){
    this.ngxLoader.start();
    var formData = this.changePasswordForm.value;
    var data ={
      oldPassword: formData.oldPassword,
      newPassword: formData.newPassword,
      retypeNewPassword: formData.retypeNewPassword
    }
    this.userService.changePassword(data).subscribe((response: any)=>{
      this.ngxLoader.stop();
      this.responseMessage = response?.message;
      this.dialogRef.close();
      this.snackBarService.openSnackBar(this.responseMessage, '');
    }, (error)=>{
      this.ngxLoader.stop();
      if(error.error?.message){
        this.responseMessage = error.error?.message;
      }
      else{
        this.responseMessage = GlobalConstant.genericError;
      }
      this.snackBarService.openSnackBar(this.responseMessage, GlobalConstant.error);
    })
  }

}
