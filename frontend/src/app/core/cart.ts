import { Injectable, signal, computed } from '@angular/core';
import { CartLine, Product } from './models';

@Injectable({ providedIn: 'root' })
export class Cart {
  lines = signal<CartLine[]>([]);

  total = computed(() =>
    this.lines().reduce((sum, l) => sum + l.product.price * l.quantity, 0)
  );

  add(product: Product) {
    const existing = this.lines().find((l) => l.product.id === product.id);
    if (existing) {
      this.lines.update((lines) =>
        lines.map((l) => (l.product.id === product.id ? { ...l, quantity: l.quantity + 1 } : l))
      );
    } else {
      this.lines.update((lines) => [...lines, { product, quantity: 1 }]);
    }
  }

  remove(productId: number) {
    this.lines.update((lines) => lines.filter((l) => l.product.id !== productId));
  }

  clear() {
    this.lines.set([]);
  }
}