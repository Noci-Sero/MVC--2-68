package controller;

import database.DatabaseManager;
import model.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Controller: ComplaintController
 * Handles business logic for complaints and coordinates between Model and View
 */
public class ComplaintController {
    private DatabaseManager dbManager;
    
    public ComplaintController() {
        this.dbManager = new DatabaseManager();
    }
    
    // ==================== COMPLAINT OPERATIONS ====================
    
    /**
     * Get all complaints sorted by date
     */
    public List<Complaint> getAllComplaints() {
        return dbManager.getAllComplaints();
    }
    
    /**
     * Get a specific complaint by ID
     */
    public Complaint getComplaintById(String complaintId) {
        return dbManager.getComplaintById(complaintId);
    }
    
    /**
     * Get complaint details with stall and canteen information
     */
    public ComplaintDetails getComplaintDetails(String complaintId) {
        Complaint complaint = dbManager.getComplaintById(complaintId);
        if (complaint == null) {
            return null;
        }
        
        Stall stall = dbManager.getStallById(complaint.getStallId());
        Canteen canteen = null;
        if (stall != null) {
            canteen = dbManager.getCanteenById(stall.getCanteenId());
        }
        
        List<Response> responses = dbManager.getResponsesByComplaintId(complaintId);
        
        return new ComplaintDetails(complaint, stall, canteen, responses);
    }
    
    // ==================== RESPONSE OPERATIONS ====================
    
    /**
     * Add a response to a complaint and update status
     * Business Rule: When a response is added, status changes to "Resolved"
     */
    public void addResponse(String complaintId, String responseMessage) {
        // Create response
        Response response = new Response(
            complaintId,
            LocalDate.now(),
            responseMessage
        );
        
        // Save response
        dbManager.addResponse(response);
        
        // Update complaint status to "Resolved"
        dbManager.updateComplaintStatus(complaintId, "Resolved");
    }
    
    // ==================== STALL STATISTICS ====================
    
    /**
     * Get stalls with their complaint counts
     */
    public List<StallStatistics> getStallStatistics() {
        List<Stall> stalls = dbManager.getAllStalls();
        Map<String, Integer> complaintCounts = dbManager.getComplaintCountByStall();
        
        List<StallStatistics> statistics = new java.util.ArrayList<>();
        for (Stall stall : stalls) {
            int count = complaintCounts.getOrDefault(stall.getStallId(), 0);
            Canteen canteen = dbManager.getCanteenById(stall.getCanteenId());
            statistics.add(new StallStatistics(stall, canteen, count));
        }
        
        // Sort by complaint count (descending)
        statistics.sort((s1, s2) -> Integer.compare(s2.getComplaintCount(), s1.getComplaintCount()));
        
        return statistics;
    }
    
    // ==================== HELPER CLASSES ====================
    
    /**
     * Data Transfer Object for complaint details
     */
    public static class ComplaintDetails {
        private Complaint complaint;
        private Stall stall;
        private Canteen canteen;
        private List<Response> responses;
        
        public ComplaintDetails(Complaint complaint, Stall stall, Canteen canteen, List<Response> responses) {
            this.complaint = complaint;
            this.stall = stall;
            this.canteen = canteen;
            this.responses = responses;
        }
        
        public Complaint getComplaint() { return complaint; }
        public Stall getStall() { return stall; }
        public Canteen getCanteen() { return canteen; }
        public List<Response> getResponses() { return responses; }
    }
    
    /**
     * Data Transfer Object for stall statistics
     */
    public static class StallStatistics {
        private Stall stall;
        private Canteen canteen;
        private int complaintCount;
        
        public StallStatistics(Stall stall, Canteen canteen, int complaintCount) {
            this.stall = stall;
            this.canteen = canteen;
            this.complaintCount = complaintCount;
        }
        
        public Stall getStall() { return stall; }
        public Canteen getCanteen() { return canteen; }
        public int getComplaintCount() { return complaintCount; }
    }
}
