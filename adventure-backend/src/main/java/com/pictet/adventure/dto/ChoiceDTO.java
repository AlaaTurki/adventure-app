package com.pictet.adventure.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChoiceDTO {
    private String description;
    private Integer gotoId;
    private ConsequenceDTO consequence;
}
