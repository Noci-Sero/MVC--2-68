package view;

import controller.ComplaintController;
import javax.swing.*;
import java.awt.*;

/**
 * View: Response Form
 * Form for adding responses to complaints
 */
public class ResponseFormView extends JFrame {
    private ComplaintController controller;
    private String complaintId;
    private JFrame parentFrame;
    
    private JTextArea responseTextArea;
    private JButton submitButton;
    private JButton cancelButton;
    
    public ResponseFormView(String complaintId, JFrame parentFrame) {
        this.controller = new ComplaintController();
        this.complaintId = complaintId;
        this.parentFrame = parentFrame;
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Add Response");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setSize(600, 400);
        
        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(46, 204, 113));
        JLabel titleLabel = new JLabel("Add Response");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        
        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BorderLayout(10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel instructionLabel = new JLabel("Complaint ID: " + complaintId);
        instructionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(instructionLabel, BorderLayout.NORTH);
        
        JLabel responseLabel = new JLabel("Response Message:");
        responseLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        responseTextArea = new JTextArea(10, 40);
        responseTextArea.setLineWrap(true);
        responseTextArea.setWrapStyleWord(true);
        responseTextArea.setFont(new Font("Arial", Font.PLAIN, 13));
        responseTextArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JScrollPane scrollPane = new JScrollPane(responseTextArea);
        
        JPanel textPanel = new JPanel(new BorderLayout(5, 5));
        textPanel.add(responseLabel, BorderLayout.NORTH);
        textPanel.add(scrollPane, BorderLayout.CENTER);
        
        formPanel.add(textPanel, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        submitButton = new JButton("Submit Response");
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
        submitButton.setPreferredSize(new Dimension(160, 40));
        submitButton.setBackground(new Color(46, 204, 113));
        submitButton.addActionListener(e -> submitResponse());
        
        cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setPreferredSize(new Dimension(160, 40));
        cancelButton.addActionListener(e -> cancel());
        
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);
        
        // Add components
        add(titlePanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        setLocationRelativeTo(null);
    }
    
    private void submitResponse() {
        String responseMessage = responseTextArea.getText().trim();
        
        if (responseMessage.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter a response message.",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // Add response through controller
            // Business Rule: This will also update status to "Resolved"
            controller.addResponse(complaintId, responseMessage);
            
            JOptionPane.showMessageDialog(this,
                "Response added successfully!\nStatus updated to 'Resolved'.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Navigate back properly: ResponseForm -> ComplaintDetails -> AllComplaints
            this.dispose();
            
            if (parentFrame != null && parentFrame instanceof ComplaintDetailsView) {
                // Get the parent of ComplaintDetailsView (which is AllComplaintsView)
                parentFrame.dispose();
                // Show AllComplaintsView
                new AllComplaintsView().setVisible(true);
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error adding response: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void cancel() {
        if (parentFrame != null) {
            parentFrame.setVisible(true);
        }
        this.dispose();
    }
}
