package com.pictet.adventure.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "consequences")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Consequence {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String type;
    
    @Column(name = "consequence_value")
    private String value;
    
    private String text;
}
