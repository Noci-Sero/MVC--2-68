package model;

import java.time.LocalDate;

/**
 * Model: Complaint
 * Represents a food quality complaint
 */
public class Complaint {
    private String complaintId;
    private String stallId;
    private LocalDate complaintDate;
    private String problemType;
    private String details;
    private String status; // "Pending" or "Resolved"
    
    public Complaint() {}
    
    public Complaint(String complaintId, String stallId, LocalDate complaintDate, 
                     String problemType, String details, String status) {
        this.complaintId = complaintId;
        this.stallId = stallId;
        this.complaintDate = complaintDate;
        this.problemType = problemType;
        this.details = details;
        this.status = status;
    }
    
    // Getters and Setters
    public String getComplaintId() {
        return complaintId;
    }
    
    public void setComplaintId(String complaintId) {
        this.complaintId = complaintId;
    }
    
    public String getStallId() {
        return stallId;
    }
    
    public void setStallId(String stallId) {
        this.stallId = stallId;
    }
    
    public LocalDate getComplaintDate() {
        return complaintDate;
    }
    
    public void setComplaintDate(LocalDate complaintDate) {
        this.complaintDate = complaintDate;
    }
    
    public String getProblemType() {
        return problemType;
    }
    
    public void setProblemType(String problemType) {
        this.problemType = problemType;
    }
    
    public String getDetails() {
        return details;
    }
    
    public void setDetails(String details) {
        this.details = details;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
}
