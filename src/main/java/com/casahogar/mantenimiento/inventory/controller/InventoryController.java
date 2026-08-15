package com.casahogar.mantenimiento.inventory.controller;

import com.casahogar.mantenimiento.common.dto.ApiResponse;
import com.casahogar.mantenimiento.common.dto.PageResponse;
import com.casahogar.mantenimiento.common.dto.SearchCriteria;
import com.casahogar.mantenimiento.inventory.dto.InventoryItemRequest;
import com.casahogar.mantenimiento.inventory.dto.InventoryItemResponse;
import com.casahogar.mantenimiento.inventory.dto.InventoryMovementRequest;
import com.casahogar.mantenimiento.inventory.dto.InventoryMovementResponse;
import com.casahogar.mantenimiento.inventory.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping("/items")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY')")
    public ResponseEntity<ApiResponse<InventoryItemResponse>> createItem(
            @Valid @RequestBody InventoryItemRequest request,
            Authentication auth) {
        InventoryItemResponse response = inventoryService.createItem(request, auth.getName());
        return ResponseEntity.ok(ApiResponse.ok(response, "Item de inventario creado"));
    }

    @PutMapping("/items/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY')")
    public ResponseEntity<ApiResponse<InventoryItemResponse>> updateItem(
            @PathVariable Long id,
            @Valid @RequestBody InventoryItemRequest request,
            Authentication auth) {
        InventoryItemResponse response = inventoryService.updateItem(id, request, auth.getName());
        return ResponseEntity.ok(ApiResponse.ok(response, "Item de inventario actualizado"));
    }

    @DeleteMapping("/items/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteItem(
            @PathVariable Long id,
            Authentication auth) {
        inventoryService.deleteItem(id, auth.getName());
        return ResponseEntity.ok(ApiResponse.ok(null, "Item de inventario eliminado"));
    }

    @GetMapping("/items/{id}")
    public ResponseEntity<ApiResponse<InventoryItemResponse>> getItemById(@PathVariable Long id) {
        InventoryItemResponse response = inventoryService.getItemById(id);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/items")
    public ResponseEntity<ApiResponse<PageResponse<InventoryItemResponse>>> getAllItems(SearchCriteria criteria) {
        PageResponse<InventoryItemResponse> response = inventoryService.searchItemsPaged(criteria.getSearch(), criteria.toPageRequest());
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/items/search")
    public ResponseEntity<ApiResponse<List<InventoryItemResponse>>> searchItems(@RequestParam String q) {
        List<InventoryItemResponse> response = inventoryService.searchItems(q);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/items/below-reorder")
    public ResponseEntity<ApiResponse<List<InventoryItemResponse>>> getBelowReorderPoint() {
        List<InventoryItemResponse> response = inventoryService.getBelowReorderPoint();
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/items/below-minimum")
    public ResponseEntity<ApiResponse<List<InventoryItemResponse>>> getBelowMinimumStock() {
        List<InventoryItemResponse> response = inventoryService.getBelowMinimumStock();
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @PostMapping("/movements")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY')")
    public ResponseEntity<ApiResponse<InventoryMovementResponse>> recordMovement(
            @Valid @RequestBody InventoryMovementRequest request,
            Authentication auth) {
        InventoryMovementResponse response = inventoryService.recordMovement(request, auth.getName());
        return ResponseEntity.ok(ApiResponse.ok(response, "Movimiento registrado"));
    }

    @GetMapping("/movements/item/{itemId}")
    public ResponseEntity<ApiResponse<List<InventoryMovementResponse>>> getItemMovements(@PathVariable Long itemId) {
        List<InventoryMovementResponse> response = inventoryService.getItemMovements(itemId);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/movements")
    public ResponseEntity<ApiResponse<PageResponse<InventoryMovementResponse>>> getAllMovements(
            SearchCriteria criteria) {
        PageResponse<InventoryMovementResponse> response = inventoryService.getAllMovements(criteria);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<String>>> getCategories() {
        List<String> categories = java.util.Arrays.stream(com.casahogar.mantenimiento.inventory.entity.InventoryItemCategory.values())
                .map(Enum::name)
                .toList();
        return ResponseEntity.ok(ApiResponse.ok(categories));
    }

    @GetMapping("/warehouses")
    public ResponseEntity<ApiResponse<List<String>>> getWarehouses() {
        List<InventoryItemResponse> items = inventoryService.getAllItems();
        List<String> warehouses = items.stream()
                .map(InventoryItemResponse::getLocationName)
                .filter(w -> w != null && !w.isBlank())
                .distinct()
                .sorted()
                .toList();
        return ResponseEntity.ok(ApiResponse.ok(warehouses));
    }
}
