import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { BookService } from '../../services/book.service';
import { GameStateService } from '../../services/game-state.service';
import { Book, Section, GameState, Choice } from '../../models/book.model';

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
    this.gameStateService.gameState$.subscribe(state => {
      this.gameState = state;
      if (this.book && state.currentBookId === this.book.id && state.currentSectionId) {
        this.loadSection(state.currentSectionId);
      }
    });

    this.route.params.subscribe(params => {
      const bookId = +params['id'];
      this.loadBook(bookId);
    });
  }

  loadBook(bookId: number): void {
    this.loading = true;
    this.error = null;
    this.bookService.getBookById(bookId).subscribe({
      next: (data) => {
        this.book = data;
        const currentGame = this.gameStateService.getCurrentGameState();
        if (currentGame.currentBookId !== bookId || !currentGame.gameId) {
          this.bookService.startGame(bookId).subscribe({
            next: (game) => {
              this.gameStateService.setGameState(game, data.title);
              this.loadSection(game.currentSectionId);
              this.loading = false;
            },
            error: () => {
              this.error = 'Failed to start the adventure.';
              this.loading = false;
            }
          });
          return;
        }
        this.loadSection(currentGame.currentSectionId);
        this.loading = false;
      },
      error: () => {
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
      error: () => {
        this.error = 'Failed to load section';
        this.loading = false;
      }
    });
  }

  selectChoice(choice: Choice): void {
    if (!this.gameState?.gameId || !this.currentSection?.options) {
      return;
    }

    const optionIndex = this.currentSection.options.findIndex(option => option === choice);
    if (optionIndex < 0) {
      return;
    }

    this.loading = true;
    this.bookService.chooseOption(this.gameState.gameId, optionIndex).subscribe({
      next: (game) => {
        this.gameStateService.setGameState(game, this.book?.title);
        this.loadSection(game.currentSectionId);
        this.loading = false;
      },
      error: () => {
        this.error = 'Could not continue this adventure.';
        this.loading = false;
      }
    });
  }

  saveProgress(): void {
    this.gameStateService.saveProgress();
  }

  pauseGame(): void {
    this.gameStateService.pauseGame();
    this.router.navigate(['/']);
  }

  goBack(): void {
    this.gameStateService.pauseGame();
    this.router.navigate(['/']);
  }

  isGameOver(): boolean {
    return this.gameState?.health === 0 || this.gameState?.status === 'DEAD';
  }

  isSectionEnd(): boolean {
    return this.currentSection?.type === 'END';
  }

  getHealthBarColor(): string {
    if (!this.gameState) return 'success';
    const percentage = (this.gameState.health / Math.max(this.gameState.maxHealth, 10)) * 100;
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
