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

  // consequence feedback
  consequenceText: string | null = null;
  consequenceDelta: number | null = null;
  showConsequence = false;
  disablingChoices = false;

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

  // capture consequence locally for immediate feedback
  const consequence = choice.consequence;
  this.consequenceText = consequence?.text ?? null;
  this.consequenceDelta = consequence && consequence.value ? Number(consequence.value) : null;
  this.showConsequence = false;
  this.disablingChoices = true;
  this.loading = true;

  this.bookService.chooseOption(this.gameState.gameId, optionIndex).subscribe({
    next: (game) => {
      // update shared state from server
      this.gameStateService.setGameState(game, this.book?.title);

      // show consequence feedback briefly
      if (this.consequenceText || this.consequenceDelta !== null) {
        this.showConsequence = true;
        // reflect server health if provided
        this.consequenceDelta = this.consequenceDelta ?? 0;
        // small timeout to let user see feedback before loading next section
        setTimeout(() => {
          this.showConsequence = false;
          this.disablingChoices = false;
          // navigate to the returned section
          if (game?.currentSectionId) {
            this.loadSection(game.currentSectionId);
          }
          this.loading = false;
        }, 700);
      } else {
        this.disablingChoices = false;
        if (game?.currentSectionId) {
          this.loadSection(game.currentSectionId);
        }
        this.loading = false;
      }
    },
    error: () => {
      this.error = 'Could not continue this adventure.';
      this.loading = false;
      this.disablingChoices = false;
    }
  });
  }

  saveProgress(): void {
    if (!this.gameState?.gameId) {
      // fallback to local save
      this.gameStateService.saveProgress();
      return;
    }

    this.loading = true;
    this.bookService.saveGame(this.gameState.gameId).subscribe({
      next: () => {
        this.gameStateService.saveProgress(); // keep local copy too
        this.loading = false;
      },
      error: () => {
        // fallback: persist locally
        this.gameStateService.saveProgress();
        this.error = 'Failed to save to server; saved locally instead.';
        this.loading = false;
      }
    });
  }

  pauseGame(): void {
    // try server save first if possible, then pause locally and navigate
    if (this.gameState?.gameId) {
      this.loading = true;
      this.bookService.saveGame(this.gameState.gameId).subscribe({
        next: () => {
          this.gameStateService.pauseGame();
          this.loading = false;
          this.router.navigate(['/']);
        },
        error: () => {
          // fallback to local pause
          this.gameStateService.pauseGame();
          this.loading = false;
          this.router.navigate(['/']);
        }
      });
    } else {
      this.gameStateService.pauseGame();
      this.router.navigate(['/']);
    }
  }

  goBack(): void {
    this.gameStateService.pauseGame();
    this.router.navigate(['/']);
  }

  playAgain(): void {
    if (!this.book) return;
    this.loading = true;
    this.bookService.startGame(this.book.id!).subscribe({
      next: (game) => {
        this.gameStateService.setGameState(game, this.book?.title);
        this.loadSection(game.currentSectionId);
        this.loading = false;
      },
      error: () => {
        this.error = 'Failed to start the adventure.';
        this.loading = false;
      }
    });
  }

  isGameOver(): boolean {
    return this.gameState?.health === 0 || this.gameState?.status === 'DEAD' || this.gameState?.status === 'WON';
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
