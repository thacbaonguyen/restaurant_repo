import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { ProductService } from 'src/app/services/product/product.service';
import { SnackbarService } from 'src/app/services/snackbar.service';
import { GlobalConstant } from 'src/app/shared/accordion/global-constanst';
import { ProductComponent } from '../../dialog/product/product/product.component';

@Component({
  selector: 'app-manage-product',
  templateUrl: './manage-product.component.html',
  styleUrls: ['./manage-product.component.scss']
})
export class ManageProductComponent implements OnInit {

  dataSource:any;
  displayColumn: string[] = ['name', 'categoryName', 'description', 'price', 'edit'];
  length: any;
  responseMessage:any;

  constructor(private productService: ProductService,
    private ngxLoader: NgxUiLoaderService,
    public matDialog: MatDialog,
    private snackBarService: SnackbarService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.ngxLoader.start();
    this.tableData();
  }

  tableData(){
    this.productService.getProducts().subscribe((response: any) =>{
      this.ngxLoader.stop();
      this.dataSource = new MatTableDataSource(response);
      console.log(response)
    },(error)=>{
      this.ngxLoader.stop();
      console.log(error);
      if(error.error?.message){
        this.responseMessage = error.error?.message;
      }
      else{
        this.responseMessage = GlobalConstant.genericError;
      }
      this.snackBarService.openSnackBar(this.responseMessage, GlobalConstant.error);
    })
  }

  applyFilter(event: Event){
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase(); 
  }
  handleAddAction(){
    const dialogConfig = new MatDialogConfig();
    dialogConfig.width = '800px';
    dialogConfig.data = {
      action: 'Add'
    }
    const dialogRef = this.matDialog.open(ProductComponent, dialogConfig);
    const sub = dialogRef.componentInstance.onAddEvent.subscribe((response:any)=>{
      dialogRef.close();
      this.tableData();
    })
  }

  handleEditAction(value:any){
    const dialogConfig = new MatDialogConfig();
    dialogConfig.width = '800px';
    dialogConfig.data = {
      action: 'Edit',
      data: value
    }
    const dialogRef = this.matDialog.open(ProductComponent, dialogConfig);
    const sub = dialogRef.componentInstance.onUpdateEvent.subscribe((response: any)=>{
      dialogRef.close();
      this.tableData();
    })
  }

  handleDeleteAction(value: any){
    console.log('logging id', value.id)
    this.productService.deleteProduct(value.id).subscribe((response:any)=>{
      this.ngxLoader.stop();
      this.responseMessage = response?.message;
      this.snackBarService.openSnackBar(this.responseMessage, "")
      this.tableData();
    },(error)=>
      {
        this.ngxLoader.stop();
        console.log(error);
        if(error.error?.message){
          this.responseMessage = error.error?.message;
        }
        else{
          this.responseMessage = GlobalConstant.genericError;
        }
        this.snackBarService.openSnackBar(this.responseMessage, GlobalConstant.error);
      }
    )
  }

  onChange(status:any, id:any){
    var data = {
      status: 'true',
      id: id
    }
    console.log('logging status', typeof data.status, 'logging id', id)
    this.productService.updateStatus(data).subscribe((response:any)=>{
      this.ngxLoader.stop();
      this.responseMessage = response?.message;
      this.snackBarService.openSnackBar(this.responseMessage, "")
    },(error)=>{
      this.ngxLoader.stop();
        console.log('logging error',error);
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
