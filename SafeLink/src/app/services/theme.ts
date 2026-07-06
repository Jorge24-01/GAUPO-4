import { DOCUMENT } from '@angular/common';
import { Inject, Injectable, signal } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class ThemeService {
  private readonly storageKey = 'safelink_theme';
  private readonly darkMode = signal(false);

  constructor(@Inject(DOCUMENT) private readonly document: Document) {
    const savedTheme = localStorage.getItem(this.storageKey);
    this.setDarkMode(savedTheme === 'dark', false);
  }

  toggleTheme(): void {
    this.setDarkMode(!this.darkMode());
  }

  isDarkMode(): boolean {
    return this.darkMode();
  }

  private setDarkMode(isDark: boolean, persist = true): void {
    this.darkMode.set(isDark);
    this.document.documentElement.classList.toggle('theme-dark', isDark);

    if (persist) {
      localStorage.setItem(this.storageKey, isDark ? 'dark' : 'light');
    }
  }
}
