import { Component, OnInit, inject } from '@angular/core';
import { Router } from '@angular/router';
import { Auth } from '../../core/auth';

@Component({
  selector: 'app-home',
  imports: [],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home implements OnInit {
  private auth = inject(Auth);
  private router = inject(Router);

  ngOnInit() {
    if (this.auth.getRole() === 'ADMIN') {
      this.router.navigate(['/admin-products']);
    } else {
      this.router.navigate(['/shop']);
    }
  }
}