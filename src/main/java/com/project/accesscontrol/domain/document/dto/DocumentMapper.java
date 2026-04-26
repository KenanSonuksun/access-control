package com.project.accesscontrol.domain.document.dto;

import com.project.accesscontrol.domain.document.entity.Document;

public final class DocumentMapper {

    private DocumentMapper() {
    }

    public static DocumentResponse toResponse(Document document) {
        return new DocumentResponse(
                document.getId(),
                document.getTitle(),
                document.getContent(),
                document.getOwner().getId(),
                document.getOwner().getUsername(),
                document.getCreatedBy(),
                document.getUpdatedBy(),
                document.getCreatedAt(),
                document.getUpdatedAt()
        );
    }
}