package model;

/**
 * Model: Stall
 * Represents a food stall in a canteen
 */
public class Stall {
    private String stallId;
    private String stallName;
    private String canteenId;
    
    public Stall() {}
    
    public Stall(String stallId, String stallName, String canteenId) {
        this.stallId = stallId;
        this.stallName = stallName;
        this.canteenId = canteenId;
    }
    
    // Getters and Setters
    public String getStallId() {
        return stallId;
    }
    
    public void setStallId(String stallId) {
        this.stallId = stallId;
    }
    
    public String getStallName() {
        return stallName;
    }
    
    public void setStallName(String stallName) {
        this.stallName = stallName;
    }
    
    public String getCanteenId() {
        return canteenId;
    }
    
    public void setCanteenId(String canteenId) {
        this.canteenId = canteenId;
    }
    
    @Override
    public String toString() {
        return stallName;
    }
}
