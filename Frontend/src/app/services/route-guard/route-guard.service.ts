import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { SnackbarService } from '../snackbar.service';
import { jwtDecode } from 'jwt-decode';
import { GlobalConstant } from 'src/app/shared/accordion/global-constanst';

@Injectable({
  providedIn: 'root'
})
export class RouteGuardService {

  constructor(public router:Router,
    private snackBarService : SnackbarService
  ) { }

  canActivate(route: ActivatedRouteSnapshot): boolean{
    let expectedRoleArray = route.data;
    expectedRoleArray = expectedRoleArray.expectedRole;

    const token:any = localStorage.getItem('token');

    var tokenPayload:any;

    try {
      tokenPayload = jwtDecode(token);
    } catch (error) {
      console.log('logging', error);
      localStorage.clear();
      this.router.navigate(['/'])
    }

    let thisRole = "";

    for(let i = 0; i < expectedRoleArray.length; i++){
      console.log(expectedRoleArray[i])
      if(expectedRoleArray[i] == tokenPayload.role){
          thisRole = tokenPayload.role;
      }
    }

    if(tokenPayload.role == 'admin' || tokenPayload.role == 'user'){
      console.log(thisRole)
      if(tokenPayload.role == thisRole){
          return true;
      }
      this.snackBarService.openSnackBar(GlobalConstant.unauthorized, GlobalConstant.error);
      this.router.navigate(['/cafe/dashboard']);
      return false;
    }
    else{
      this.snackBarService.openSnackBar(GlobalConstant.unauthorized, GlobalConstant.error);
      this.router.navigate(['/']);
      localStorage.clear();
      return false;
    }
  }
}
