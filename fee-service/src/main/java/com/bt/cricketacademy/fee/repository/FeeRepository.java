package com.bt.cricketacademy.fee.repository;

import com.bt.cricketacademy.fee.model.Fee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface FeeRepository extends JpaRepository<Fee, Long> {
    
    // Find fees by player ID
    List<Fee> findByPlayerId(Long playerId);
    
    // Find fees by status
    List<Fee> findByStatus(Fee.PaymentStatus status);
    
    // Find fees by player name (case-insensitive)
    List<Fee> findByPlayerNameContainingIgnoreCase(String playerName);
    
    // Find fees by month
    List<Fee> findByMonth(String month);
    
    // Find overdue fees
    @Query("SELECT f FROM Fee f WHERE f.status = 'PENDING' AND f.dueDate < :currentDate")
    List<Fee> findOverdueFees(@Param("currentDate") LocalDate currentDate);
    
    // Count fees by status
    long countByStatus(Fee.PaymentStatus status);
    
    // Sum amount by status
    @Query("SELECT COALESCE(SUM(f.amount), 0) FROM Fee f WHERE f.status = :status")
    BigDecimal sumAmountByStatus(@Param("status") Fee.PaymentStatus status);
    
    // Sum amount for current month
    @Query("SELECT COALESCE(SUM(f.amount), 0) FROM Fee f WHERE f.status = 'PAID' AND f.month = :month")
    BigDecimal sumPaidAmountForMonth(@Param("month") String month);
    
    // Get total revenue (all paid fees)
    @Query("SELECT COALESCE(SUM(f.amount), 0) FROM Fee f WHERE f.status = 'PAID'")
    BigDecimal getTotalRevenue();
}

