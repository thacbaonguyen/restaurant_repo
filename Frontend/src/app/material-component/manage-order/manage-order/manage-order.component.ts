import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { error } from 'console';
import { saveAs } from 'file-saver';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { BillService } from 'src/app/services/bill/bill/bill.service';
import { CategoryService } from 'src/app/services/category/category.service';
import { ProductService } from 'src/app/services/product/product.service';
import { SnackbarService } from 'src/app/services/snackbar.service';
import { GlobalConstant } from 'src/app/shared/accordion/global-constanst';

@Component({
  selector: 'app-manage-order',
  templateUrl: './manage-order.component.html',
  styleUrls: ['./manage-order.component.scss']
})
export class ManageOrderComponent implements OnInit {
  displayColumn:string[] = ['name', 'category', 'price', 'quantity', 'total', 'edit'];
  dataSource:any = [];
  orderForm:any = FormGroup;
  categories:any = [];
  products:any = [];
  price:any;
  totalAmount:number = 0;
  responseMessage:any;

  constructor(private categoryService:CategoryService,
    private productService: ProductService,
    private snackBarService: SnackbarService,
    private ngxLoader: NgxUiLoaderService,
    private formBuilder: FormBuilder,
    private billService: BillService
  ) { }

  ngOnInit(): void {
    this.ngxLoader.start();
    this.getCategories();
    this.orderForm = this.formBuilder.group({
      name: [null, [Validators.required, Validators.pattern(GlobalConstant.nameRegex)]],
      email: [null, [Validators.required, Validators.pattern(GlobalConstant.emailRegex)]],
      contactNumber: [null, [Validators.required, Validators.pattern(GlobalConstant.contactNumberRegex)]],
      paymentMethod: [null, Validators.required],
      product: [null, Validators.required],
      category: [null, Validators.required],
      price: [null, Validators.required],
      quantity: [null, Validators.required],
      total: [0, Validators.required]
    })
  }

  getCategories(){
    this.categoryService.getCategoryFilter().subscribe((response:any)=>{
      this.categories = response;
      this.ngxLoader.stop();
    },(error)=>{
      console.log(error)
      this.ngxLoader.stop();
      if(error.error?.message){
        this.responseMessage = error.error?.message
      }
      else{
        this.responseMessage = GlobalConstant.genericError;
      }
      this.snackBarService.openSnackBar(this.responseMessage, GlobalConstant.error);
    })
  }

  getProductByCategory(value: any){
    this.productService.getProductByCategory(value.id).subscribe((reponse: any)=>{
      this.products = reponse;
      this.orderForm.controls['price'].setValue('');
      this.orderForm.controls['quantity'].setValue('');
      this.orderForm.controls['price'].setValue(0);
    },(error)=>{
      console.log(error)
      if(error.error?.message){
        this.responseMessage = error.error?.message
      }
      else{
        this.responseMessage = GlobalConstant.genericError;
      }
      this.snackBarService.openSnackBar(this.responseMessage, GlobalConstant.error);
    })
  }

  getProductById(value : any){
    this.productService.getProductById(value.id).subscribe((response: any)=>{
      this.price = response.price;
      this.orderForm.controls['price'].setValue(response.price);
      this.orderForm.controls['quantity'].setValue('1');
      this.orderForm.controls['total'].setValue(response.price * 1);
      
    },(error)=>{
      console.log(error)
      if(error.error?.message){
        this.responseMessage = error.error?.message
      }
      else{
        this.responseMessage = GlobalConstant.genericError;
      }
      this.snackBarService.openSnackBar(this.responseMessage, GlobalConstant.error);
    })
  }

  setQuantity(value: any){
    var temp = this.orderForm.controls['quantity'].value;
    if(temp > 0){
      this.orderForm.controls['total'].setValue(this.orderForm.controls['price'].value* this.orderForm.controls['quantity'].value);
    }
    else if(temp != ''){
      this.orderForm.controls['quantity'].setValue('1');
      this.orderForm.controls['total'].setValue(this.orderForm.controls['price'].value* this.orderForm.controls['quantity'].value);
    }
  }

  validateAdd(){
    if(this.orderForm.controls['total'].value === 0 || this.orderForm.controls['total'].value === null || this.orderForm.controls['quantity'].value <= 0){
      return true;
    }
    else return false;
  }

  validateSubmit(){
    if(this.totalAmount === 0 || this.orderForm.controls['name'].value === null || this.orderForm.controls['email'].value === null ||
       this.orderForm.controls['contactNumber'].value === null || this.orderForm.controls['paymentMethod'].value === null){
        return true;
       }
       else return false;
  }

  add(){
    var formData = this.orderForm.value;
    var productName = this.dataSource.find((e:{id:number}) => e.id=== formData.product.id);
    if(productName === undefined){
      this.totalAmount+= formData.total;
      this.dataSource.push({id:formData.product.id, name:formData.product.name, category:formData.category.name, quantity:formData.quantity,
        price:formData.price, total:formData.total
      })
      this.dataSource = [...this.dataSource];
      this.snackBarService.openSnackBar(GlobalConstant.productSuccess, "");
    }
    else{
      this.snackBarService.openSnackBar(GlobalConstant.productExist, GlobalConstant.error);
    }
  }

  handleDelete(value:any, element:any){
    this.totalAmount = this.totalAmount - element.total;
    this.dataSource.splice(value, 1);
    this.dataSource = [...this.dataSource]; 
  }

  submit(){
    var formData = this.orderForm.value;
    var data = {
      name: formData.name,
      email: formData.email,
      contactNumber: formData.contactNumber,
      paymentMethod: formData.paymentMethod,
      totalAmount: this.totalAmount.toString(),
      productDetails: JSON.stringify(this.dataSource)

    }
    console.log(data)
    this.ngxLoader.start();
    this.billService.generateReport(data).subscribe((response: any)=>{
      console.log(response.uuid)
      this.downloadFile(response?.uuid);
      this.dataSource = [];
      this.orderForm.reset();
      this.totalAmount = 0;
    },(error)=>{
      this.ngxLoader.stop();
      console.log('logging err', error)
      if(error.error?.message){
        this.responseMessage = error.error?.message
      }
      else{
        this.responseMessage = GlobalConstant.genericError;
      }
      this.snackBarService.openSnackBar(this.responseMessage, GlobalConstant.error);
    })
  }

  downloadFile(fileName:string){
     var data ={
      uuid: fileName
     }
     this.billService.getPdf(data).subscribe((response:any)=>{
      saveAs(response, fileName+'.pdf');
      this.ngxLoader.stop();
     })
  }
}
