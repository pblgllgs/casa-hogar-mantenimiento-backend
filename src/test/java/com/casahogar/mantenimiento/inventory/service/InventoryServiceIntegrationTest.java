package com.casahogar.mantenimiento.inventory.service;

import com.casahogar.mantenimiento.assets.entity.Location;
import com.casahogar.mantenimiento.assets.repository.LocationRepository;
import com.casahogar.mantenimiento.auth.entity.Role;
import com.casahogar.mantenimiento.auth.entity.User;
import com.casahogar.mantenimiento.auth.repository.UserRepository;
import com.casahogar.mantenimiento.inventory.dto.InventoryItemRequest;
import com.casahogar.mantenimiento.inventory.dto.InventoryItemResponse;
import com.casahogar.mantenimiento.inventory.dto.InventoryMovementRequest;
import com.casahogar.mantenimiento.inventory.dto.InventoryMovementResponse;
import com.casahogar.mantenimiento.inventory.entity.InventoryItem;
import com.casahogar.mantenimiento.inventory.entity.InventoryItemCategory;
import com.casahogar.mantenimiento.inventory.repository.InventoryItemRepository;
import com.casahogar.mantenimiento.inventory.repository.InventoryMovementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("integration")
class InventoryServiceIntegrationTest {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private InventoryItemRepository itemRepository;

    @Autowired
    private InventoryMovementRepository movementRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void cleanDatabase() {
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");
        jdbcTemplate.execute("DELETE FROM inventory_movements");
        jdbcTemplate.execute("DELETE FROM inventory_items");
        jdbcTemplate.execute("DELETE FROM locations");
        jdbcTemplate.execute("DELETE FROM user_roles");
        jdbcTemplate.execute("DELETE FROM users");
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");
    }

    private Location createLocation(String code) {
        Location location = new Location();
        location.setCode(code);
        location.setName("Ubicación " + code);
        location.setType(Location.LocationType.STORAGE);
        location.setIsActive(true);
        return locationRepository.save(location);
    }

