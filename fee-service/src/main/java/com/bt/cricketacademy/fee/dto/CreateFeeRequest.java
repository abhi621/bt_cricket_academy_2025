package com.bt.cricketacademy.fee.dto;

import java.time.LocalDate;

public class CreateFeeRequest {
    private Long playerId;
    private String playerName;
    private Double amount;
    private LocalDate dueDate;
    private String month;
    
    // Default constructor
    public CreateFeeRequest() {}
    
    // Constructor
    public CreateFeeRequest(Long playerId, String playerName, Double amount, LocalDate dueDate, String month) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.amount = amount;
        this.dueDate = dueDate;
        this.month = month;
    }
    
    // Getters and setters
    public Long getPlayerId() { return playerId; }
    public void setPlayerId(Long playerId) { this.playerId = playerId; }
    
    public String getPlayerName() { return playerName; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }
    
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    
    public String getMonth() { return month; }
    public void setMonth(String month) { this.month = month; }
}