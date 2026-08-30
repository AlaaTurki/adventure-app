import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { GameState } from '../models/book.model';

interface GameResponse {
  id: string;
  bookId: number;
  bookTitle: string;
  currentSectionId: number;
  health: number;
  status: string;
}

@Injectable({
  providedIn: 'root'
})
export class GameStateService {
  private gameState = new BehaviorSubject<GameState>({
    currentBookId: 0,
    currentSectionId: 1,
    health: 10,
    choices: [],
    status: 'PLAYING'
  });

  public gameState$ = this.gameState.asObservable();

  constructor(private http: HttpClient) { }

  startGame(bookId: number): Observable<GameState> {
    return this.http.post<GameResponse>(`http://localhost:8080/games/start?bookId=${bookId}`, {}).pipe(
      map((game) => {
        const state: GameState = {
          gameId: game.id,
          currentBookId: game.bookId,
          currentSectionId: game.currentSectionId,
          health: game.health,
          choices: [],
          status: game.status,
          bookTitle: game.bookTitle
        };
        this.gameState.next(state);
        return state;
      })
    );
  }

  chooseOption(optionIndex: number): Observable<GameState> {
    const current = this.gameState.value;
    if (!current.gameId) {
      throw new Error('No active game to choose from.');
    }

    return this.http.post<GameResponse>(`http://localhost:8080/games/${current.gameId}/choices`, { optionIndex }).pipe(
      map((game) => {
        const state: GameState = {
          gameId: game.id,
          currentBookId: game.bookId,
          currentSectionId: game.currentSectionId,
          health: game.health,
          choices: [...(current.choices ?? []), optionIndex],
          status: game.status,
          bookTitle: game.bookTitle
        };
        this.gameState.next(state);
        return state;
      })
    );
  }

  getCurrentGameState(): GameState {
    return this.gameState.value;
  }
}
