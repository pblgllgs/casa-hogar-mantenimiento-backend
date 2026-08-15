package com.casahogar.mantenimiento.maintenance.repository;

import com.casahogar.mantenimiento.common.repository.BaseRepository;
import com.casahogar.mantenimiento.maintenance.entity.WorkOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkOrderRepository extends BaseRepository<WorkOrder, Long> {

    Optional<WorkOrder> findByOrderNumber(String orderNumber);

    @Query("SELECT w FROM WorkOrder w WHERE w.status = :status AND w.deleted = false")
    List<WorkOrder> findByStatus(@Param("status") String status);

    @Query("SELECT w FROM WorkOrder w WHERE w.assignedToId = :userId AND w.deleted = false ORDER BY w.scheduledStartDate ASC")
    List<WorkOrder> findByAssignedToId(@Param("userId") Long userId);

    @Query("SELECT w FROM WorkOrder w WHERE w.requestedById = :userId AND w.deleted = false ORDER BY w.createdAt DESC")
    List<WorkOrder> findByRequestedById(@Param("userId") Long userId);

    @Query("SELECT w FROM WorkOrder w WHERE w.assetId = :assetId AND w.deleted = false ORDER BY w.scheduledStartDate DESC")
    List<WorkOrder> findByAssetId(@Param("assetId") Long assetId);

    @Query("SELECT w FROM WorkOrder w WHERE w.locationId = :locationId AND w.deleted = false ORDER BY w.scheduledStartDate ASC")
    List<WorkOrder> findByLocationId(@Param("locationId") Long locationId);

    @Query("SELECT w FROM WorkOrder w WHERE w.scheduledStartDate BETWEEN :start AND :end AND w.deleted = false")
    List<WorkOrder> findByScheduledDateRange(@Param("start") LocalDate start, @Param("end") LocalDate end);

    Page<WorkOrder> findAllByDeletedFalse(Pageable pageable);

    @Query("SELECT w FROM WorkOrder w WHERE (w.orderNumber LIKE %:search% OR w.title LIKE %:search%) AND w.deleted = false")
    Page<WorkOrder> search(@Param("search") String search, Pageable pageable);

    @Query("SELECT COUNT(w) FROM WorkOrder w WHERE w.status = :status AND w.deleted = false")
    long countByStatusAndDeletedFalse(@Param("status") WorkOrder.WorkOrderStatus status);

    @Query("SELECT COUNT(w) FROM WorkOrder w WHERE w.priority = :priority AND w.deleted = false")
    long countByPriorityAndDeletedFalse(@Param("priority") WorkOrder.Priority priority);

    @Query("SELECT COUNT(w) FROM WorkOrder w WHERE w.assignedToId = :userId AND w.status IN ('ASSIGNED','IN_PROGRESS') AND w.deleted = false")
    long countActiveByAssignedTo(@Param("userId") Long userId);
}