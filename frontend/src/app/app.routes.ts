import { Routes } from '@angular/router';
import { authGuard } from './core/auth-guard';
import { AdminProducts } from './pages/admin-products/admin-products';

export const routes: Routes = [
  {
    path: 'admin-products',
    component: AdminProducts,
    canActivate: [authGuard],
    data: { roles: ['ADMIN'] },
  },
  { path: '', redirectTo: 'admin-products', pathMatch: 'full' },
  { path: '**', redirectTo: 'admin-products' },
];