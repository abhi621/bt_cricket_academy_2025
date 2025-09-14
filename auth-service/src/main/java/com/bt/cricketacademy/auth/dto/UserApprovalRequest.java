package com.bt.cricketacademy.auth.dto;

public class UserApprovalRequest {
    private boolean approved;
    private boolean active;

    // constructors
    public UserApprovalRequest() {}

    public UserApprovalRequest(boolean approved, boolean active) {
        this.approved = approved;
        this.active = active;
    }

    // getters and setters
    public boolean isApproved() { return approved; }
    public void setApproved(boolean approved) { this.approved = approved; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}