package model;

/**
 * Model: Canteen
 * Represents a canteen in the university
 */
public class Canteen {
    private String canteenId;
    private String canteenName;
    private String location;
    
    public Canteen() {}
    
    public Canteen(String canteenId, String canteenName, String location) {
        this.canteenId = canteenId;
        this.canteenName = canteenName;
        this.location = location;
    }
    
    // Getters and Setters
    public String getCanteenId() {
        return canteenId;
    }
    
    public void setCanteenId(String canteenId) {
        this.canteenId = canteenId;
    }
    
    public String getCanteenName() {
        return canteenName;
    }
    
    public void setCanteenName(String canteenName) {
        this.canteenName = canteenName;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    @Override
    public String toString() {
        return canteenName + " (" + location + ")";
    }
}
