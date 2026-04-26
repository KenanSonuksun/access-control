package com.project.accesscontrol.domain.document.controller;

import com.project.accesscontrol.domain.document.dto.CreateDocumentRequest;
import com.project.accesscontrol.domain.document.dto.DocumentMapper;
import com.project.accesscontrol.domain.document.dto.DocumentResponse;
import com.project.accesscontrol.domain.document.entity.Document;
import com.project.accesscontrol.domain.document.service.DocumentService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping
    public DocumentResponse createDocument(@Valid @RequestBody CreateDocumentRequest request,
                                           Authentication authentication) {
        Document document = documentService.createDocument(
                request.getTitle(),
                request.getContent(),
                authentication.getName()
        );

        return DocumentMapper.toResponse(document);
    }

    @GetMapping("/me")
    public List<DocumentResponse> getMyDocuments(Authentication authentication) {
        return documentService.getDocumentsByOwnerUsername(authentication.getName())
                .stream()
                .map(DocumentMapper::toResponse)
                .toList();
    }

    @GetMapping
    public List<DocumentResponse> getAllDocuments() {
        return documentService.getAllDocuments()
                .stream()
                .map(DocumentMapper::toResponse)
                .toList();
    }
}