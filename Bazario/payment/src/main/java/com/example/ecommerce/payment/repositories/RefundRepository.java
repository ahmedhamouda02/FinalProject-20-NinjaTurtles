package com.example.ecommerce.payment.repositories;

import com.example.ecommerce.payment.models.Refund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RefundRepository extends JpaRepository<Refund, Long> {

    @Query("SELECT COALESCE(SUM(r.points), 0) FROM Refund r WHERE r.userId = :userId")
    Integer sumPointsByUserId(@Param("userId") Long userId);
}
