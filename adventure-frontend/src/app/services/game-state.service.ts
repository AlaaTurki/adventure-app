import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { GameState } from '../models/book.model';

@Injectable({
  providedIn: 'root'
})
export class GameStateService {
  private readonly storageKey = 'adventure-game-state';

  private readonly initialState: GameState = {
    currentBookId: 0,
    currentSectionId: 1,
    health: 10,
    choices: [],
    status: 'PLAYING',
    isPaused: false
  };

  private gameState = new BehaviorSubject<GameState>(this.loadState());

  public gameState$ = this.gameState.asObservable();

  constructor() { }

  // remove local start/goToSection/updateHealth; server is authoritative

  setGameState(game: any, bookTitle?: string): void {
    const nextState: GameState = {
      gameId: game?.id,
      currentBookId: game?.bookId ?? this.gameState.value.currentBookId,
      currentSectionId: game?.currentSectionId ?? this.gameState.value.currentSectionId,
      health: game?.health ?? 10,
      choices: this.gameState.value.choices,
      bookTitle: bookTitle ?? this.gameState.value.bookTitle,
      status: game?.status ?? this.gameState.value.status,
      isPaused: false
    };
    this.gameState.next(nextState);
    this.persist();
  }

  pauseGame(): void {
    const state = this.gameState.value;
    state.isPaused = true;
    this.gameState.next({ ...state });
    this.persist();
  }

  saveProgress(): void {
    this.persist();
  }

  getCurrentGameState(): GameState {
    return this.gameState.value;
  }

  private persist(): void {
    localStorage.setItem(this.storageKey, JSON.stringify(this.gameState.value));
  }

  private loadState(): GameState {
    try {
      const saved = localStorage.getItem(this.storageKey);
      if (!saved) {
        return { ...this.initialState };
      }
      const parsed = JSON.parse(saved) as GameState;
      return {
        ...this.initialState,
        ...parsed,
        choices: parsed.choices ?? []
      };
    } catch {
      return { ...this.initialState };
    }
  }
}
