package com.casahogar.mantenimiento.inventory.service;

import com.casahogar.mantenimiento.auth.entity.User;
import com.casahogar.mantenimiento.auth.repository.UserRepository;
import com.casahogar.mantenimiento.common.dto.PageResponse;
import com.casahogar.mantenimiento.common.dto.SearchCriteria;
import com.casahogar.mantenimiento.inventory.dto.InventoryItemRequest;
import com.casahogar.mantenimiento.inventory.dto.InventoryItemResponse;
import com.casahogar.mantenimiento.inventory.dto.InventoryMovementRequest;
import com.casahogar.mantenimiento.inventory.dto.InventoryMovementResponse;
import com.casahogar.mantenimiento.inventory.entity.InventoryItem;
import com.casahogar.mantenimiento.inventory.entity.InventoryMovement;
import com.casahogar.mantenimiento.inventory.repository.InventoryItemRepository;
import com.casahogar.mantenimiento.inventory.repository.InventoryMovementRepository;
import com.casahogar.mantenimiento.assets.repository.LocationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    private final InventoryItemRepository itemRepository;
    private final InventoryMovementRepository movementRepository;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;

    public InventoryService(InventoryItemRepository itemRepository,
                            InventoryMovementRepository movementRepository,
                            LocationRepository locationRepository,
                            UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.movementRepository = movementRepository;
        this.locationRepository = locationRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public InventoryItemResponse createItem(InventoryItemRequest request, String currentUser) {
        if (itemRepository.existsByCode(request.getCode())) {
            throw new IllegalArgumentException("Ya existe un item con el código: " + request.getCode());
        }

        if (!locationRepository.existsByIdActive(request.getLocationId())) {
            throw new IllegalArgumentException("Ubicación no encontrada");
        }

        InventoryItem item = new InventoryItem();
        item.setCode(request.getCode());
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setCategory(request.getCategory());
        item.setUnitOfMeasure(request.getUnitOfMeasure());
        item.setCurrentStock(request.getCurrentStock());
        item.setMinimumStock(request.getMinimumStock());
        item.setMaximumStock(request.getMaximumStock());
        item.setReorderPoint(request.getReorderPoint());
        item.setUnitCost(request.getUnitCost());
        item.setLocationId(request.getLocationId());
        item.setSupplierName(request.getSupplierName());
        item.setSupplierContact(request.getSupplierContact());
        item.setSupplierSku(request.getSupplierSku());
        item.setIsActive(true);

        item = itemRepository.save(item);
        return InventoryItemResponse.of(item);
    }

    @Transactional
    public InventoryItemResponse updateItem(Long id, InventoryItemRequest request, String currentUser) {
        InventoryItem item = itemRepository.findByIdActive(id)
                .orElseThrow(() -> new IllegalArgumentException("Item de inventario no encontrado"));

        if (!locationRepository.existsByIdActive(request.getLocationId())) {
            throw new IllegalArgumentException("Ubicación no encontrada");
        }

        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setCategory(request.getCategory());
        item.setUnitOfMeasure(request.getUnitOfMeasure());
        item.setCurrentStock(request.getCurrentStock());
        item.setMinimumStock(request.getMinimumStock());
        item.setMaximumStock(request.getMaximumStock());
        item.setReorderPoint(request.getReorderPoint());
        item.setUnitCost(request.getUnitCost());
        item.setLocationId(request.getLocationId());
        item.setSupplierName(request.getSupplierName());
        item.setSupplierContact(request.getSupplierContact());
        item.setSupplierSku(request.getSupplierSku());

        item = itemRepository.save(item);
        return InventoryItemResponse.of(item);
    }

    @Transactional
    public void deleteItem(Long id, String currentUser) {
        itemRepository.softDeleteById(id, currentUser);
    }

    @Transactional(readOnly = true)
    public InventoryItemResponse getItemById(Long id) {
        InventoryItem item = itemRepository.findByIdActive(id)
                .orElseThrow(() -> new IllegalArgumentException("Item de inventario no encontrado"));
        return InventoryItemResponse.of(item);
    }

    @Transactional(readOnly = true)
    public List<InventoryItemResponse> getAllItems() {
        return itemRepository.findAllActive().stream()
                .map(InventoryItemResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InventoryItemResponse> searchItems(String query) {
        return itemRepository.search(query).stream()
                .map(InventoryItemResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PageResponse<InventoryItemResponse> searchItemsPaged(String search, Pageable pageable) {
        if (search != null && !search.isBlank()) {
            return PageResponse.of(itemRepository.searchPaged(search, pageable).map(InventoryItemResponse::of));
        }
        return PageResponse.of(itemRepository.findAllActivePaged(pageable).map(InventoryItemResponse::of));
    }

    @Transactional(readOnly = true)
    public List<InventoryItemResponse> getBelowReorderPoint() {
        return itemRepository.findBelowReorderPoint().stream()
                .map(InventoryItemResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InventoryItemResponse> getBelowMinimumStock() {
        return itemRepository.findBelowMinimumStock().stream()
                .map(InventoryItemResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public InventoryMovementResponse recordMovement(InventoryMovementRequest request, String currentUser) {
        InventoryItem item = itemRepository.findByIdActive(request.getInventoryItemId())
                .orElseThrow(() -> new IllegalArgumentException("Item de inventario no encontrado"));

        User user = userRepository.findByUsername(currentUser)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        InventoryMovement movement = new InventoryMovement();
        movement.setInventoryItemId(request.getInventoryItemId());
        movement.setMovementType(InventoryMovement.MovementType.valueOf(request.getMovementType()));
        movement.setQuantity(request.getQuantity());
        movement.setUnitCost(request.getUnitCost());
        movement.setTotalCost(request.getUnitCost() != null
                ? request.getUnitCost().multiply(request.getQuantity())
                : null);
        movement.setReferenceType(request.getReferenceType());
        movement.setReferenceId(request.getReferenceId());
        movement.setNotes(request.getNotes());
        movement.setPerformedById(user.getId());
        movement.setPerformedByName(user.getFullName());
        movement.setMovementDate(LocalDateTime.now());
        movement.setCreatedBy(currentUser);

        switch (movement.getMovementType()) {
            case IN:
                item.setCurrentStock(item.getCurrentStock().add(request.getQuantity()));
                break;
            case OUT:
                if (item.getCurrentStock().compareTo(request.getQuantity()) < 0) {
                    throw new IllegalArgumentException("Stock insuficiente. Stock actual: " + item.getCurrentStock());
                }
                item.setCurrentStock(item.getCurrentStock().subtract(request.getQuantity()));
                break;
            case ADJUSTMENT:
                item.setCurrentStock(request.getQuantity());
                break;
            default:
                break;
        }

        if (request.getUnitCost() != null) {
            item.setUnitCost(request.getUnitCost());
        }
        if (movement.getMovementType() == InventoryMovement.MovementType.IN) {
            item.setLastPurchaseDate(LocalDateTime.now().toLocalDate());
            if (request.getUnitCost() != null) {
                item.setLastPurchaseCost(request.getUnitCost());
            }
        }

        itemRepository.save(item);
        movementRepository.save(movement);
        return InventoryMovementResponse.of(movement);
    }

    @Transactional(readOnly = true)
    public List<InventoryMovementResponse> getItemMovements(Long itemId) {
        return movementRepository.findByInventoryItemId(itemId).stream()
                .map(InventoryMovementResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PageResponse<InventoryMovementResponse> getAllMovements(SearchCriteria criteria) {
        Pageable pageable = criteria.toPageRequest();
        Page<InventoryMovement> page = movementRepository.findAllByDeletedFalse(pageable);
        return PageResponse.of(page.map(InventoryMovementResponse::of));
    }
}
