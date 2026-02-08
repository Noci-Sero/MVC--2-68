package view;

import controller.ComplaintController;
import controller.ComplaintController.StallStatistics;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * View: Stalls with Complaint Counts
 * Shows all stalls with their complaint counts
 */
public class StallsView extends JFrame {
    private ComplaintController controller;
    private JFrame parentFrame;
    private JTable stallsTable;
    private DefaultTableModel tableModel;
    private JButton backButton;
    
    public StallsView(JFrame parentFrame) {
        this.controller = new ComplaintController();
        this.parentFrame = parentFrame;
        initializeUI();
        loadStallStatistics();
    }
    
    private void initializeUI() {
        setTitle("Stalls - Complaint Statistics");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setSize(800, 600);
        
        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(155, 89, 182));
        JLabel titleLabel = new JLabel("Stalls & Complaint Count");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        
        // Table
        String[] columns = {"Stall ID", "Stall Name", "Canteen", "Complaint Count"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        stallsTable = new JTable(tableModel);
        stallsTable.setFont(new Font("Arial", Font.PLAIN, 14));
        stallsTable.setRowHeight(30);
        stallsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        
        // Custom cell renderer for complaint count
        stallsTable.getColumnModel().getColumn(3).setCellRenderer((table, value, isSelected, hasFocus, row, column) -> {
            JLabel label = new JLabel(value.toString());
            label.setOpaque(true);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 14));
            
            if (isSelected) {
                label.setBackground(table.getSelectionBackground());
                label.setForeground(table.getSelectionForeground());
            } else {
                int count = (Integer) value;
                if (count >= 3) {
                    label.setBackground(new Color(231, 76, 60));
                    label.setForeground(Color.WHITE);
                } else if (count >= 1) {
                    label.setBackground(new Color(241, 196, 15));
                } else {
                    label.setBackground(new Color(46, 204, 113));
                    label.setForeground(Color.WHITE);
                }
            }
            return label;
        });
        
        JScrollPane scrollPane = new JScrollPane(stallsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Info Panel
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        
        JLabel infoLabel = new JLabel("Legend: ");
        infoLabel.setFont(new Font("Arial", Font.BOLD, 12));
        
        JLabel greenLabel = new JLabel(" 0 complaints ");
        greenLabel.setOpaque(true);
        greenLabel.setBackground(new Color(46, 204, 113));
        greenLabel.setForeground(Color.WHITE);
        greenLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        JLabel yellowLabel = new JLabel(" 1-2 complaints ");
        yellowLabel.setOpaque(true);
        yellowLabel.setBackground(new Color(241, 196, 15));
        yellowLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        JLabel redLabel = new JLabel(" 3+ complaints ");
        redLabel.setOpaque(true);
        redLabel.setBackground(new Color(231, 76, 60));
        redLabel.setForeground(Color.WHITE);
        redLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        infoPanel.add(infoLabel);
        infoPanel.add(greenLabel);
        infoPanel.add(yellowLabel);
        infoPanel.add(redLabel);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        backButton = new JButton("Back to Complaints");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setPreferredSize(new Dimension(180, 40));
        backButton.addActionListener(e -> goBack());
        
        buttonPanel.add(backButton);
        
        // Bottom Panel (info + buttons)
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(infoPanel, BorderLayout.NORTH);
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        
        // Add components
        add(titlePanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        
        setLocationRelativeTo(null);
    }
    
    private void loadStallStatistics() {
        tableModel.setRowCount(0);
        List<StallStatistics> statistics = controller.getStallStatistics();
        
        for (StallStatistics stat : statistics) {
            Object[] row = {
                stat.getStall().getStallId(),
                stat.getStall().getStallName(),
                stat.getCanteen() != null ? stat.getCanteen().getCanteenName() : "Unknown",
                stat.getComplaintCount()
            };
            tableModel.addRow(row);
        }
    }
    
    private void goBack() {
        if (parentFrame != null) {
            parentFrame.setVisible(true);
        }
        this.dispose();
    }
}
