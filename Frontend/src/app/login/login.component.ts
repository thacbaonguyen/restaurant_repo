import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { SnackbarService } from '../services/snackbar.service';
import { UserService } from '../services/user/user.service';
import { MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { GlobalConstant } from '../shared/accordion/global-constanst';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  hidePassword = true;
  loginForm:any = FormGroup;
  responseMessage:any; 

  constructor(private formBuilder: FormBuilder,
    private ngxLoader: NgxUiLoaderService,
    private snackBarService: SnackbarService,
    private userService: UserService,
    public dialogRef: MatDialogRef<LoginComponent>,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loginForm = this.formBuilder.group({
      email: [null, [Validators.required, Validators.pattern(GlobalConstant.emailRegex)]],
      password: [null, Validators.required]
    })
  }

  handleSubmit(){
    this.ngxLoader.start();
    var dataForm = this.loginForm.value;
    var data = {
      email: dataForm.email,
      password: dataForm.password
    }
    this.userService.login(data).subscribe((response: any)=>{
      this.ngxLoader.stop();
      this.dialogRef.close();
      localStorage.setItem('token', response.token);
      this.router.navigate(['/cafe/dashboard']);
      
    },(error)=>{
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
