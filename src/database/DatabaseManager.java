package database;

import model.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map.Entry;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Database Manager - Handles CSV file operations
 * Acts as the data access layer for the MVC pattern
 */
public class DatabaseManager {
    private static final String DATA_DIR = "data/";
    private static final String CANTEENS_FILE = DATA_DIR + "canteens.csv";
    private static final String STALLS_FILE = DATA_DIR + "stalls.csv";
    private static final String COMPLAINTS_FILE = DATA_DIR + "complaints.csv";
    private static final String RESPONSES_FILE = DATA_DIR + "responses.csv";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public DatabaseManager() {
        initializeDataDirectory();
    }

    private void initializeDataDirectory() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        Map<String, String> fileData = new LinkedHashMap<>();
        fileData.put(CANTEENS_FILE, "canteen_id,canteen_name,location");
        fileData.put(STALLS_FILE, "complaint_id,stall_id,complaint_date,problem_type,details,status");
        fileData.put(COMPLAINTS_FILE, "complaint_id,response_date,response_message");
        fileData.put(RESPONSES_FILE, "stall_id,stall_name,canteen_id");
        for (Entry<String, String> fileWHeader : fileData.entrySet()) {
            String filePath = fileWHeader.getKey();
            String fileHeader = fileWHeader.getValue();
            Path path = Paths.get(fileWHeader.getKey());

            if (Files.exists(path)) {
                System.out.println("File '" + filePath + "' is already existed, Proceed to append data.");
            } else {
                System.out.println(
                        "File '" + filePath + "' does not exist, Proceed to create new one and append header.");

                try {
                    Files.write(path, fileHeader.getBytes(), StandardOpenOption.CREATE);
                    System.out.println("Successfully create file and append its header.");

                } catch (IOException e) {
                    System.err.println("An error occurred while writing to the file: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }

    }

    // ==================== CANTEEN OPERATIONS ====================

    public List<Canteen> getAllCanteens() {
        List<Canteen> canteens = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(CANTEENS_FILE))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3) {
                    canteens.add(new Canteen(data[0].trim(), data[1].trim(), data[2].trim()));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading canteens: " + e.getMessage());
        }
        return canteens;
    }

    public Canteen getCanteenById(String canteenId) {
        List<Canteen> canteens = getAllCanteens();
        for (Canteen c : canteens) {
            if (c.getCanteenId().equals(canteenId)) {
                return c;
            }
        }
        return null;
    }

    // ==================== STALL OPERATIONS ====================

    public List<Stall> getAllStalls() {
        List<Stall> stalls = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(STALLS_FILE))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3) {
                    stalls.add(new Stall(data[0].trim(), data[1].trim(), data[2].trim()));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading stalls: " + e.getMessage());
        }
        return stalls;
    }

    public Stall getStallById(String stallId) {
        List<Stall> stalls = getAllStalls();
        for (Stall s : stalls) {
            if (s.getStallId().equals(stallId)) {
                return s;
            }
        }
        return null;
    }

    public List<Stall> getStallsByCanteenId(String canteenId) {
        List<Stall> result = new ArrayList<>();
        List<Stall> allStalls = getAllStalls();
        for (Stall s : allStalls) {
            if (s.getCanteenId().equals(canteenId)) {
                result.add(s);
            }
        }
        return result;
    }

    // ==================== COMPLAINT OPERATIONS ====================

    public List<Complaint> getAllComplaints() {
        List<Complaint> complaints = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(COMPLAINTS_FILE))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", -1);
                if (data.length >= 6) {
                    Complaint complaint = new Complaint(
                            data[0].trim(),
                            data[1].trim(),
                            LocalDate.parse(data[2].trim(), DATE_FORMATTER),
                            data[3].trim(),
                            data[4].trim(),
                            data[5].trim());
                    complaints.add(complaint);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading complaints: " + e.getMessage());
        }

        // Sort by date (newest first)
        complaints.sort((c1, c2) -> c2.getComplaintDate().compareTo(c1.getComplaintDate()));
        return complaints;
    }

    public Complaint getComplaintById(String complaintId) {
        List<Complaint> complaints = getAllComplaints();
        for (Complaint c : complaints) {
            if (c.getComplaintId().equals(complaintId)) {
                return c;
            }
        }
        return null;
    }

    public void updateComplaintStatus(String complaintId, String newStatus) {
        List<Complaint> complaints = getAllComplaints();
        try (PrintWriter pw = new PrintWriter(new FileWriter(COMPLAINTS_FILE))) {
            pw.println("complaint_id,stall_id,complaint_date,problem_type,details,status");
            for (Complaint c : complaints) {
                if (c.getComplaintId().equals(complaintId)) {
                    c.setStatus(newStatus);
                }
                pw.println(String.format("%s,%s,%s,%s,%s,%s",
                        c.getComplaintId(),
                        c.getStallId(),
                        c.getComplaintDate().format(DATE_FORMATTER),
                        c.getProblemType(),
                        c.getDetails(),
                        c.getStatus()));
            }
        } catch (IOException e) {
            System.err.println("Error updating complaint: " + e.getMessage());
        }
    }

    // ==================== RESPONSE OPERATIONS ====================

    public List<Response> getResponsesByComplaintId(String complaintId) {
        List<Response> responses = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(RESPONSES_FILE))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", -1);
                if (data.length >= 3 && data[0].trim().equals(complaintId)) {
                    responses.add(new Response(
                            data[0].trim(),
                            LocalDate.parse(data[1].trim(), DATE_FORMATTER),
                            data[2].trim()));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading responses: " + e.getMessage());
        }
        return responses;
    }

    public void addResponse(Response response) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(RESPONSES_FILE, true))) {
            pw.println(String.format("%s,%s,%s",
                    response.getComplaintId(),
                    response.getResponseDate().format(DATE_FORMATTER),
                    response.getResponseMessage()));
        } catch (IOException e) {
            System.err.println("Error adding response: " + e.getMessage());
        }
    }

    // ==================== STATISTICS ====================

    public Map<String, Integer> getComplaintCountByStall() {
        Map<String, Integer> countMap = new HashMap<>();
        List<Complaint> complaints = getAllComplaints();
        for (Complaint c : complaints) {
            countMap.put(c.getStallId(), countMap.getOrDefault(c.getStallId(), 0) + 1);
        }
        return countMap;
    }
}
