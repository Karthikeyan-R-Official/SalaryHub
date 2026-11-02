import { Component, inject } from '@angular/core';
import {
  Router,
  RouterLink,
  RouterLinkActive,
  RouterOutlet
} from '@angular/router';
import { AuthService } from './services/auth';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  readonly router = inject(Router);
  readonly authService = inject(AuthService);

  logout(): void {
    this.authService.logout();
    this.router.navigateByUrl('/login');
  }

  get username(): string | null {
    return this.authService.getUsername();
  }
}
