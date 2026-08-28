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
    this.bookService.getAllBooks().subscribe({
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
        (this.activeFilter === 'Steampunk Mystery' && book.title.toLowerCase().includes('jade')) ||
        (this.activeFilter === 'Easy' && book.difficulty === 'EASY') ||
        (this.activeFilter === 'Medium' && book.difficulty === 'MEDIUM') ||
        (this.activeFilter === 'Hard' && book.difficulty === 'HARD');

      return matchesQuery && matchesFilter;
    });
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
      this.gameStateService.startGame(book.id);
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
    const title = book.title.toLowerCase();
    if (title.includes('crystal')) {
      return 'Deep beneath the mountain lies a network of crystal caves filled with ancient magic and dangerous creatures. Your choices will determine whether you emerge as a hero or become another lost soul in the depths.';
    }
    if (title.includes('jade')) {
      return 'Set sail on the treacherous Jade Sea where pirates rule and treasure awaits the bold. Navigate through storms, rival crews, and ancient curses in this swashbuckling adventure.';
    }
    if (title.includes('dragon')) {
      return 'The kingdom calls for a dragon hunter, and the mountain holds secrets older than most empires. Face fire, fear, and destiny in one final reckless sprint.';
    }
    return 'An ancient prison door, a whispering key, and a choice that may change your fate. Survive the darkness and uncover what truly matters.';
  }

  getBookTags(book: Book): string[] {
    const title = book.title.toLowerCase();
    if (title.includes('crystal')) {
      return ['Magic', 'Underground', 'Crystals'];
    }
    if (title.includes('jade')) {
      return ['Pirates', 'Ocean', 'Treasure'];
    }
    if (title.includes('dragon')) {
      return ['Dragons', 'Magic', 'Quest'];
    }
    return ['Mystery', 'Prison', 'Escape'];
  }

  getBookRuntime(book: Book): string {
    const chapterCount = book.sections?.length ?? 0;
    const minutes = Math.max(15, chapterCount * 12);
    return `${minutes} min`;
  }
}
