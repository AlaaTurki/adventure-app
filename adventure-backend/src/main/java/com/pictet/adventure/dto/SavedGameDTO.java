package com.pictet.adventure.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Setter
@Getter
public class SavedGameDTO {
    private UUID id;
    private UUID gameId;
    private Long bookId;
    private Instant createdAt;
    private String snapshot;

}
