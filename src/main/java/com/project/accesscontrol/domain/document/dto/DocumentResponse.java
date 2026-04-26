package com.project.accesscontrol.domain.document.dto;

import java.time.LocalDateTime;

public class DocumentResponse {

    private Long id;
    private String title;
    private String content;
    private Long ownerUserId;
    private String ownerUsername;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public DocumentResponse(Long id,
                            String title,
                            String content,
                            Long ownerUserId,
                            String ownerUsername,
                            String createdBy,
                            String updatedBy,
                            LocalDateTime createdAt,
                            LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.ownerUserId = ownerUserId;
        this.ownerUsername = ownerUsername;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Long getOwnerUserId() {
        return ownerUserId;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}