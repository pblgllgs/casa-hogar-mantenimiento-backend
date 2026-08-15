package com.casahogar.mantenimiento.maintenance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class WorkOrderCommentRequest {
    @NotNull
    private Long workOrderId;

    @NotBlank
    @Size(max = 5000)
    private String content;

    @NotNull
    private CommentType commentType;

    private Boolean isInternal = false;

    public enum CommentType {
        STATUS_CHANGE, PROGRESS_UPDATE, NOTE, MATERIAL_REQUEST,
        VENDOR_QUOTE, APPROVAL_REQUEST, COMPLETION_REPORT
    }

    public Long getWorkOrderId() { return workOrderId; }
    public void setWorkOrderId(Long workOrderId) { this.workOrderId = workOrderId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public CommentType getCommentType() { return commentType; }
    public void setCommentType(CommentType commentType) { this.commentType = commentType; }

    public Boolean getIsInternal() { return isInternal; }
    public void setIsInternal(Boolean isInternal) { this.isInternal = isInternal; }
}