    private User createUser(String username) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(username + "@test.cl");
        user.setPassword("hash");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setRoles(new java.util.HashSet<>(Set.of(Role.ADMIN)));
        return userRepository.save(user);
    }

    private InventoryItemRequest itemRequest(String code, Long locationId, BigDecimal stock) {
        InventoryItemRequest request = new InventoryItemRequest();
        request.setCode(code);
        request.setName("Item " + code);
        request.setCategory(InventoryItemCategory.LIMPIEZA);
        request.setUnitOfMeasure("un");
        request.setCurrentStock(stock);
        request.setMinimumStock(BigDecimal.ZERO);
        request.setLocationId(locationId);
        return request;
    }

    private InventoryMovementRequest movementRequest(Long itemId, String type, BigDecimal quantity) {
        InventoryMovementRequest request = new InventoryMovementRequest();
        request.setInventoryItemId(itemId);
        request.setMovementType(type);
        request.setQuantity(quantity);
        return request;
    }

    @Test
    void createItem_persistsAndReturnsId() {
        Location location = createLocation("BODEGA");

        InventoryItemResponse response = inventoryService.createItem(
                itemRequest("CLN-001", location.getId(), new BigDecimal("10")), "admin");

        assertThat(response.getId()).isNotNull();
        assertThat(response.getCode()).isEqualTo("CLN-001");
        assertThat(response.getCurrentStock()).isEqualByComparingTo("10");

        InventoryItem saved = itemRepository.findByCode("CLN-001").orElseThrow();
        assertThat(saved.getLocationId()).isEqualTo(location.getId());
        assertThat(saved.getIsActive()).isTrue();
        assertThat(saved.getDeleted()).isFalse();
    }

    @Test
    void createItem_duplicateCode_rejects() {
        Location location = createLocation("BODEGA");
        inventoryService.createItem(itemRequest("CLN-001", location.getId(), BigDecimal.ZERO), "admin");

        assertThatThrownBy(() -> inventoryService.createItem(
                itemRequest("CLN-001", location.getId(), BigDecimal.ONE), "admin"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Ya existe un item");
    }

    @Test
    void createItem_missingLocation_rejects() {
        assertThatThrownBy(() -> inventoryService.createItem(
                itemRequest("CLN-002", 99999L, BigDecimal.ZERO), "admin"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Ubicación no encontrada");
    }

    @Test
    void recordMovement_in_increasesStock() {
        Location location = createLocation("BODEGA");
        User user = createUser("inventario");
        Long itemId = inventoryService.createItem(
                itemRequest("CLN-003", location.getId(), BigDecimal.ZERO), "inventario").getId();

        InventoryMovementResponse movement = inventoryService.recordMovement(
                movementRequest(itemId, "IN", new BigDecimal("15")), user.getUsername());

        assertThat(movement.getMovementType()).isEqualTo("IN");
        assertThat(movement.getQuantity()).isEqualByComparingTo("15");
        assertThat(movement.getPerformedByName()).isEqualTo("Test User");
        assertThat(itemRepository.findByIdActive(itemId).orElseThrow().getCurrentStock())
                .isEqualByComparingTo("15");
        assertThat(movementRepository.findByInventoryItemId(itemId)).hasSize(1);
    }

    @Test
    void recordMovement_out_decreasesStock() {
        Location location = createLocation("BODEGA");
        User user = createUser("inventario");
        Long itemId = inventoryService.createItem(
                itemRequest("CLN-004", location.getId(), new BigDecimal("20")), "inventario").getId();

        inventoryService.recordMovement(movementRequest(itemId, "OUT", new BigDecimal("8")), user.getUsername());

        assertThat(itemRepository.findByIdActive(itemId).orElseThrow().getCurrentStock())
                .isEqualByComparingTo("12");
    }

    @Test
    void recordMovement_out_insufficientStock_rejectsWithoutChanges() {
        Location location = createLocation("BODEGA");
        User user = createUser("inventario");
        Long itemId = inventoryService.createItem(
                itemRequest("CLN-005", location.getId(), new BigDecimal("5")), "inventario").getId();

        assertThatThrownBy(() -> inventoryService.recordMovement(
                movementRequest(itemId, "OUT", new BigDecimal("10")), user.getUsername()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Stock insuficiente");

        assertThat(itemRepository.findByIdActive(itemId).orElseThrow().getCurrentStock())
                .isEqualByComparingTo("5");
        assertThat(movementRepository.findByInventoryItemId(itemId)).isEmpty();
    }

    @Test
    void recordMovement_adjustment_setsStock() {
        Location location = createLocation("BODEGA");
        User user = createUser("inventario");
        Long itemId = inventoryService.createItem(
                itemRequest("CLN-006", location.getId(), new BigDecimal("3")), "inventario").getId();

        inventoryService.recordMovement(movementRequest(itemId, "ADJUSTMENT", new BigDecimal("42")), user.getUsername());

        assertThat(itemRepository.findByIdActive(itemId).orElseThrow().getCurrentStock())
                .isEqualByComparingTo("42");
    }

    @Test
    void deleteItem_softDeletes() {
        Location location = createLocation("BODEGA");
        Long itemId = inventoryService.createItem(
                itemRequest("CLN-007", location.getId(), BigDecimal.ZERO), "admin").getId();

        inventoryService.deleteItem(itemId, "admin");

        assertThatThrownBy(() -> inventoryService.getItemById(itemId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Item de inventario no encontrado");
        assertThat(inventoryService.getAllItems()).isEmpty();
        assertThat(itemRepository.findById(itemId).orElseThrow().getDeleted()).isTrue();
    }

    @Test
    void searchItemsPaged_filtersByName() {
        Location location = createLocation("BODEGA");
        inventoryService.createItem(itemRequest("CLN-100", location.getId(), BigDecimal.ZERO), "admin");
        inventoryService.createItem(itemRequest("CLN-200", location.getId(), BigDecimal.ZERO), "admin");

        List<InventoryItemResponse> results = inventoryService.searchItems("Item CLN-200");

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getCode()).isEqualTo("CLN-200");
    }

    @Test
    void getBelowReorderPoint_returnsOnlyLowStock() {
        Location location = createLocation("BODEGA");
        InventoryItemRequest lowRequest = itemRequest("CLN-300", location.getId(), new BigDecimal("5"));
        lowRequest.setReorderPoint(new BigDecimal("10"));
        InventoryItemResponse low = inventoryService.createItem(lowRequest, "admin");
        InventoryItemRequest highRequest = itemRequest("CLN-301", location.getId(), new BigDecimal("50"));
        highRequest.setReorderPoint(new BigDecimal("10"));
        inventoryService.createItem(highRequest, "admin");

        List<InventoryItemResponse> below = inventoryService.getBelowReorderPoint();

        assertThat(below).extracting(InventoryItemResponse::getCode).containsExactly("CLN-300");
    }
}
