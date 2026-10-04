import { Component, OnInit, inject, signal } from '@angular/core';
import { Api } from '../../core/api';
import { Cart } from '../../core/cart';
import { Auth } from '../../core/auth';
import { Product } from '../../core/models';
import { errMsg } from '../../core/error-message';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-shop',
  imports: [ RouterLink ],
  templateUrl: './shop.html',
  styleUrl: './shop.css',
})
export class Shop implements OnInit {
  private api = inject(Api);
  cart = inject(Cart);
  auth = inject(Auth);

  products = signal<Product[]>([]);
  message = signal('');
  error = signal('');
  placing = signal(false);

ngOnInit() {
  this.api.getAvailableProducts(0, 50).subscribe({
    next: (p) => this.products.set(p.content),
    error: (err) => this.error.set(errMsg(err)),
  });
  this.cart.load();
}

placeOrder() {
  if (this.cart.lines().length === 0) return;
  this.placing.set(true);
  this.api.placeOrder().subscribe({
    next: () => {
      this.placing.set(false);
      this.message.set('Order placed!');
      this.error.set('');
      this.cart.clearLocal();
    },
    error: (err) => {
      this.placing.set(false);
      this.message.set('');
      this.error.set(errMsg(err));
    },
  });
}
}