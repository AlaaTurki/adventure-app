package com.pictet.adventure.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "saved_games")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SavedGame {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID gameId;

    @Column(nullable = false)
    private Long bookId;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    @Lob
    @Column(nullable = false)
    private String snapshot; // JSON snapshot of the game state
}
