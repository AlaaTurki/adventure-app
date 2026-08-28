import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { BookService } from '../../services/book.service';

@Component({
  selector: 'app-add-book',
  templateUrl: './add-book.component.html',
  styleUrls: ['./add-book.component.css']
})
export class AddBookComponent {
  public jsonText = '';
  public error: string | null = null;
  public success: string | null = null;
  public submitting = false;

  constructor(private bookService: BookService, private router: Router) { }

  submit(): void {
    this.error = null;
    this.success = null;
    try {
      const parsed = JSON.parse(this.jsonText);
      this.submitting = true;
      this.bookService.createBook(parsed).subscribe({
        next: () => {
          this.success = 'Book added successfully.';
          this.submitting = false;
          setTimeout(() => this.router.navigate(['/']), 800);
        },
        error: (err) => {
          this.error = err?.error?.message || 'Failed to add book';
          this.submitting = false;
        }
      });
    } catch (e) {
      this.error = 'Invalid JSON format. Please paste valid book JSON.';
    }
  }
}