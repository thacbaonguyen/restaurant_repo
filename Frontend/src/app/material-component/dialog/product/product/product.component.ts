import { Component, EventEmitter, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import {
  MAT_DIALOG_DATA,
  MatDialog,
  MatDialogRef,
} from '@angular/material/dialog';
import { error } from 'console';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { CategoryService } from 'src/app/services/category/category.service';
import { ProductService } from 'src/app/services/product/product.service';
import { SnackbarService } from 'src/app/services/snackbar.service';
import { GlobalConstant } from 'src/app/shared/accordion/global-constanst';

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.scss'],
})
export class ProductComponent implements OnInit {
  onAddEvent = new EventEmitter();
  onUpdateEvent = new EventEmitter();
  productForm: any = FormGroup;
  dialogAction: any = 'Add';

  responseMessage: any;
  categories: any = [];
  constructor(
    @Inject(MAT_DIALOG_DATA) public matDialogData: any,
    private productService: ProductService,
    private categoryService: CategoryService,
    public dialogRef: MatDialogRef<ProductComponent>,
    private snackBarservice: SnackbarService,
    private ngxLoader: NgxUiLoaderService,
    private formBuilder: FormBuilder
  ) {}

  ngOnInit(): void {
    this.productForm = this.formBuilder.group({
      name: [null, [Validators.required, Validators.pattern(GlobalConstant.nameRegex)]],
      categoryId: [null, [Validators.required]],
      price: [null, Validators.required],
      description: [null, Validators.required]
    })

    if(this.matDialogData.action === 'Edit'){
      this.dialogAction = 'Edit';
      // this.action = 'Update';
      this.productForm.patchValue(this.matDialogData.data);
    }
    this.getCategory();
  }
  getCategory(){
    this.categoryService.getAll().subscribe((response: any)=>{
      this.categories = response;
    },(error)=>{
      this.responseMessage = GlobalConstant.genericError;
      this.snackBarservice.openSnackBar(this.responseMessage, GlobalConstant.error);
    })
  }

  handleSubmit(){
    if(this.dialogAction === 'Edit'){
      this.edit();
    }
    else{
      this.add();
    }
  }

  add(){
    var formData = this.productForm.value;
    var data = {
      name: formData.name,
      categoryId: formData.categoryId,
      price: formData.price,
      description: formData.description
    }
    this.productService.addProduct(data).subscribe((response:any)=>{
      this.dialogRef.close();
      this.onAddEvent.emit();
      this.responseMessage = response.message;
      this.snackBarservice.openSnackBar(this.responseMessage, "")
    },(error)=>{
      console.log(error)
      if(error.error?.message){
        this.responseMessage = error.error.message;
      }
      else{
        this.responseMessage = GlobalConstant.genericError;
      }
      this.snackBarservice.openSnackBar(this.responseMessage, GlobalConstant.error);
    })
  }

  edit(){
    var formData = this.productForm.value;
    var data = {
      id: this.matDialogData.data.id,
      name: formData.name,
      categoryId: formData.categoryId,
      price: formData.price,
      description: formData.description
    }
    this.productService.updateProduct(data).subscribe((response:any)=>{
      this.dialogRef.close();
      this.onUpdateEvent.emit();
      this.responseMessage = response.message;
      this.snackBarservice.openSnackBar(this.responseMessage, "")
    },(error)=>{
      console.log(error)
      if(error.error?.message){
        this.responseMessage = error.error.message;
      }
      else{
        this.responseMessage = GlobalConstant.genericError;
      }
      this.snackBarservice.openSnackBar(this.responseMessage, GlobalConstant.error);
    })
  }
}
