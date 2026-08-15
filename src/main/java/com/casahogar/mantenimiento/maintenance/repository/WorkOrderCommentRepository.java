package com.casahogar.mantenimiento.maintenance.repository;

import com.casahogar.mantenimiento.maintenance.entity.WorkOrderComment;
import com.casahogar.mantenimiento.common.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkOrderCommentRepository extends BaseRepository<WorkOrderComment, Long> {

    @Query("SELECT c FROM WorkOrderComment c WHERE c.workOrderId = :workOrderId AND c.deleted = false ORDER BY c.createdAt ASC")
    List<WorkOrderComment> findByWorkOrderId(@Param("workOrderId") Long workOrderId);

    Page<WorkOrderComment> findByWorkOrderId(@Param("workOrderId") Long workOrderId, Pageable pageable);

    @Query("SELECT c FROM WorkOrderComment c WHERE c.authorId = :authorId AND c.deleted = false ORDER BY c.createdAt DESC")
    List<WorkOrderComment> findByAuthorId(@Param("authorId") Long authorId);
}