import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService } from '../services/user/user.service';
import { SnackbarService } from '../services/snackbar.service';
import { Router } from '@angular/router';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { MatDialogRef } from '@angular/material/dialog';
import { GlobalConstant } from '../shared/accordion/global-constanst';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss'],
})
export class SignupComponent implements OnInit {
  passwordShow = true;
  retypePasswordShow = true;
  signupForm: any = FormGroup;
  responseMessage: any;

  constructor(
    private formBuilder: FormBuilder,
    private userService: UserService,
    private snackBarService: SnackbarService,
    private router: Router,
    private ngxUiService: NgxUiLoaderService,
    public dialogRef: MatDialogRef<SignupComponent>
  ) {}

  ngOnInit(): void {
    this.signupForm = this.formBuilder.group({
      name: [
        null,
        [Validators.required,
        Validators.pattern(GlobalConstant.nameRegex)]
      ],
      email: [
        null,
        [Validators.required,
        Validators.pattern(GlobalConstant.emailRegex)]
      ],
      contactNumber: [
        null,
        [Validators.required,
        Validators.pattern(GlobalConstant.contactNumberRegex)]
      ],
      password: [null, Validators.required],
      retypePassword: [null, Validators.required],
    });
  }

  validateSubmit() {
    if (
      this.signupForm.controls['password'].value !=
      this.signupForm.controls['retypePassword'].value
    ) {
      return true;
    } else return false;
  }

  handleSubmit() {
    this.ngxUiService.start();
    var formData = this.signupForm.value;
    var data = {
      name: formData.name,
      email: formData.email,
      contactNumber: formData.contactNumber,
      password: formData.password,
    };
    this.userService.signup(data).subscribe((response: any) => {
      this.ngxUiService.stop();
      this.dialogRef.close();
      this.responseMessage = response?.message;
      this.snackBarService.openSnackBar(this.responseMessage, "");
      this.router.navigate(['/']);
    }, (error) => {
      this.ngxUiService.stop();
      if(error.error?.message){
        this.responseMessage = error.error?.message;
      }
      else{
        this.responseMessage = GlobalConstant.genericError;
      }
      console.log('logging', error)
      this.snackBarService.openSnackBar(this.responseMessage, GlobalConstant.error)
    });
  }
}
