import { Injectable, inject } from '@angular/core';
import { Client, IMessage } from '@stomp/stompjs';
import { Auth } from './auth';
import { OrderResponse } from './models';

@Injectable({ providedIn: 'root' })
export class Ws {
  private auth = inject(Auth);
  private client: Client | null = null;

  connectAndSubscribe(orderId: number, onUpdate: (order: OrderResponse) => void) {
    this.client = new Client({
      brokerURL: 'ws://localhost:8080/ws',
      connectHeaders: { Authorization: `Bearer ${this.auth.getToken()}` },
      reconnectDelay: 5000,
    });

    this.client.onConnect = () => {
      this.client!.subscribe(`/topic/orders/${orderId}`, (message: IMessage) => {
        onUpdate(JSON.parse(message.body));
      });
    };

    this.client.activate();
  }

  disconnect() {
    this.client?.deactivate();
    this.client = null;
  }
}