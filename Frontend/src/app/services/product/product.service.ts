import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  url = environment.apiUrl;
  constructor(private httpClient: HttpClient) { }

  getProducts(){
    return this.httpClient.get(this.url + "/product/getAll");
  }

  addProduct(data: any){
    return this.httpClient.post(this.url + "/product/add", data, {headers: new HttpHeaders().set('Content-Type', 'application/json')})
  }

  updateProduct(data: any){
    return this.httpClient.post(this.url + "/product/update", data, {headers: new HttpHeaders().set('Content-Type', 'application/json')})
  }

  deleteProduct(id: any){
    return this.httpClient.delete(this.url + "/product/delete/" + id, {headers: new HttpHeaders().set('Content-Type', 'application/json')})
  }
}
