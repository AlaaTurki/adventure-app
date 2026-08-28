package com.pictet.adventure.repository;

import com.pictet.adventure.model.SavedGame;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SavedGameRepository extends JpaRepository<SavedGame, UUID> {
    List<SavedGame> findByBookId(Long bookId);
    List<SavedGame> findByGameId(UUID gameId);
}
