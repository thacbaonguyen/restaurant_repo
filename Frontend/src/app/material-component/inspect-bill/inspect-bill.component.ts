import { Component, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { BillService } from 'src/app/services/bill/bill/bill.service';
import { SnackbarService } from 'src/app/services/snackbar.service';
import { GlobalConstant } from 'src/app/shared/accordion/global-constanst';
import { ViewBillProductsComponent } from '../dialog/view-bill-products/view-bill-products.component';
import { ConfirmationComponent } from '../dialog/confirmation/confirmation.component';
import { saveAs } from 'file-saver';

@Component({
  selector: 'app-inspect-bill',
  templateUrl: './inspect-bill.component.html',
  styleUrls: ['./inspect-bill.component.scss']
})
export class InspectBillComponent implements OnInit {

  displayColumn: string[] = ['name', 'email', 'contactNumber', 'paymentMethod', 'total', 'view']
  dataSource:any;
  responseMessage:any;

  constructor(private billService: BillService,
    private ngxLoader: NgxUiLoaderService,
    private matDialog: MatDialog,
    private snackBarService: SnackbarService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.ngxLoader.start();
    this.tableData();
  }

  tableData(){
    this.billService.getBills().subscribe((response:any)=>{
      this.ngxLoader.stop();
      this.dataSource = new MatTableDataSource(response);
    },(error)=>{
      this.ngxLoader.stop();
      console.log('logging err', error)
      if(error.error?.message){
        this.responseMessage = error.error?.message;
      }
      else{
        this.responseMessage = GlobalConstant.genericError
      }
      this.snackBarService.openSnackBar(this.responseMessage, GlobalConstant.error)
    })
  }

  handleViewAction(values:any){
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = {
      data:values
    }
    dialogConfig.width = '100%';
    const dialogRef = this.matDialog.open(ViewBillProductsComponent, dialogConfig);
    this.router.events.subscribe((response:any)=>{
      dialogRef.close();
    })
  }

  handleDeleteAction(values:any){
    const matDialogConfig = new MatDialogConfig();
    matDialogConfig.data = {
      message: "Confirm delete this bill!",
      confirmmation: true
    }
    const dialogRef = this.matDialog.open(ConfirmationComponent, matDialogConfig);
    const sub = dialogRef.componentInstance.onEmitStatusChange.subscribe((reponse:any)=>{
      this.ngxLoader.start();
      this.deleteBill(values.id);
      dialogRef.close();
    })
  }

  deleteBill(id:any){
    this.billService.deleteBill(id).subscribe((reponse:any)=>{
      this.ngxLoader.stop();
      this.tableData();
      this.responseMessage = reponse.message;
      this.snackBarService.openSnackBar(this.responseMessage, "")
    },(error)=>{
      console.log('logging error:', error)
      if(error.error?.message) this.responseMessage = error.error?.message
      else this.responseMessage = GlobalConstant.genericError;
      this.snackBarService.openSnackBar(this.responseMessage, GlobalConstant.error);
    })

  }
  handleDownloadAction(values:any){
    this.ngxLoader.start();
    var data = {
      name: values.name,
      email: values.email,
      uuid: values.uuid,
      contactNumber: values.contactNumber,
      paymentMethod: values.paymentMethod,
      totalAmount: values.totalAmount,
      productDetails: values.productDetail
    }
    this.downloadFile(data.uuid, data);
  }
  downloadFile(fileName:string, data:any){
    this.billService.getPdf(data).subscribe((response:any)=>{
      saveAs(response, fileName+ ".pdf");
      this.ngxLoader.stop();
    })
  }

}
