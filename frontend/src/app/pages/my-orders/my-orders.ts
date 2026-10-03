import { Component, OnInit, inject, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { Api } from '../../core/api';
import { OrderResponse } from '../../core/models';
import { errMsg } from '../../core/error-message';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-my-orders',
  imports: [DatePipe, RouterLink],
  templateUrl: './my-orders.html',
  styleUrl: './my-orders.css',
})
export class MyOrders implements OnInit {
  private api = inject(Api);
  orders = signal<OrderResponse[]>([]);
  error = signal('');

  ngOnInit() {
    this.api.getMyOrders().subscribe({
      next: (o) => this.orders.set(o),
      error: (err) => this.error.set(errMsg(err)),
    });
  }
}