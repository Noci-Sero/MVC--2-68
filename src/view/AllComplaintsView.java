package view;

import controller.ComplaintController;
import model.Complaint;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * View: All Complaints List
 * Displays all complaints sorted by date
 */
public class AllComplaintsView extends JFrame {
    private ComplaintController controller;
    private JTable complaintsTable;
    private DefaultTableModel tableModel;
    private JButton viewDetailsButton;
    private JButton viewStallsButton;
    private JButton refreshButton;
    
    public AllComplaintsView() {
        this.controller = new ComplaintController();
        initializeUI();
        loadComplaints();
    }
    
    private void initializeUI() {
        setTitle("Food Complaint Tracking System - All Complaints");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setSize(900, 600);
        
        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(41, 128, 185));
        JLabel titleLabel = new JLabel("All Complaints");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        
        // Table
        String[] columns = {"ID", "Stall ID", "Date", "Problem Type", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        complaintsTable = new JTable(tableModel);
        complaintsTable.setFont(new Font("Arial", Font.PLAIN, 14));
        complaintsTable.setRowHeight(25);
        complaintsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        complaintsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(complaintsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        viewDetailsButton = new JButton("View Details");
        viewDetailsButton.setFont(new Font("Arial", Font.BOLD, 14));
        viewDetailsButton.setPreferredSize(new Dimension(150, 40));
        viewDetailsButton.addActionListener(e -> viewDetails());
        
        viewStallsButton = new JButton("View Stalls");
        viewStallsButton.setFont(new Font("Arial", Font.BOLD, 14));
        viewStallsButton.setPreferredSize(new Dimension(150, 40));
        viewStallsButton.addActionListener(e -> viewStalls());
        
        refreshButton = new JButton("Refresh");
        refreshButton.setFont(new Font("Arial", Font.BOLD, 14));
        refreshButton.setPreferredSize(new Dimension(150, 40));
        refreshButton.addActionListener(e -> loadComplaints());
        
        buttonPanel.add(viewDetailsButton);
        buttonPanel.add(viewStallsButton);
        buttonPanel.add(refreshButton);
        
        // Add components
        add(titlePanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        setLocationRelativeTo(null);
    }
    
    private void loadComplaints() {
        tableModel.setRowCount(0);
        List<Complaint> complaints = controller.getAllComplaints();
        
        for (Complaint complaint : complaints) {
            Object[] row = {
                complaint.getComplaintId(),
                complaint.getStallId(),
                complaint.getComplaintDate(),
                complaint.getProblemType(),
                complaint.getStatus()
            };
            tableModel.addRow(row);
        }
    }
    
    private void viewDetails() {
        int selectedRow = complaintsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a complaint to view details.",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String complaintId = (String) tableModel.getValueAt(selectedRow, 0);
        ComplaintDetailsView detailsView = new ComplaintDetailsView(complaintId, this);
        detailsView.setVisible(true);
        this.setVisible(false);
    }
    
    private void viewStalls() {
        StallsView stallsView = new StallsView(this);
        stallsView.setVisible(true);
        this.setVisible(false);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new AllComplaintsView().setVisible(true);
        });
    }
}
