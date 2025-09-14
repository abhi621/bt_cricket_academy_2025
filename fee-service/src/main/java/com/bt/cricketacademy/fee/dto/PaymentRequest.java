package com.bt.cricketacademy.fee.dto;

public class PaymentRequest {
    private String paymentMethod; // CASH, ONLINE, CHEQUE
    private String transactionId;
    private String chequeNumber;
    
    // Default constructor
    public PaymentRequest() {}
    
    // Constructor
    public PaymentRequest(String paymentMethod, String transactionId) {
        this.paymentMethod = paymentMethod;
        this.transactionId = transactionId;
    }
    
    // Getters and setters
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    
    public String getChequeNumber() { return chequeNumber; }
    public void setChequeNumber(String chequeNumber) { this.chequeNumber = chequeNumber; }
}