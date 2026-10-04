import { Injectable, inject, signal, computed } from '@angular/core';
import { Api } from './api';
import { CartLine, Product } from './models';

@Injectable({ providedIn: 'root' })
export class Cart {
  private api = inject(Api);

  lines = signal<CartLine[]>([]);
  total = computed(() => this.lines().reduce((s, l) => s + l.price * l.quantity, 0));

  load() {
    this.api.getCart().subscribe({ next: (c) => this.lines.set(c.items) });
  }

add(productId: number) {
  this.api.addToCart(productId).subscribe({ next: (c) => this.lines.set(c.items) });
}

  remove(productId: number) {
    this.api.removeFromCart(productId).subscribe({ next: (c) => this.lines.set(c.items) });
  }

  decrease(productId: number) {
  this.api.decreaseCartItem(productId).subscribe({ next: (c) => this.lines.set(c.items) });
}

  clearLocal() {
    this.lines.set([]);
  }
}