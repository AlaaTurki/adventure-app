package com.pictet.adventure.util;

import com.pictet.adventure.dto.BookDTO;
import com.pictet.adventure.dto.SectionDTO;
import com.pictet.adventure.dto.ChoiceDTO;
import com.pictet.adventure.dto.ConsequenceDTO;
import com.pictet.adventure.model.Book;
import com.pictet.adventure.model.Section;
import com.pictet.adventure.model.Choice;
import com.pictet.adventure.model.Consequence;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class BookMapper {
    
    public BookDTO toDTO(Book book) {
        if (book == null) return null;
        
        BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setDifficulty(book.getDifficulty());
        
        if (book.getSections() != null) {
            dto.setSections(book.getSections().stream()
                    .map(this::sectionToDTO)
                    .collect(Collectors.toList()));
        }
        
        return dto;
    }
    
    public SectionDTO sectionToDTO(Section section) {
        if (section == null) return null;
        
        SectionDTO dto = new SectionDTO();
        dto.setId(section.getSectionId());
        dto.setText(section.getText());
        dto.setType(section.getType());
        
        if (section.getOptions() != null) {
            dto.setOptions(section.getOptions().stream()
                    .map(this::choiceToDTO)
                    .collect(Collectors.toList()));
        }
        
        return dto;
    }
    
    public ChoiceDTO choiceToDTO(Choice choice) {
        if (choice == null) return null;
        
        ChoiceDTO dto = new ChoiceDTO();
        dto.setDescription(choice.getDescription());
        dto.setGotoId(choice.getGotoId());
        
        if (choice.getConsequence() != null) {
            dto.setConsequence(consequenceToDTO(choice.getConsequence()));
        }
        
        return dto;
    }
    
    public ConsequenceDTO consequenceToDTO(Consequence consequence) {
        if (consequence == null) return null;
        
        ConsequenceDTO dto = new ConsequenceDTO();
        dto.setType(consequence.getType());
        dto.setValue(consequence.getValue());
        dto.setText(consequence.getText());
        
        return dto;
    }
    
    public Book toEntity(BookDTO dto) {
        if (dto == null) return null;
        
        Book book = new Book();
        book.setId(dto.getId());
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setDifficulty(dto.getDifficulty());
        
        if (dto.getSections() != null) {
            book.setSections(dto.getSections().stream()
                    .map(this::sectionToEntity)
                    .collect(Collectors.toList()));
        }
        
        return book;
    }
    
    public Section sectionToEntity(SectionDTO dto) {
        if (dto == null) return null;
        
        Section section = new Section();
        section.setSectionId(dto.getId());
        section.setText(dto.getText());
        section.setType(dto.getType());
        
        if (dto.getOptions() != null) {
            section.setOptions(dto.getOptions().stream()
                    .map(this::choiceToEntity)
                    .collect(Collectors.toList()));
        }
        
        return section;
    }
    
    public Choice choiceToEntity(ChoiceDTO dto) {
        if (dto == null) return null;
        
        Choice choice = new Choice();
        choice.setDescription(dto.getDescription());
        choice.setGotoId(dto.getGotoId());
        
        if (dto.getConsequence() != null) {
            choice.setConsequence(consequenceToEntity(dto.getConsequence()));
        }
        
        return choice;
    }
    
    public Consequence consequenceToEntity(ConsequenceDTO dto) {
        if (dto == null) return null;
        
        Consequence consequence = new Consequence();
        consequence.setType(dto.getType());
        consequence.setValue(dto.getValue());
        consequence.setText(dto.getText());
        
        return consequence;
    }
}
