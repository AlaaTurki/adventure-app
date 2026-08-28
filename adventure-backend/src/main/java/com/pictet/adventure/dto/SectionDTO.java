package com.pictet.adventure.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SectionDTO {
    private Integer id;
    private String text;
    private String type;
    private List<ChoiceDTO> options;
}
