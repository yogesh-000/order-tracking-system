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