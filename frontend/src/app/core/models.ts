export interface LoginResponse {
  token: string;
  username: string;
  role: 'ADMIN' | 'CUSTOMER';
}

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
}

export interface Page<T> {
  content: T[];
  number: number;
  size: number;
  totalElements: number;
  totalPages: number;
}