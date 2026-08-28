import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { BookService } from '../../services/book.service';
import { GameStateService } from '../../services/game-state.service';
import { Book, Section, GameState } from '../../models/book.model';

@Component({
  selector: 'app-story-player',
  templateUrl: './story-player.component.html',
  styleUrls: ['./story-player.component.css']
})
export class StoryPlayerComponent implements OnInit {
  book: Book | null = null;
  currentSection: Section | null = null;
  gameState: GameState | null = null;
  loading = false;
  error: string | null = null;

  constructor(
    private bookService: BookService,
    private gameStateService: GameStateService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      const bookId = +params['id'];
      this.loadBook(bookId);
    });

    this.gameStateService.gameState$.subscribe(state => {
      this.gameState = state;
      if (this.book && state.currentBookId === this.book.id) {
        this.loadSection(state.currentSectionId);
      }
    });
  }

  loadBook(bookId: number): void {
    this.loading = true;
    this.bookService.getBookById(bookId).subscribe({
      next: (data) => {
        this.book = data;
        const gameState = this.gameStateService.getCurrentGameState();
        this.loadSection(gameState.currentSectionId);
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load book';
        this.loading = false;
      }
    });
  }

  loadSection(sectionId: number): void {
    if (!this.book) return;

    this.loading = true;
    this.bookService.getSection(this.book.id!, sectionId).subscribe({
      next: (data) => {
        this.currentSection = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load section';
        this.loading = false;
      }
    });
  }

  selectChoice(gotoId: number, healthChange?: string): void {
    if (healthChange) {
      const change = parseInt(healthChange, 10);
      this.gameStateService.updateHealth(-change);
    }

    this.gameStateService.goToSection(gotoId);
  }

  goBack(): void {
    this.router.navigate(['/']);
  }

  isGameOver(): boolean {
    return this.gameState?.health === 0;
  }

  isSectionEnd(): boolean {
    return this.currentSection?.type === 'END';
  }

  getHealthBarColor(): string {
    if (!this.gameState) return 'success';
    const percentage = (this.gameState.health / this.gameState.maxHealth) * 100;
    if (percentage > 60) return 'success';
    if (percentage > 30) return 'warning';
    return 'danger';
  }

  getParagraphs(text: string): string[] {
    if (!text) {
      return [''];
    }

    return text
      .split(/(?<=[.!?])\s+/)
      .map((paragraph) => paragraph.trim())
      .filter((paragraph) => paragraph.length > 0);
  }
}
