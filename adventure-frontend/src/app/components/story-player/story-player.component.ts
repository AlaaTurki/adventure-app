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
    this.error = null;
    this.bookService.getBookById(bookId).subscribe({
      next: (data) => {
        this.book = data;
        const gameState = this.gameStateService.getCurrentGameState();
        if (gameState.currentBookId === bookId && gameState.currentSectionId) {
          this.loadSection(gameState.currentSectionId);
        } else {
          this.loadSection(this.book.sections[0]?.id ?? 1);
        }
        this.loading = false;
      },
      error: () => {
        this.error = 'Failed to load book';
        this.loading = false;
      }
    });
  }

  loadSection(sectionId: number): void {
    if (!this.book || !this.book.id) {
      return;
    }

    this.loading = true;
    this.bookService.getSection(this.book.id, sectionId).subscribe({
      next: (data) => {
        this.currentSection = data;
        this.loading = false;
      },
      error: () => {
        this.error = 'Failed to load section';
        this.loading = false;
      }
    });
  }

  selectChoice(optionIndex: number): void {
    this.loading = true;
    this.error = null;

    this.gameStateService.chooseOption(optionIndex).subscribe({
      next: (state) => {
        this.gameState = state;
        this.loadSection(state.currentSectionId);
        this.loading = false;
      },
      error: (err) => {
        this.error = err?.error?.message || 'Unable to continue this adventure.';
        this.loading = false;
      }
    });
  }

  saveProgress(): void {
    this.error = null;
    this.gameStateService.saveGame().subscribe({
      next: () => {
        this.error = null;
      },
      error: (err) => {
        this.error = err?.error?.message || 'Unable to save your progress right now.';
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/']);
  }

  isGameOver(): boolean {
    return this.gameState?.status === 'DEAD' || this.gameState?.health === 0;
  }

  isSectionEnd(): boolean {
    return this.currentSection?.type === 'END' || this.gameState?.status === 'WON';
  }

  getHealthBarColor(): string {
    if (!this.gameState) return 'success';
    const percentage = (this.gameState.health / 10) * 100;
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
