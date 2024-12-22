import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { BillService } from 'src/app/services/bill/bill/bill.service';

@Component({
  selector: 'app-view-bill-products',
  templateUrl: './view-bill-products.component.html',
  styleUrls: ['./view-bill-products.component.scss']
})
export class ViewBillProductsComponent implements OnInit {

  displayColumn: string[] = ['name', 'category', 'price', 'quantity', 'total'];
  dataSource:any;
  reponseMessage:any;
  data:any;

  constructor(@Inject(MAT_DIALOG_DATA) public matDialogData:any,
    public matDialogRef: MatDialogRef<ViewBillProductsComponent>,
  ) { }

  ngOnInit() {
    this.data = this.matDialogData.data;
    this.dataSource = JSON.parse(this.matDialogData.data.productDetail);
  }
}
