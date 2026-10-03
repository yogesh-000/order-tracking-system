import { Component, OnInit, OnDestroy, inject, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { DatePipe } from '@angular/common';
import { Api } from '../../core/api';
import { Ws } from '../../core/ws';
import { OrderResponse, STATUS_FLOW } from '../../core/models';
import { errMsg } from '../../core/error-message';

@Component({
  selector: 'app-order-detail',
  imports: [DatePipe, RouterLink],
  templateUrl: './order-detail.html',
  styleUrl: './order-detail.css',
})
export class OrderDetail implements OnInit, OnDestroy {
  private api = inject(Api);
  private ws = inject(Ws);
  private route = inject(ActivatedRoute);

  order = signal<OrderResponse | null>(null);
  error = signal('');
  readonly steps = STATUS_FLOW;

  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.api.getOrder(id).subscribe({
      next: (o) => {
        this.order.set(o);
        if (o.status !== 'DELIVERED' && o.status !== 'CANCELLED') {
          this.ws.connectAndSubscribe(id, (updated) => this.order.set(updated));
        }
      },
      error: (err) => this.error.set(errMsg(err)),
    });
  }

  ngOnDestroy() {
    this.ws.disconnect();
  }

  stepIndex(status: string): number {
    return this.steps.indexOf(status as any);
  }

  cancelling = signal(false);

cancelOrder() {
  const o = this.order();
  if (!o || !confirm(`Cancel order #${o.id}?`)) return;
  this.cancelling.set(true);
  this.api.cancelMyOrder(o.id).subscribe({
    next: (updated) => { this.order.set(updated); this.cancelling.set(false); },
    error: (err) => { this.error.set(errMsg(err)); this.cancelling.set(false); },
  });
}

canCancel(status: string): boolean {
  return status === 'PLACED' || status === 'CONFIRMED';
}
}