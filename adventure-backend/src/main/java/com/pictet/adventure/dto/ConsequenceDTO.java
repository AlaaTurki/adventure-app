package com.pictet.adventure.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsequenceDTO {
    private String type;
    private String value;
    private String text;
}
