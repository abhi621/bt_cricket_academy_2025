package com.bt.cricketacademy.fee.controller;

import com.bt.cricketacademy.fee.dto.CreateFeeRequest;
import com.bt.cricketacademy.fee.dto.FeeSummaryResponse;
import com.bt.cricketacademy.fee.dto.PaymentRequest;
import com.bt.cricketacademy.fee.model.Fee;
import com.bt.cricketacademy.fee.service.FeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/fees")
@CrossOrigin(origins = "*")
public class FeeController {
    
    @Autowired
    private FeeService feeService;
    
    // Hello endpoint for testing
    @GetMapping("/hello")
    public String hello() {
        return "Hello from Fee Service!";
    }
    
    // Get all fees with optional filtering
    @GetMapping
    public ResponseEntity<List<Fee>> getAllFees(
            @RequestParam(required = false) Long playerId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String playerName) {
        
        try {
            List<Fee> fees = feeService.getAllFees(playerId, status, playerName);
            return ResponseEntity.ok(fees);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Get fee by ID
    @GetMapping("/{id}")
    public ResponseEntity<Fee> getFeeById(@PathVariable Long id) {
        try {
            Optional<Fee> fee = feeService.getFeeById(id);
            return fee.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Create new fee record
    @PostMapping
    public ResponseEntity<?> createFee(@RequestBody CreateFeeRequest request) {
        try {
            Fee createdFee = feeService.createFee(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdFee);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Failed to create fee: " + e.getMessage()));
        }
    }
    
    // Mark fee as paid
    @PatchMapping("/{id}/pay")
    public ResponseEntity<?> markAsPaid(@PathVariable Long id, 
                                       @RequestBody PaymentRequest paymentRequest) {
        try {
            Fee updatedFee = feeService.markAsPaid(id, paymentRequest);
            return ResponseEntity.ok(updatedFee);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to update payment: " + e.getMessage()));
        }
    }
    
    // Get fee summary
    @GetMapping("/summary")
    public ResponseEntity<FeeSummaryResponse> getFeeSummary() {
        try {
            // Update overdue fees before generating summary
            feeService.updateOverdueFees();
            
            FeeSummaryResponse summary = feeService.getFeeSummary();
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Delete fee (ADMIN only)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFee(@PathVariable Long id) {
        try {
            feeService.deleteFee(id);
            return ResponseEntity.ok(Map.of("message", "Fee deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete fee: " + e.getMessage()));
        }
    }
    
    // Get fees for a specific player (can be accessed by the player themselves)
    @GetMapping("/my-fees")
    public ResponseEntity<List<Fee>> getMyFees(@RequestParam Long playerId) {
        try {
            List<Fee> fees = feeService.getAllFees(playerId, null, null);
            return ResponseEntity.ok(fees);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

