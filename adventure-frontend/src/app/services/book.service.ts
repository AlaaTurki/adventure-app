import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Book, Section } from '../models/book.model';

@Injectable({
  providedIn: 'root'
})
export class BookService {
  private apiUrl = 'http://localhost:8080/books';

  constructor(private http: HttpClient) { }

  getAllBooks(): Observable<Book[]> {
    return this.http.get<Book[]>(this.apiUrl);
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
}
