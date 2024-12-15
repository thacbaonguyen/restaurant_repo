import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { SnackbarService } from '../services/snackbar.service';
import { UserService } from '../services/user.service';
import { GlobalConstant } from '../shared/accordion/global-constanst';
import { error } from 'console';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.scss']
})
export class ForgotPasswordComponent implements OnInit {
  forgotPasswordFormGroup:any =  FormGroup;
  reponseMessage:any;

  constructor(private formBuilder: FormBuilder,
    private dialogRef: MatDialogRef<ForgotPasswordComponent>,
    private ngxLoaderService: NgxUiLoaderService,
    private snackBarService: SnackbarService,
    private userService: UserService
  ) { }

  ngOnInit(): void {
    this.forgotPasswordFormGroup = this.formBuilder.group({
      email: [null, [Validators.required, Validators.pattern(GlobalConstant.emailRegex)]]
    })
  }

  handleSubmit(){
    this.ngxLoaderService.start();
    var formData = this.forgotPasswordFormGroup.value;
    var data = {
      email: formData.email
    }
    this.userService.forgotPassword(data).subscribe((response: any) =>{
      this.ngxLoaderService.stop();
      this.reponseMessage = response?.message;
      this.dialogRef.close();
      this.snackBarService.openSnackBar(this.reponseMessage, "")
    },(error) =>{
      this.ngxLoaderService.stop();
      if(error.error?.message){
        this.reponseMessage = error.error?.message;
      }
      else{
        this.reponseMessage = GlobalConstant.genericError;
      }
      this.snackBarService.openSnackBar(this.reponseMessage, GlobalConstant.error);
    })
  }

}
