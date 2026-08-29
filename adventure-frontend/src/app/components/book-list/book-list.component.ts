import { Component, OnInit } from '@angular/core';
import { BookService } from '../../services/book.service';
import { GameStateService } from '../../services/game-state.service';
import { Book } from '../../models/book.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-book-list',
  templateUrl: './book-list.component.html',
  styleUrls: ['./book-list.component.css']
})
export class BookListComponent implements OnInit {
  books: Book[] = [];
  filteredBooks: Book[] = [];
  loading = false;
  error: string | null = null;
  searchTerm = '';
  activeFilter = 'All';
  filters = ['All', 'Fantasy', 'Adventure', 'High Fantasy', 'Steampunk Mystery', 'Easy', 'Medium', 'Hard'];

  constructor(
    private bookService: BookService,
    private gameStateService: GameStateService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadBooks();
  }

  loadBooks(): void {
    this.loading = true;
    this.error = null;
    const difficultyFilter = this.getDifficultyFilterValue();
    this.bookService.getAllBooks(this.searchTerm, difficultyFilter ?? undefined).subscribe({
      next: (data) => {
        this.books = data;
        this.applyFilters();
        this.loading = false;
      },
      error: () => {
        this.error = 'Failed to load books. Make sure the backend is running on port 8080.';
        this.loading = false;
      }
    });
  }

  applyFilters(): void {
    const query = this.searchTerm.trim().toLowerCase();

    this.filteredBooks = this.books.filter((book) => {
      const matchesQuery = !query ||
        book.title.toLowerCase().includes(query) ||
        book.author.toLowerCase().includes(query) ||
        book.difficulty.toLowerCase().includes(query);

      const matchesFilter = this.activeFilter === 'All' ||
        this.activeFilter === book.difficulty ||
        (this.activeFilter === 'Fantasy' && (book.difficulty === 'EASY' || book.difficulty === 'MEDIUM')) ||
        (this.activeFilter === 'Adventure' && book.difficulty === 'EASY') ||
        (this.activeFilter === 'High Fantasy' && book.difficulty === 'HARD') ||
        (this.activeFilter === 'Easy' && book.difficulty === 'EASY') ||
        (this.activeFilter === 'Medium' && book.difficulty === 'MEDIUM') ||
        (this.activeFilter === 'Hard' && book.difficulty === 'HARD');

      return matchesQuery && matchesFilter;
    });
  }

  private getDifficultyFilterValue(): string | null {
    if (this.activeFilter === 'All') return null;
    if (this.activeFilter === 'Easy') return 'EASY';
    if (this.activeFilter === 'Medium') return 'MEDIUM';
    if (this.activeFilter === 'Hard') return 'HARD';
    return null;
  }

  onSearch(): void {
    this.applyFilters();
  }

  setFilter(filter: string): void {
    this.activeFilter = filter;
    this.applyFilters();
  }

  selectBook(book: Book): void {
    if (book.id) {
      // backend is authoritative; StoryPlayer will start the game when it loads the book
      this.router.navigate(['/play', book.id]);
    }
  }

  getDifficultyColor(difficulty: string): string {
    switch (difficulty) {
      case 'EASY':
        return 'easy';
      case 'MEDIUM':
        return 'medium';
      case 'HARD':
        return 'hard';
      default:
        return 'secondary';
    }
  }

  getBookDescription(book: Book): string {
    return book.description ?? 'No description available.';
  }

  getBookTags(book: Book): string[] {
    return book.tags ?? [];
  }

  getBookRuntime(book: Book): string {
    const chapterCount = book.sections?.length ?? 0;
    const minutes = Math.max(15, chapterCount * 12);
    return `${minutes} min`;
  }
}
