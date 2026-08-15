package com.casahogar.mantenimiento.maintenance.dto;

import com.casahogar.mantenimiento.maintenance.entity.WorkOrderComment;

import java.time.LocalDateTime;

public class WorkOrderCommentResponse {
    private Long id;
    private Long workOrderId;
    private Long authorId;
    private String authorName;
    private String authorRole;
    private String content;
    private String commentType;
    private Boolean isInternal;
    private LocalDateTime createdAt;

    public static WorkOrderCommentResponse of(WorkOrderComment comment) {
        WorkOrderCommentResponse r = new WorkOrderCommentResponse();
        r.id = comment.getId();
        r.workOrderId = comment.getWorkOrderId();
        r.authorId = comment.getAuthorId();
        r.authorName = comment.getAuthorName();
        r.authorRole = comment.getAuthorRole();
        r.content = comment.getContent();
        r.commentType = comment.getCommentType() != null ? comment.getCommentType().name() : null;
        r.isInternal = comment.getIsInternal();
        r.createdAt = comment.getCreatedAt();
        return r;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getWorkOrderId() { return workOrderId; }
    public void setWorkOrderId(Long workOrderId) { this.workOrderId = workOrderId; }

    public Long getAuthorId() { return authorId; }
    public void setAuthorId(Long authorId) { this.authorId = authorId; }

    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }

    public String getAuthorRole() { return authorRole; }
    public void setAuthorRole(String authorRole) { this.authorRole = authorRole; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getCommentType() { return commentType; }
    public void setCommentType(String commentType) { this.commentType = commentType; }

    public Boolean getIsInternal() { return isInternal; }
    public void setIsInternal(Boolean isInternal) { this.isInternal = isInternal; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
