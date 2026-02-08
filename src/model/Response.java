package model;

import java.time.LocalDate;

/**
 * Model: Response (การตอบกลับ)
 * Represents a response to a complaint
 */
public class Response {
    private String complaintId;
    private LocalDate responseDate;
    private String responseMessage;
    
    public Response() {}
    
    public Response(String complaintId, LocalDate responseDate, String responseMessage) {
        this.complaintId = complaintId;
        this.responseDate = responseDate;
        this.responseMessage = responseMessage;
    }
    
    // Getters and Setters
    public String getComplaintId() {
        return complaintId;
    }
    
    public void setComplaintId(String complaintId) {
        this.complaintId = complaintId;
    }
    
    public LocalDate getResponseDate() {
        return responseDate;
    }
    
    public void setResponseDate(LocalDate responseDate) {
        this.responseDate = responseDate;
    }
    
    public String getResponseMessage() {
        return responseMessage;
    }
    
    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}
