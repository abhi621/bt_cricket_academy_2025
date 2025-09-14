package com.bt.cricketacademy.fee.dto;

import java.math.BigDecimal;

public class FeeSummaryResponse {
    private BigDecimal totalRevenue;
    private BigDecimal pendingPayments;
    private BigDecimal thisMonthRevenue;
    private Long totalFeeRecords;
    private Long paidRecords;
    private Long pendingRecords;
    private Long overdueRecords;
    
    // Default constructor
    public FeeSummaryResponse() {}
    
    // Constructor
    public FeeSummaryResponse(BigDecimal totalRevenue, BigDecimal pendingPayments, 
                             BigDecimal thisMonthRevenue, Long totalFeeRecords, 
                             Long paidRecords, Long pendingRecords, Long overdueRecords) {
        this.totalRevenue = totalRevenue;
        this.pendingPayments = pendingPayments;
        this.thisMonthRevenue = thisMonthRevenue;
        this.totalFeeRecords = totalFeeRecords;
        this.paidRecords = paidRecords;
        this.pendingRecords = pendingRecords;
        this.overdueRecords = overdueRecords;
    }
    
    // Getters and setters
    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }
    
    public BigDecimal getPendingPayments() { return pendingPayments; }
    public void setPendingPayments(BigDecimal pendingPayments) { this.pendingPayments = pendingPayments; }
    
    public BigDecimal getThisMonthRevenue() { return thisMonthRevenue; }
    public void setThisMonthRevenue(BigDecimal thisMonthRevenue) { this.thisMonthRevenue = thisMonthRevenue; }
    
    public Long getTotalFeeRecords() { return totalFeeRecords; }
    public void setTotalFeeRecords(Long totalFeeRecords) { this.totalFeeRecords = totalFeeRecords; }
    
    public Long getPaidRecords() { return paidRecords; }
    public void setPaidRecords(Long paidRecords) { this.paidRecords = paidRecords; }
    
    public Long getPendingRecords() { return pendingRecords; }
    public void setPendingRecords(Long pendingRecords) { this.pendingRecords = pendingRecords; }
    
    public Long getOverdueRecords() { return overdueRecords; }
    public void setOverdueRecords(Long overdueRecords) { this.overdueRecords = overdueRecords; }
}