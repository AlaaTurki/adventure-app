export interface Consequence {
  type: string;
  value: string;
  text: string;
}

export interface Choice {
  description: string;
  gotoId: number;
  consequence?: Consequence;
}

export interface Section {
  id: number;
  text: string;
  type: string;
  options?: Choice[];
}

export interface Book {
  id?: number;
  title: string;
  author: string;
  difficulty: string;
  sections: Section[];
}

export interface GameState {
  gameId?: string;
  currentBookId: number;
  currentSectionId: number;
  health: number;
  choices: number[];
  status?: string;
  bookTitle?: string;
}
