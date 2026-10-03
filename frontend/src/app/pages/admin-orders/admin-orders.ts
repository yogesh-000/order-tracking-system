import { Component, OnInit, inject, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { Api } from '../../core/api';
import { Auth } from '../../core/auth';
import { OrderResponse, STATUS_FLOW } from '../../core/models';
import { errMsg } from '../../core/error-message';

@Component({
  selector: 'app-admin-orders',
  imports: [DatePipe],
  templateUrl: './admin-orders.html',
  styleUrl: './admin-orders.css',
})
export class AdminOrders implements OnInit {
  private api = inject(Api);
  auth = inject(Auth);

  orders = signal<OrderResponse[]>([]);
  filter = signal('');
  error = signal('');
  readonly statuses = ['', ...STATUS_FLOW, 'CANCELLED'];

  ngOnInit() {
    this.load();
  }

  load() {
    this.api.getAllOrders(this.filter()).subscribe({
      next: (o) => this.orders.set(o),
      error: (err) => this.error.set(errMsg(err)),
    });
  }

  setFilter(s: string) {
    this.filter.set(s);
    this.load();
  }

  nextStatus(status: string): string | null {
    const i = STATUS_FLOW.indexOf(status as any);
    return i >= 0 && i < STATUS_FLOW.length - 1 ? STATUS_FLOW[i + 1] : null;
  }

  advance(order: OrderResponse) {
    const next = this.nextStatus(order.status);
    if (!next) return;
    this.api.updateOrderStatus(order.id, next).subscribe({
      next: () => { this.error.set(''); this.load(); },
      error: (err) => this.error.set(errMsg(err)),
    });
  }

  cancel(order: OrderResponse) {
    if (!confirm(`Cancel order #${order.id}?`)) return;
    this.api.updateOrderStatus(order.id, 'CANCELLED').subscribe({
      next: () => { this.error.set(''); this.load(); },
      error: (err) => this.error.set(errMsg(err)),
    });
  }
}