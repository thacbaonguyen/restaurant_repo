import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { CategoryService } from 'src/app/services/category/category.service';
import { SnackbarService } from 'src/app/services/snackbar.service';
import { GlobalConstant } from 'src/app/shared/accordion/global-constanst';
import { CategoryComponent } from '../../dialog/category/category/category.component';

@Component({
  selector: 'app-manage-category',
  templateUrl: './manage-category.component.html',
  styleUrls: ['./manage-category.component.scss']
})
export class ManageCategoryComponent implements OnInit {

  displayColumn: string[] = ['name', 'edit'];
  dataSource:any;
  responseMessage:any;

  constructor(private categoryService: CategoryService,
    private router:Router,
    private ngxLoader: NgxUiLoaderService,
    private matDialog: MatDialog,
    private snackBarService: SnackbarService
  ) { }

  ngOnInit(): void {
    this.ngxLoader.start();
    this.tableData();
  }
   
  tableData(){
    this.categoryService.getAll().subscribe((response: any)=>{
      this.ngxLoader.stop();
      this.dataSource = new MatTableDataSource(response);
      console.log(response)

    },(error)=>{
      console.log(error)
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

  applyFilter(event: Event){
    const filterValue =(event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim();
  }
  handleEditAction(values:any){
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = {
      action: 'Edit',
      data:values
    };
    dialogConfig.width = '800px';
    const dialogRef = this.matDialog.open(CategoryComponent, dialogConfig);
    const sub = dialogRef.componentInstance.onUpdateCategory.subscribe((response: any)=>{
      dialogRef.close();
      this.tableData();
    })
  }

  handleAddAction(){
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = {
      action: 'Add'
    };
    dialogConfig.width = '800px';
    const dialogRef = this.matDialog.open(CategoryComponent, dialogConfig);
    const sub = dialogRef.componentInstance.onAddCategory.subscribe((response: any)=>{
      dialogRef.close();
      this.tableData();
    })
  }

}
