import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Api } from '../../core/api';
import { Product } from '../../core/models';
import { errMsg } from '../../core/error-message';
import { Auth } from '../../core/auth';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-admin-products',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './admin-products.html',
  styleUrl: './admin-products.css',
})
export class AdminProducts implements OnInit {
  private api = inject(Api);
  private fb = inject(FormBuilder);
  auth = inject(Auth);

  products = signal<Product[]>([]);
  page = signal(0);
  totalPages = signal(0);
  editingId = signal<number | null>(null);
  showForm = signal(false);
  message = signal('');
  error = signal('');

  form = this.fb.nonNullable.group({
    name: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
    description: ['', Validators.maxLength(500)],
    price: [0, [Validators.required, Validators.min(0.01)]],
    imageUrl: [''],
    available: [true],
  });

  ngOnInit() {
    this.load(0);
  }

  load(page: number) {
    this.api.getAllProductsAdmin(page, 10).subscribe({
      next: (p) => {
        this.products.set(p.content);
        this.page.set(p.number);
        this.totalPages.set(p.totalPages);
      },
      error: (err) => this.error.set(errMsg(err)),
    });
  }

  openAdd() {
    this.editingId.set(null);
    this.form.reset({ name: '', description: '', price: 0, imageUrl: '', available: true });
    this.showForm.set(true);
  }

  openEdit(p: Product) {
    this.editingId.set(p.id);
    this.form.setValue({
      name: p.name,
      description: p.description ?? '',
      price: p.price,
      imageUrl: p.imageUrl ?? '',
      available: p.available,
    });
    this.showForm.set(true);
  }

  save() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const body = this.form.getRawValue();
    const id = this.editingId();
    const request = id === null ? this.api.createProduct(body) : this.api.updateProduct(id, body);

    request.subscribe({
      next: () => {
        this.message.set(id === null ? 'Product created.' : 'Product updated.');
        this.error.set('');
        this.showForm.set(false);
        this.load(id === null ? 0 : this.page());
      },
      error: (err) => {
        this.message.set('');
        this.error.set(errMsg(err));
      },
    });
  }

  toggleAvailability(p: Product) {
    if (!p.available) return; // re-enabling: use Edit and check the box instead
    if (!confirm(`Mark "${p.name}" as unavailable?`)) return;
    this.api.deactivateProduct(p.id).subscribe({
      next: () => {
        this.message.set('Product marked unavailable.');
        this.error.set('');
        this.load(this.page());
      },
      error: (err) => this.error.set(errMsg(err)),
    });
  }
}