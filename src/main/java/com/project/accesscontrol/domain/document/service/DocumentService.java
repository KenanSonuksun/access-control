package com.project.accesscontrol.domain.document.service;

import com.project.accesscontrol.common.exception.ResourceNotFoundException;
import com.project.accesscontrol.domain.document.entity.Document;
import com.project.accesscontrol.domain.document.repository.DocumentRepository;
import com.project.accesscontrol.domain.user.entity.User;
import com.project.accesscontrol.domain.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final UserService userService;

    public DocumentService(DocumentRepository documentRepository,
                           UserService userService) {
        this.documentRepository = documentRepository;
        this.userService = userService;
    }

    public Document createDocument(String title, String content, String ownerUsername) {
        User owner = userService.getRequiredUserByUsername(ownerUsername);
        Document document = new Document(title, content, owner);
        document.initializeAudit(ownerUsername);
        return documentRepository.save(document);
    }

    @Transactional(readOnly = true)
    public List<Document> getDocumentsByOwnerUsername(String ownerUsername) {
        User owner = userService.getRequiredUserByUsername(ownerUsername);
        return documentRepository.findAllByOwnerId(owner.getId());
    }

    @Transactional(readOnly = true)
    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Document getRequiredDocument(Long documentId) {
        return documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found with id: " + documentId));
    }
}