package com.pictet.adventure.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameDTO {
    private UUID id;
    private Long bookId;
    private String bookTitle;
    private Integer currentSectionId;
    private Integer health;
    private String status;
}
