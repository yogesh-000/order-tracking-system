import { Routes } from '@angular/router';
import { authGuard } from './core/auth-guard';
import { AdminProducts } from './pages/admin-products/admin-products';
import { Login } from './pages/login/login';
import { Home } from './pages/home/home';
import { Shop } from './pages/shop/shop';
import { MyOrders } from './pages/my-orders/my-orders';
import { AdminOrders } from './pages/admin-orders/admin-orders';
import { OrderDetail } from './pages/order-detail/order-detail';

export const routes: Routes = [
  { path: 'login', component: Login },
  { path: 'home', component: Home, canActivate: [authGuard] },
  {
    path: 'admin-products',
    component: AdminProducts,
    canActivate: [authGuard],
    data: { roles: ['ADMIN'] },
  },
  { path: 'shop', component: Shop, canActivate: [authGuard] },
  { path: 'my-orders', component: MyOrders, canActivate: [authGuard] },
  { path: 'admin-orders', component: AdminOrders, canActivate: [authGuard], data: { roles: ['ADMIN'] } },
  { path: 'orders/:id', component: OrderDetail, canActivate: [authGuard] },
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: '**', redirectTo: 'home' },
];