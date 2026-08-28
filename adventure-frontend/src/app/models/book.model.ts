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
  currentBookId: number;
  currentSectionId: number;
  health: number;
  maxHealth: number;
  choices: number[];
}
