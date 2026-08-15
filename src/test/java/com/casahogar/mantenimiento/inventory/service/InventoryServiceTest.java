package com.casahogar.mantenimiento.inventory.service;

import com.casahogar.mantenimiento.auth.entity.User;
import com.casahogar.mantenimiento.auth.repository.UserRepository;
import com.casahogar.mantenimiento.assets.repository.LocationRepository;
import com.casahogar.mantenimiento.inventory.dto.InventoryMovementRequest;
import com.casahogar.mantenimiento.inventory.entity.InventoryItem;
import com.casahogar.mantenimiento.inventory.repository.InventoryItemRepository;
import com.casahogar.mantenimiento.inventory.repository.InventoryMovementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private InventoryItemRepository itemRepository;
    @Mock
    private InventoryMovementRepository movementRepository;
    @Mock
    private LocationRepository locationRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private InventoryService inventoryService;

    private InventoryItem item;

    @BeforeEach
    void setUp() {
        item = new InventoryItem();
        item.setId(1L);
        item.setCurrentStock(new BigDecimal("10"));
        item.setUnitCost(new BigDecimal("5"));
    }

    private InventoryMovementRequest request(String type, String qty) {
        InventoryMovementRequest req = new InventoryMovementRequest();
        req.setInventoryItemId(1L);
        req.setMovementType(type);
        req.setQuantity(new BigDecimal(qty));
        req.setUnitCost(new BigDecimal("5"));
        return req;
    }

    private User user() {
        User u = new User();
        u.setId(1L);
        u.setUsername("admin");
        u.setFirstName("Admin");
        u.setLastName("Sistema");
        return u;
    }

    @Test
    void recordMovement_IN_addsToStock() {
        when(itemRepository.findByIdActive(1L)).thenReturn(Optional.of(item));
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user()));

        inventoryService.recordMovement(request("IN", "5"), "admin");

        assertEquals(0, new BigDecimal("15").compareTo(item.getCurrentStock()));
        verify(itemRepository).save(item);
        verify(movementRepository).save(any());
    }

    @Test
    void recordMovement_OUT_subtractsFromStock() {
        when(itemRepository.findByIdActive(1L)).thenReturn(Optional.of(item));
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user()));

        inventoryService.recordMovement(request("OUT", "3"), "admin");

        assertEquals(0, new BigDecimal("7").compareTo(item.getCurrentStock()));
    }

    @Test
    void recordMovement_OUT_insufficientStock_throws() {
        when(itemRepository.findByIdActive(1L)).thenReturn(Optional.of(item));
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user()));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> inventoryService.recordMovement(request("OUT", "99"), "admin"));

        assertEquals(true, ex.getMessage().contains("Stock insuficiente"));
        verify(itemRepository, never()).save(item);
        verify(movementRepository, never()).save(any());
    }

    @Test
    void recordMovement_ADJUSTMENT_setsStock() {
        when(itemRepository.findByIdActive(1L)).thenReturn(Optional.of(item));
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user()));

        inventoryService.recordMovement(request("ADJUSTMENT", "42"), "admin");

        assertEquals(0, new BigDecimal("42").compareTo(item.getCurrentStock()));
    }
}
