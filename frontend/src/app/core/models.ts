export interface Page<T> {
  content: T[];
  number: number;
  size: number;
  totalElements: number;
  totalPages: number;
}

export interface Product {
  id: number;
  name: string;
  description: string;
  price: number;
  imageUrl: string;
  available: boolean;
}

export interface ProductRequest {
  name: string;
  description: string;
  price: number;
  imageUrl: string;
  available: boolean;
}

export interface LoginResponse {
  token: string;
  username: string;
  role: 'ADMIN' | 'CUSTOMER';
}

export interface OrderItemRequest { productId: number; quantity: number; }
export interface PlaceOrderRequest { items: OrderItemRequest[]; }
export interface OrderItemResponse { productName: string; quantity: number; priceAtOrder: number; }
export interface OrderResponse {
  id: number;
  status: string;
  totalAmount: number;
  placedAt: string;
  items: OrderItemResponse[];
}

export interface CartLine { product: Product; quantity: number; }
export const STATUS_FLOW = ['PLACED', 'CONFIRMED', 'PREPARING', 'OUT_FOR_DELIVERY', 'DELIVERED'] as const;