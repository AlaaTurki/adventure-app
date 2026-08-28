import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { GameState } from '../models/book.model';

@Injectable({
  providedIn: 'root'
})
export class GameStateService {
  private gameState = new BehaviorSubject<GameState>({
    currentBookId: 0,
    currentSectionId: 1,
    health: 100,
    maxHealth: 100,
    choices: []
  });

  public gameState$ = this.gameState.asObservable();

  constructor() { }

  startGame(bookId: number): void {
    const state = this.gameState.value;
    state.currentBookId = bookId;
    state.currentSectionId = 1;
    state.health = 100;
    state.maxHealth = 100;
    state.choices = [];
    this.gameState.next(state);
  }

  goToSection(sectionId: number): void {
    const state = this.gameState.value;
    state.currentSectionId = sectionId;
    state.choices.push(sectionId);
    this.gameState.next(state);
  }

  updateHealth(amount: number): void {
    const state = this.gameState.value;
    state.health = Math.max(0, Math.min(state.maxHealth, state.health + amount));
    this.gameState.next(state);
  }

  getCurrentGameState(): GameState {
    return this.gameState.value;
  }
}
