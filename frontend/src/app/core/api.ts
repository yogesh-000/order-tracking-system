import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { OrderResponse, Page, Product, ProductRequest, CartResponse } from './models';

@Injectable({ providedIn: 'root' })
export class Api {
  private http = inject(HttpClient);
  private base = 'http://localhost:8080/api';

  getAllProductsAdmin(page: number, size: number) {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<Page<Product>>(`${this.base}/products/admin`, { params });
  }

  getOrder(id: number) {
  return this.http.get<OrderResponse>(`${this.base}/orders/${id}`);
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

  getAvailableProducts(page: number, size: number) {
  const params = new HttpParams().set('page', page).set('size', size);
  return this.http.get<Page<Product>>(`${this.base}/products`, { params });
}

getMyOrders() {
  return this.http.get<OrderResponse[]>(`${this.base}/orders/my`);
}

getAllOrders(status: string) {
  const params = status ? new HttpParams().set('status', status) : undefined;
  return this.http.get<OrderResponse[]>(`${this.base}/orders`, { params });
}

updateOrderStatus(id: number, status: string) {
  const params = new HttpParams().set('status', status);
  return this.http.put<OrderResponse>(`${this.base}/orders/${id}/status`, {}, { params });
}

cancelMyOrder(id: number) {
  return this.http.put<OrderResponse>(`${this.base}/orders/${id}/cancel`, {});
}

getCart() {
  return this.http.get<CartResponse>(`${this.base}/cart`);
}
addToCart(productId: number) {
  return this.http.post<CartResponse>(`${this.base}/cart/items/${productId}`, {});
}
removeFromCart(productId: number) {
  return this.http.delete<CartResponse>(`${this.base}/cart/items/${productId}`);
}

decreaseCartItem(productId: number) {
  return this.http.put<CartResponse>(`${this.base}/cart/items/${productId}/decrease`, {});
}

placeOrder() {
  return this.http.post<OrderResponse>(`${this.base}/orders`, {});
}
}