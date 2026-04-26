package com.project.accesscontrol.domain.document.repository;

import com.project.accesscontrol.domain.document.entity.Document;
import com.project.accesscontrol.domain.user.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    @EntityGraph(attributePaths = "owner")
    List<Document> findAllByOwner(User owner);

    @EntityGraph(attributePaths = "owner")
    List<Document> findAllByOwnerId(Long ownerId);

    @EntityGraph(attributePaths = "owner")
    List<Document> findAll();

    @EntityGraph(attributePaths = "owner")
    Optional<Document> findById(Long id);
}
