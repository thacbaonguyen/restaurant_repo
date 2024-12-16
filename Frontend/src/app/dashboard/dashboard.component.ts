import { Component, AfterViewInit } from '@angular/core';
import { DashboardService } from '../services/dashboard/dashboard.service';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { SnackbarService } from '../services/snackbar.service';
import { GlobalConstant } from '../shared/accordion/global-constanst';
@Component({
	selector: 'app-dashboard',
	templateUrl: './dashboard.component.html',
	styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements AfterViewInit {
	data:any;
	responseMessage :any;
	ngAfterViewInit() { }

	constructor(private dashBoardService: DashboardService,
		private ngxLoader: NgxUiLoaderService,
		private snackBarService: SnackbarService
	) {
		ngxLoader.start();
		this.dashBoardData();
	}
	dashBoardData(){
		this.dashBoardService.getDetails().subscribe((response: any)=>{
			this.ngxLoader.stop();
			this.data = response;
		},(error)=>{
			this.ngxLoader.stop();
			if(error.error?.message){
				this.responseMessage = error.error.message;
			}
			else{
				this.responseMessage = GlobalConstant.genericError;
			}
			this.snackBarService.openSnackBar(this.responseMessage, GlobalConstant.error);
		})
	}

}
