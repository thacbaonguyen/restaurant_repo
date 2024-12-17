import { Component, EventEmitter, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { CategoryService } from 'src/app/services/category/category.service';
import { SnackbarService } from 'src/app/services/snackbar.service';
import { GlobalConstant } from 'src/app/shared/accordion/global-constanst';

@Component({
  selector: 'app-category',
  templateUrl: './category.component.html',
  styleUrls: ['./category.component.scss'],
})
export class CategoryComponent implements OnInit {
  onAddCategory = new EventEmitter();
  onUpdateCategory = new EventEmitter();
  categoryForm: any = FormGroup;
  dialogAction: any = 'Add';
  action: any = 'add';
  responseMessage: any;

  constructor(
    @Inject(MAT_DIALOG_DATA) public matDialogData: any,
    private categoryService: CategoryService,
    private ngxLoader: NgxUiLoaderService,
    private snackBarService: SnackbarService,
    public matDialogRef: MatDialogRef<CategoryComponent>,
    private formBuilder: FormBuilder
  ) {}

  ngOnInit(): void {
    this.categoryForm = this.formBuilder.group({
      name: [null, Validators.required],
    });
    if (this.matDialogData.action === 'Edit') {
      this.dialogAction = 'Edit';
      this.action = 'Update';
      this.categoryForm.patchValue(this.matDialogData.data);
    }
  }

  handleSubmit() {
    console.log('logging', this.matDialogData.action)
    if (this.dialogAction === 'Edit') {
      this.edit();
    } else {
      this.add();
    }
  }
  add() {
    var dataForm = this.categoryForm.value;
    var data = {
      name: dataForm.name,
    };
    this.categoryService.add(data).subscribe(
      (response: any) => {
        this.matDialogRef.close();
        this.onAddCategory.emit();
        this.responseMessage = response?.message;
        this.snackBarService.openSnackBar(this.responseMessage, '');
      },
      (error) => {
        this.matDialogRef.close();
        if (error.error?.message) {
          this.responseMessage = error.error?.message;
        } else {
          this.responseMessage = GlobalConstant.genericError;
        }
        this.snackBarService.openSnackBar(
          this.responseMessage,
          GlobalConstant.error
        );
      }
    );
  }

  edit() {
    var dataForm = this.categoryForm.value;
    var data = {
      id: this.matDialogData.data.id,
      name: dataForm.name,
    };
    this.categoryService.update(data).subscribe(
      (response: any) => {
        this.matDialogRef.close();
        this.onUpdateCategory.emit();
        this.responseMessage = response?.message;
        this.snackBarService.openSnackBar(this.responseMessage, '');
      },
      (error) => {
        this.matDialogRef.close();
        if (error.error?.message) {
          this.responseMessage = error.error?.message;
        } else {
          this.responseMessage = GlobalConstant.genericError;
        }
        this.snackBarService.openSnackBar(
          this.responseMessage,
          GlobalConstant.error
        );
      }
    );
  }
}
