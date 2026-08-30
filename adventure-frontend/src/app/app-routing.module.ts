import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { BookListComponent } from './components/book-list/book-list.component';
import { StoryPlayerComponent } from './components/story-player/story-player.component';

const routes: Routes = [
  { path: '', component: BookListComponent },
  { path: 'play/:id', component: StoryPlayerComponent },
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
