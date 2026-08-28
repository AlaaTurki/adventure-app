import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Book, Section } from '../models/book.model';

@Injectable({
  providedIn: 'root'
})
export class BookService {
  private apiUrl = 'http://localhost:8080/api/books';
  private gameApiUrl = 'http://localhost:8080/api/games';

  constructor(private http: HttpClient) { }

  getAllBooks(search?: string | null, difficulty?: string | null): Observable<Book[]> {
    let params = new HttpParams();
    if (search) { params = params.set('search', search); }
    if (difficulty && difficulty !== 'All') { params = params.set('difficulty', difficulty); }
    return this.http.get<Book[]>(this.apiUrl, { params });
  }

  getBookById(id: number): Observable<Book> {
    return this.http.get<Book>(`${this.apiUrl}/${id}`);
  }

  getBookByTitle(title: string): Observable<Book> {
    return this.http.get<Book>(`${this.apiUrl}/title/${encodeURIComponent(title)}`);
  }

  getSection(bookId: number, sectionId: number): Observable<Section> {
    return this.http.get<Section>(`${this.apiUrl}/${bookId}/sections/${sectionId}`);
  }

  createBook(book: any): Observable<Book> {
    return this.http.post<Book>(this.apiUrl, book);
  }

  startGame(bookId: number): Observable<any> {
    return this.http.post<any>(`${this.gameApiUrl}/start`, null, { params: { bookId: String(bookId) } });
  }

  getGame(gameId: string): Observable<any> {
    return this.http.get<any>(`${this.gameApiUrl}/${gameId}`);
  }

  chooseOption(gameId: string, optionIndex: number): Observable<any> {
    return this.http.post<any>(`${this.gameApiUrl}/${gameId}/choices`, { optionIndex });
  }
}
