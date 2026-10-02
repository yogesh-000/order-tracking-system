import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Page, Product, ProductRequest } from './models';

@Injectable({ providedIn: 'root' })
export class Api {
  private http = inject(HttpClient);
  private base = 'http://localhost:8080/api';

  getAllProductsAdmin(page: number, size: number) {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<Page<Product>>(`${this.base}/products/admin`, { params });
  }

  createProduct(body: ProductRequest) {
    return this.http.post<Product>(`${this.base}/products`, body);
  }

  updateProduct(id: number, body: ProductRequest) {
    return this.http.put<Product>(`${this.base}/products/${id}`, body);
  }

  deactivateProduct(id: number) {
    return this.http.delete<void>(`${this.base}/products/${id}`);
  }
}