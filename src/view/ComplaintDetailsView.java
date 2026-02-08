package view;

import controller.ComplaintController;
import controller.ComplaintController.ComplaintDetails;
import model.Response;
import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

/**
 * View: Complaint Details
 * Shows detailed information about a specific complaint
 */
public class ComplaintDetailsView extends JFrame {
    private ComplaintController controller;
    private String complaintId;
    private JFrame parentFrame;
    
    private JLabel idLabel;
    private JLabel stallLabel;
    private JLabel canteenLabel;
    private JLabel dateLabel;
    private JLabel problemTypeLabel;
    private JLabel statusLabel;
    private JTextArea detailsArea;
    private JTextArea responsesArea;
    private JButton respondButton;
    private JButton backButton;
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    public ComplaintDetailsView(String complaintId, JFrame parentFrame) {
        this.controller = new ComplaintController();
        this.complaintId = complaintId;
        this.parentFrame = parentFrame;
        initializeUI();
        loadComplaintDetails();
    }
    
    private void initializeUI() {
        setTitle("Complaint Details");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setSize(700, 700);
        
        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(52, 152, 219));
        JLabel titleLabel = new JLabel("Complaint Details");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        
        // Details Panel
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create labels
        idLabel = createInfoLabel("Complaint ID: ");
        stallLabel = createInfoLabel("Stall: ");
        canteenLabel = createInfoLabel("Canteen: ");
        dateLabel = createInfoLabel("Date: ");
        problemTypeLabel = createInfoLabel("Problem Type: ");
        statusLabel = createInfoLabel("Status: ");
        
        detailsPanel.add(idLabel);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        detailsPanel.add(stallLabel);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        detailsPanel.add(canteenLabel);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        detailsPanel.add(dateLabel);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        detailsPanel.add(problemTypeLabel);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        detailsPanel.add(statusLabel);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Details area
        JLabel detailsLabel = new JLabel("Details:");
        detailsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        detailsPanel.add(detailsLabel);
        
        detailsArea = new JTextArea(4, 40);
        detailsArea.setEditable(false);
        detailsArea.setLineWrap(true);
        detailsArea.setWrapStyleWord(true);
        detailsArea.setFont(new Font("Arial", Font.PLAIN, 13));
        JScrollPane detailsScroll = new JScrollPane(detailsArea);
        detailsPanel.add(detailsScroll);
        
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Responses area
        JLabel responsesLabel = new JLabel("Responses:");
        responsesLabel.setFont(new Font("Arial", Font.BOLD, 14));
        detailsPanel.add(responsesLabel);
        
        responsesArea = new JTextArea(8, 40);
        responsesArea.setEditable(false);
        responsesArea.setLineWrap(true);
        responsesArea.setWrapStyleWord(true);
        responsesArea.setFont(new Font("Arial", Font.PLAIN, 13));
        JScrollPane responsesScroll = new JScrollPane(responsesArea);
        detailsPanel.add(responsesScroll);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        respondButton = new JButton("Add Response");
        respondButton.setFont(new Font("Arial", Font.BOLD, 14));
        respondButton.setPreferredSize(new Dimension(150, 40));
        respondButton.setBackground(new Color(46, 204, 113));
        respondButton.addActionListener(e -> addResponse());
        
        backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setPreferredSize(new Dimension(150, 40));
        backButton.addActionListener(e -> goBack());
        
        buttonPanel.add(respondButton);
        buttonPanel.add(backButton);
        
        // Add components
        add(titlePanel, BorderLayout.NORTH);
        add(detailsPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        setLocationRelativeTo(null);
    }
    
    private JLabel createInfoLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        return label;
    }
    
    private void loadComplaintDetails() {
        ComplaintDetails details = controller.getComplaintDetails(complaintId);
        
        if (details == null) {
            JOptionPane.showMessageDialog(this,
                "Complaint not found!",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            goBack();
            return;
        }
        
        idLabel.setText("Complaint ID: " + details.getComplaint().getComplaintId());
        stallLabel.setText("Stall: " + (details.getStall() != null ? details.getStall().getStallName() : "Unknown"));
        canteenLabel.setText("Canteen: " + (details.getCanteen() != null ? details.getCanteen().getCanteenName() : "Unknown"));
        dateLabel.setText("Date: " + details.getComplaint().getComplaintDate().format(DATE_FORMATTER));
        problemTypeLabel.setText("Problem Type: " + details.getComplaint().getProblemType());
        
        String status = details.getComplaint().getStatus();
        statusLabel.setText("Status: " + status);
        if (status.equals("Pending")) {
            statusLabel.setForeground(new Color(230, 126, 34));
        } else {
            statusLabel.setForeground(new Color(39, 174, 96));
        }
        
        detailsArea.setText(details.getComplaint().getDetails());
        
        // Load responses
        StringBuilder responsesText = new StringBuilder();
        if (details.getResponses().isEmpty()) {
            responsesText.append("No responses yet.");
        } else {
            for (Response response : details.getResponses()) {
                responsesText.append("Date: ").append(response.getResponseDate().format(DATE_FORMATTER))
                           .append("\n")
                           .append(response.getResponseMessage())
                           .append("\n\n");
            }
        }
        responsesArea.setText(responsesText.toString());
    }
    
    private void addResponse() {
        ResponseFormView responseView = new ResponseFormView(complaintId, this);
        responseView.setVisible(true);
        this.setVisible(false);
    }
    
    private void goBack() {
        if (parentFrame != null) {
            parentFrame.setVisible(true);
        }
        this.dispose();
    }
}
