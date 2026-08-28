package com.pictet.adventure.repository;

import com.pictet.adventure.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {
    @Query("SELECT s FROM Book b JOIN b.sections s WHERE b.id = :bookId AND s.sectionId = :sectionId")
    Optional<Section> findByBookIdAndSectionId(@Param("bookId") Long bookId, @Param("sectionId") Integer sectionId);
}
