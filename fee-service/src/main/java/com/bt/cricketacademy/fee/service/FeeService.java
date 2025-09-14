package com.bt.cricketacademy.fee.service;

import com.bt.cricketacademy.fee.dto.CreateFeeRequest;
import com.bt.cricketacademy.fee.dto.FeeSummaryResponse;
import com.bt.cricketacademy.fee.dto.PaymentRequest;
import com.bt.cricketacademy.fee.model.Fee;
import com.bt.cricketacademy.fee.repository.FeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FeeService {
    
    @Autowired
    private FeeRepository feeRepository;
    
    // Get all fees with optional filtering
    public List<Fee> getAllFees(Long playerId, String status, String playerName) {
        if (playerId != null) {
            return feeRepository.findByPlayerId(playerId);
        } else if (status != null) {
            try {
                Fee.PaymentStatus paymentStatus = Fee.PaymentStatus.valueOf(status.toUpperCase());
                return feeRepository.findByStatus(paymentStatus);
            } catch (IllegalArgumentException e) {
                // Invalid status, return all fees
                return feeRepository.findAll();
            }
        } else if (playerName != null && !playerName.trim().isEmpty()) {
            return feeRepository.findByPlayerNameContainingIgnoreCase(playerName.trim());
        } else {
            return feeRepository.findAll();
        }
    }
    
    // Create a new fee record
    public Fee createFee(CreateFeeRequest request) {
        Fee fee = new Fee();
        fee.setPlayerId(request.getPlayerId());
        fee.setPlayerName(request.getPlayerName());
        fee.setAmount(request.getAmount());
        fee.setDueDate(request.getDueDate());
        fee.setMonth(request.getMonth());
        fee.setStatus(Fee.PaymentStatus.PENDING);
        
        return feeRepository.save(fee);
    }
    
    // Mark fee as paid
    public Fee markAsPaid(Long feeId, PaymentRequest paymentRequest) {
        Optional<Fee> optionalFee = feeRepository.findById(feeId);
        if (optionalFee.isEmpty()) {
            throw new RuntimeException("Fee not found with ID: " + feeId);
        }
        
        Fee fee = optionalFee.get();
        fee.setStatus(Fee.PaymentStatus.PAID);
        fee.setPaidAt(LocalDateTime.now());
        fee.setPaymentMethod(paymentRequest.getPaymentMethod());
        fee.setTransactionId(paymentRequest.getTransactionId() != null ? 
                             paymentRequest.getTransactionId() : 
                             paymentRequest.getChequeNumber());
        
        return feeRepository.save(fee);
    }
    
    // Get fee summary
    public FeeSummaryResponse getFeeSummary() {
        // Get current month for this month's revenue calculation
        String currentMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM yyyy"));
        
        BigDecimal totalRevenue = feeRepository.getTotalRevenue();
        BigDecimal pendingPayments = feeRepository.sumAmountByStatus(Fee.PaymentStatus.PENDING);
        BigDecimal thisMonthRevenue = feeRepository.sumPaidAmountForMonth(currentMonth);
        
        Long totalRecords = feeRepository.count();
        Long paidRecords = feeRepository.countByStatus(Fee.PaymentStatus.PAID);
        Long pendingRecords = feeRepository.countByStatus(Fee.PaymentStatus.PENDING);
        Long overdueRecords = (long) feeRepository.findOverdueFees(LocalDate.now()).size();
        
        return new FeeSummaryResponse(
            totalRevenue,
            pendingPayments,
            thisMonthRevenue,
            totalRecords,
            paidRecords,
            pendingRecords,
            overdueRecords
        );
    }
    
    // Update overdue fees
    @Transactional
    public void updateOverdueFees() {
        List<Fee> overdueFees = feeRepository.findOverdueFees(LocalDate.now());
        for (Fee fee : overdueFees) {
            fee.setStatus(Fee.PaymentStatus.OVERDUE);
        }
        feeRepository.saveAll(overdueFees);
    }
    
    // Get fee by ID
    public Optional<Fee> getFeeById(Long id) {
        return feeRepository.findById(id);
    }
    
    // Delete fee
    public void deleteFee(Long id) {
        if (!feeRepository.existsById(id)) {
            throw new RuntimeException("Fee not found with ID: " + id);
        }
        feeRepository.deleteById(id);
    }
}