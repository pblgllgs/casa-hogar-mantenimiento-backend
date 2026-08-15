package com.casahogar.mantenimiento.maintenance.controller;

import com.casahogar.mantenimiento.common.dto.ApiResponse;
import com.casahogar.mantenimiento.common.dto.PageResponse;
import com.casahogar.mantenimiento.common.dto.SearchCriteria;
import com.casahogar.mantenimiento.maintenance.dto.WorkOrderCommentRequest;
import com.casahogar.mantenimiento.maintenance.dto.WorkOrderCommentResponse;
import com.casahogar.mantenimiento.maintenance.dto.WorkOrderRequest;
import com.casahogar.mantenimiento.maintenance.dto.WorkOrderResponse;
import com.casahogar.mantenimiento.maintenance.service.WorkOrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/maintenance/work-orders")
public class WorkOrderController {

    private final WorkOrderService workOrderService;

    public WorkOrderController(WorkOrderService workOrderService) {
        this.workOrderService = workOrderService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR', 'MAINTENANCE')")
    public ResponseEntity<ApiResponse<WorkOrderResponse>> create(
            @Valid @RequestBody WorkOrderRequest request,
            Authentication auth) {
        WorkOrderResponse response = workOrderService.create(request, auth.getName());
        return ResponseEntity.ok(ApiResponse.ok(response, "Orden de trabajo creada"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR', 'MAINTENANCE')")
    public ResponseEntity<ApiResponse<WorkOrderResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody WorkOrderRequest request,
            Authentication auth) {
        WorkOrderResponse response = workOrderService.update(id, request, auth.getName());
        return ResponseEntity.ok(ApiResponse.ok(response, "Orden de trabajo actualizada"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long id,
            Authentication auth) {
        workOrderService.delete(id, auth.getName());
        return ResponseEntity.ok(ApiResponse.ok(null, "Orden de trabajo eliminada"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<WorkOrderResponse>> getById(@PathVariable Long id) {
        WorkOrderResponse response = workOrderService.getById(id);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/number/{orderNumber}")
    public ResponseEntity<ApiResponse<WorkOrderResponse>> getByOrderNumber(@PathVariable String orderNumber) {
        return workOrderService.getByOrderNumber(orderNumber)
                .map(r -> ResponseEntity.ok(ApiResponse.ok(r)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<WorkOrderResponse>>> search(
            SearchCriteria criteria) {
        PageResponse<WorkOrderResponse> response = workOrderService.search(criteria);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/assigned-to/{userId}")
    public ResponseEntity<ApiResponse<List<WorkOrderResponse>>> getByAssignedTo(@PathVariable Long userId) {
        List<WorkOrderResponse> response = workOrderService.getByAssignedTo(userId);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/requested-by/{userId}")
    public ResponseEntity<ApiResponse<List<WorkOrderResponse>>> getByRequestedBy(@PathVariable Long userId) {
        List<WorkOrderResponse> response = workOrderService.getByRequestedBy(userId);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/asset/{assetId}")
    public ResponseEntity<ApiResponse<List<WorkOrderResponse>>> getByAsset(@PathVariable Long assetId) {
        List<WorkOrderResponse> response = workOrderService.getByAsset(assetId);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/location/{locationId}")
    public ResponseEntity<ApiResponse<List<WorkOrderResponse>>> getByLocation(@PathVariable Long locationId) {
        List<WorkOrderResponse> response = workOrderService.getByLocation(locationId);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<WorkOrderResponse>>> getByStatus(@PathVariable String status) {
        List<WorkOrderResponse> response = workOrderService.getByStatus(status);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/date-range")
    public ResponseEntity<ApiResponse<List<WorkOrderResponse>>> getByDateRange(
            @RequestParam LocalDate start,
            @RequestParam LocalDate end) {
        List<WorkOrderResponse> response = workOrderService.getByDateRange(start, end);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR', 'MAINTENANCE')")
    public ResponseEntity<ApiResponse<WorkOrderResponse>> changeStatus(
            @PathVariable Long id,
            @RequestParam String status,
            Authentication auth) {
        WorkOrderResponse response = workOrderService.changeStatus(id,
                com.casahogar.mantenimiento.maintenance.entity.WorkOrder.WorkOrderStatus.valueOf(status),
                auth.getName());
        return ResponseEntity.ok(ApiResponse.ok(response, "Estado actualizado"));
    }

    @PostMapping("/{id}/comments")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR', 'MAINTENANCE')")
    public ResponseEntity<ApiResponse<WorkOrderCommentResponse>> addComment(
            @PathVariable Long id,
            @Valid @RequestBody WorkOrderCommentRequest request,
            Authentication auth) {
        request.setWorkOrderId(id);
        WorkOrderCommentResponse response = workOrderService.addComment(request, auth.getName());
        return ResponseEntity.ok(ApiResponse.ok(response, "Comentario agregado"));
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<ApiResponse<List<WorkOrderCommentResponse>>> getComments(@PathVariable Long id) {
        List<WorkOrderCommentResponse> response = workOrderService.getComments(id);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }
}
