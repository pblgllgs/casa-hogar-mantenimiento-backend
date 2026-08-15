package com.casahogar.mantenimiento.maintenance.service;

import com.casahogar.mantenimiento.auth.entity.User;
import com.casahogar.mantenimiento.auth.repository.UserRepository;
import com.casahogar.mantenimiento.common.dto.PageResponse;
import com.casahogar.mantenimiento.common.dto.SearchCriteria;
import com.casahogar.mantenimiento.maintenance.dto.WorkOrderCommentRequest;
import com.casahogar.mantenimiento.maintenance.dto.WorkOrderCommentResponse;
import com.casahogar.mantenimiento.maintenance.dto.WorkOrderRequest;
import com.casahogar.mantenimiento.maintenance.dto.WorkOrderResponse;
import com.casahogar.mantenimiento.maintenance.entity.WorkOrder;
import com.casahogar.mantenimiento.maintenance.entity.WorkOrderComment;
import com.casahogar.mantenimiento.maintenance.repository.WorkOrderCommentRepository;
import com.casahogar.mantenimiento.maintenance.repository.WorkOrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WorkOrderService {

    private final WorkOrderRepository workOrderRepository;
    private final WorkOrderCommentRepository commentRepository;
    private final UserRepository userRepository;

    public WorkOrderService(WorkOrderRepository workOrderRepository,
                            WorkOrderCommentRepository commentRepository,
                            UserRepository userRepository) {
        this.workOrderRepository = workOrderRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public WorkOrderResponse create(WorkOrderRequest request, String currentUser) {
        User requester = userRepository.findById(request.getRequestedById())
                .orElseThrow(() -> new IllegalArgumentException("Solicitante no encontrado"));

        WorkOrder workOrder = new WorkOrder();
        workOrder.setOrderNumber(generateOrderNumber());
        workOrder.setTitle(request.getTitle());
        workOrder.setDescription(request.getDescription());
        workOrder.setType(mapType(request.getType()));
        workOrder.setPriority(mapPriority(request.getPriority()));
        workOrder.setStatus(WorkOrder.WorkOrderStatus.PENDING);
        workOrder.setLocationId(request.getLocationId());
        workOrder.setLocationName(request.getLocationName());
        workOrder.setAssetId(request.getAssetId());
        workOrder.setAssetName(request.getAssetName());
        workOrder.setRequestedById(requester.getId());
        workOrder.setRequestedByName(requester.getFullName());
        workOrder.setAssignedToId(request.getAssignedToId());
        workOrder.setAssignedToName(resolveAssignedToName(request.getAssignedToId()));
        workOrder.setSupervisorId(request.getSupervisorId());
        workOrder.setSupervisorName(resolveAssignedToName(request.getSupervisorId()));
        workOrder.setEstimatedHours(request.getEstimatedHours());
        workOrder.setScheduledStartDate(request.getScheduledStartDate());
        workOrder.setScheduledEndDate(request.getScheduledEndDate());
        workOrder.setCostMaterials(request.getCostMaterials());
        workOrder.setCostLabor(request.getCostLabor());
        workOrder.setRequiresExternalVendor(request.getRequiresExternalVendor() != null ? request.getRequiresExternalVendor() : false);
        workOrder.setVendorName(request.getVendorName());
        workOrder.setVendorContact(request.getVendorContact());

        workOrderRepository.save(workOrder);
        return WorkOrderResponse.of(workOrder);
    }

    @Transactional
    public WorkOrderResponse update(Long id, WorkOrderRequest request, String currentUser) {
        WorkOrder workOrder = workOrderRepository.findByIdActive(id)
                .orElseThrow(() -> new IllegalArgumentException("Orden de trabajo no encontrada"));

        workOrder.setTitle(request.getTitle());
        workOrder.setDescription(request.getDescription());
        workOrder.setType(mapType(request.getType()));
        workOrder.setPriority(mapPriority(request.getPriority()));
        workOrder.setLocationId(request.getLocationId());
        workOrder.setLocationName(request.getLocationName());
        workOrder.setAssetId(request.getAssetId());
        workOrder.setAssetName(request.getAssetName());
        workOrder.setAssignedToId(request.getAssignedToId());
        workOrder.setAssignedToName(resolveAssignedToName(request.getAssignedToId()));
        workOrder.setSupervisorId(request.getSupervisorId());
        workOrder.setSupervisorName(resolveAssignedToName(request.getSupervisorId()));
        workOrder.setEstimatedHours(request.getEstimatedHours());
        workOrder.setScheduledStartDate(request.getScheduledStartDate());
        workOrder.setScheduledEndDate(request.getScheduledEndDate());
        workOrder.setCostMaterials(request.getCostMaterials());
        workOrder.setCostLabor(request.getCostLabor());
        workOrder.setRequiresExternalVendor(request.getRequiresExternalVendor() != null ? request.getRequiresExternalVendor() : false);
        workOrder.setVendorName(request.getVendorName());
        workOrder.setVendorContact(request.getVendorContact());

        workOrderRepository.save(workOrder);
        return WorkOrderResponse.of(workOrder);
    }

    @Transactional
    public void delete(Long id, String currentUser) {
        workOrderRepository.softDeleteById(id, currentUser);
    }

    public WorkOrderResponse getById(Long id) {
        WorkOrder workOrder = workOrderRepository.findByIdActive(id)
                .orElseThrow(() -> new IllegalArgumentException("Orden de trabajo no encontrada"));
        return WorkOrderResponse.of(workOrder);
    }

    public Optional<WorkOrderResponse> getByOrderNumber(String orderNumber) {
        return workOrderRepository.findByOrderNumber(orderNumber).map(WorkOrderResponse::of);
    }

    public PageResponse<WorkOrderResponse> search(SearchCriteria criteria) {
        Pageable pageable = criteria.toPageRequest();
        Page<WorkOrder> page = (criteria.getSearch() != null && !criteria.getSearch().isBlank())
                ? workOrderRepository.search(criteria.getSearch(), pageable)
                : workOrderRepository.findAllByDeletedFalse(pageable);
        return PageResponse.of(page.map(WorkOrderResponse::of));
    }

    public List<WorkOrderResponse> getByAssignedTo(Long userId) {
        return workOrderRepository.findByAssignedToId(userId).stream()
                .map(WorkOrderResponse::of)
                .collect(Collectors.toList());
    }

    public List<WorkOrderResponse> getByRequestedBy(Long userId) {
        return workOrderRepository.findByRequestedById(userId).stream()
                .map(WorkOrderResponse::of)
                .collect(Collectors.toList());
    }

    public List<WorkOrderResponse> getByAsset(Long assetId) {
        return workOrderRepository.findByAssetId(assetId).stream()
                .map(WorkOrderResponse::of)
                .collect(Collectors.toList());
    }

    public List<WorkOrderResponse> getByLocation(Long locationId) {
        return workOrderRepository.findByLocationId(locationId).stream()
                .map(WorkOrderResponse::of)
                .collect(Collectors.toList());
    }

    public List<WorkOrderResponse> getByStatus(String status) {
        return workOrderRepository.findByStatus(status).stream()
                .map(WorkOrderResponse::of)
                .collect(Collectors.toList());
    }

    public List<WorkOrderResponse> getByDateRange(LocalDate start, LocalDate end) {
        return workOrderRepository.findByScheduledDateRange(start, end).stream()
                .map(WorkOrderResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public WorkOrderResponse changeStatus(Long id, WorkOrder.WorkOrderStatus newStatus, String currentUser) {
        WorkOrder workOrder = workOrderRepository.findByIdActive(id)
                .orElseThrow(() -> new IllegalArgumentException("Orden de trabajo no encontrada"));

        WorkOrder.WorkOrderStatus oldStatus = workOrder.getStatus();
        workOrder.setStatus(newStatus);

        if (newStatus == WorkOrder.WorkOrderStatus.IN_PROGRESS && oldStatus != WorkOrder.WorkOrderStatus.IN_PROGRESS) {
            workOrder.setActualStartDate(LocalDateTime.now());
        }
        if (newStatus == WorkOrder.WorkOrderStatus.COMPLETED) {
            workOrder.setActualEndDate(LocalDateTime.now());
        }

        workOrderRepository.save(workOrder);
        addComment(id, currentUser, "Estado cambiado de " + oldStatus + " a " + newStatus,
                WorkOrderComment.CommentType.STATUS_CHANGE, false);
        return WorkOrderResponse.of(workOrder);
    }

    @Transactional
    public WorkOrderCommentResponse addComment(WorkOrderCommentRequest request, String currentUser) {
        workOrderRepository.findById(request.getWorkOrderId())
                .orElseThrow(() -> new IllegalArgumentException("Orden de trabajo no encontrada"));

        User author = userRepository.findByUsername(currentUser)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        WorkOrderComment comment = new WorkOrderComment();
        comment.setWorkOrderId(request.getWorkOrderId());
        comment.setAuthorId(author.getId());
        comment.setAuthorName(author.getFullName());
        comment.setAuthorRole(getUserRoles(author));
        comment.setContent(request.getContent());
        comment.setCommentType(mapCommentType(request.getCommentType()));
        comment.setIsInternal(request.getIsInternal() != null ? request.getIsInternal() : false);

        commentRepository.save(comment);
        return WorkOrderCommentResponse.of(comment);
    }

    public List<WorkOrderCommentResponse> getComments(Long workOrderId) {
        return commentRepository.findByWorkOrderId(workOrderId).stream()
                .map(WorkOrderCommentResponse::of)
                .collect(Collectors.toList());
    }

    private String generateOrderNumber() {
        long count = workOrderRepository.count() + 1;
        return String.format("WO-%06d", count);
    }

    private String resolveAssignedToName(Long userId) {
        if (userId == null) return null;
        return userRepository.findById(userId)
                .map(User::getFullName)
                .orElse(null);
    }

    private String getUserRoles(User user) {
        return user.getRoles().stream()
                .map(Enum::name)
                .collect(Collectors.joining(","));
    }

    private WorkOrder.WorkOrderType mapType(WorkOrderRequest.WorkOrderType dtoType) {
        if (dtoType == null) return WorkOrder.WorkOrderType.CORRECTIVE;
        return WorkOrder.WorkOrderType.valueOf(dtoType.name());
    }

    private WorkOrder.Priority mapPriority(WorkOrderRequest.Priority dtoPriority) {
        if (dtoPriority == null) return WorkOrder.Priority.MEDIUM;
        return WorkOrder.Priority.valueOf(dtoPriority.name());
    }

    private WorkOrderComment.CommentType mapCommentType(WorkOrderCommentRequest.CommentType dtoType) {
        if (dtoType == null) return WorkOrderComment.CommentType.NOTE;
        return WorkOrderComment.CommentType.valueOf(dtoType.name());
    }

    private void addComment(Long workOrderId, String author, String content,
                           WorkOrderComment.CommentType type, boolean isInternal) {
        User user = userRepository.findByUsername(author)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        WorkOrderComment comment = new WorkOrderComment();
        comment.setWorkOrderId(workOrderId);
        comment.setAuthorId(user.getId());
        comment.setAuthorName(user.getFullName());
        comment.setAuthorRole(getUserRoles(user));
        comment.setContent(content);
        comment.setCommentType(type);
        comment.setIsInternal(isInternal);
        commentRepository.save(comment);
    }
}
