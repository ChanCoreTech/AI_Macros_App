package org.aimacrosapp;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

public class GoalHistoryGUI extends JFrame {

    private JPanel panel1;
    private JButton btnBack, btnLogout, btnDelete;

    public GoalHistoryGUI() throws IOException {

        //initialize delete
        btnDelete = new JButton("Delete");

        // === BACK BUTTON ===
        ImageIcon backIcon = new ImageIcon(getClass().getResource("/back_arrow.png"));
        btnBack = new JButton(backIcon);
        btnBack.setBorderPainted(false);
        btnBack.setContentAreaFilled(false);
        btnBack.setFocusPainted(false);
        btnBack.setBounds(10, 10, 50, 50);

        //back button click event
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //go back to Dashboard
                new DashboardGUI();
                //close current page
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(panel1);
                if (topFrame != null) {
                    topFrame.dispose();
                }
            }
        });

        // Log Out
        ImageIcon logoutIcon = new ImageIcon(getClass().getResource("/logout.png"));
        btnLogout = new JButton(logoutIcon);
        btnLogout.setBorderPainted(false);
        btnLogout.setContentAreaFilled(false);

        // === TOP PANEL ===
        JPanel topPanel = new JPanel(null);
        // Top panel for back button (left) and logout button (right)
        topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setPreferredSize(new Dimension(60, 60));

        btnBack.setPreferredSize(new Dimension(50, 50));
        btnLogout.setPreferredSize(new Dimension(100, 50)); // adjust size as needed
        ToolTipManager.sharedInstance().setInitialDelay(100);
        btnBack.setToolTipText("Back");
        btnLogout.setToolTipText("Logout");

        topPanel.add(btnBack, BorderLayout.WEST);    // Puts back button on the far left
        topPanel.add(btnLogout, BorderLayout.EAST);  // Puts logout button on the far right

        // === FONTS ===
        Font headerFont = new Font("Helvetica", Font.BOLD, 22);
        Font mainFont   = new Font("Verdana",  Font.PLAIN, 15);
        Font boldFont   = new Font("Verdana",  Font.BOLD, 15);
        btnDelete.setFont(boldFont);

        // === TABLE SETUP ===
        String[] columnNames = {"Date", "Workout", "Calories", "Carbs", "Protein", "Fats"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override public boolean isCellEditable(int r, int c) { return true; }
        };

        JTable goalTable = new JTable(model);
        goalTable.setFont(mainFont);
        goalTable.setRowHeight(24);
        goalTable.getTableHeader().setFont(boldFont);
        ((DefaultTableCellRenderer)goalTable.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

        // === ENABLE SORTING ===
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        goalTable.setRowSorter(sorter);

        // Optional: Sort Workout column (index 1) as boolean values
        //sorter.setComparator(1, Comparator.comparing(Boolean::valueOf));

        // Sort by Date (index 0) descending
        sorter.toggleSortOrder(0);
        sorter.toggleSortOrder(0); // descending

        JScrollPane scrollPane = new JScrollPane(goalTable);
        scrollPane.setPreferredSize(new Dimension(850, 400));

        // === LOAD DATA FROM SUPABASE ===
        String email = Session.getEmail();
        GoalsLogic logic = new GoalsLogic();
        UserGoalData data = logic.getUserGoalsAndHistoryByEmail(email);

        if (data != null && data.getHistory() != null) {
            for (UserGoalHistory entry : data.getHistory()) {
                model.addRow(new Object[]{
                        entry.getGoal_date(),
                        entry.isTodays_workout(),
                        entry.getTodays_calories(),
                        entry.getTodays_carbs(),
                        entry.getTodays_protein(),
                        entry.getTodays_fats()
                });
            }
        }

        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int option = JOptionPane.showOptionDialog(
                        panel1,
                        "Are you sure you want to log out?!",
                        "Message",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        new Object[]{"Yes", "No"},
                        "Yes"
                );
                if (option == 0) {
                    Session.clear();
                    new SignInGUI();
                    JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(panel1);
                    if (topFrame != null) {
                        topFrame.dispose();
                    }
                } else if (option == 1 || option == JOptionPane.CLOSED_OPTION) {
                    //do nothing
                }
            }
        });

        // === SUBMIT BUTTON ===
        JButton btnSubmitUpdates = new JButton("Submit Changes");
        btnSubmitUpdates.setFont(boldFont);
        btnSubmitUpdates.setPreferredSize(new Dimension(180, 30));
        btnDelete.setPreferredSize(new Dimension(180, 30));

        btnSubmitUpdates.addActionListener(e -> {
            if (goalTable.isEditing()) goalTable.getCellEditor().stopCellEditing();

            StringBuilder errors = new StringBuilder();
            boolean hasErrors = false;

            for (int i = 0; i < model.getRowCount(); i++) {
                String date = model.getValueAt(i, 0).toString().trim();
                String workout = model.getValueAt(i, 1).toString().trim().toLowerCase();
                String calories = model.getValueAt(i, 2).toString().trim();
                String carbs = model.getValueAt(i, 3).toString().trim();
                String protein = model.getValueAt(i, 4).toString().trim();
                String fats = model.getValueAt(i, 5).toString().trim();

                // Validate date
                if (!date.matches("^(\\d{4}[-/]\\d{2}[-/]\\d{2})|(\\d{2}[-/]\\d{2}[-/]\\d{4})$")) {
                    errors.append("- Row ").append(i + 1).append(": Invalid date format.\n");
                    hasErrors = true;
                }

                // Validate workout
                if (!workout.equals("true") && !workout.equals("false")) {
                    errors.append("- Row ").append(i + 1).append(": Workout must be true or false.\n");
                    hasErrors = true;
                }

                // Validate Calories: optional or 0–10000
                // === CALORIES ===
                if (!calories.isEmpty()) {
                    try {
                        int calVal = Integer.parseInt(calories);
                        if (calVal < 0 || calVal > 10000) {
                            errors.append("- Row ").append(i + 1).append(": Calories must be between 0 and 10000.\n");
                            hasErrors = true;
                        }
                    } catch (NumberFormatException ex) {
                        errors.append("- Row ").append(i + 1).append(": Calories must be a valid number (0–10000).\n");
                        hasErrors = true;
                    }
                }

                // === CARBS ===
                if (!carbs.isEmpty()) {
                    try {
                        int carbVal = Integer.parseInt(carbs);
                        if (carbVal < 0 || carbVal > 20000) {
                            errors.append("- Row ").append(i + 1).append(": Carbs must be between 0 and 20000.\n");
                            hasErrors = true;
                        }
                    } catch (NumberFormatException ex) {
                        errors.append("- Row ").append(i + 1).append(": Carbs must be a valid number (0–20000).\n");
                        hasErrors = true;
                    }
                }

                // === PROTEIN ===
                if (!protein.isEmpty()) {
                    try {
                        int proteinVal = Integer.parseInt(protein);
                        if (proteinVal < 0 || proteinVal > 1000) {
                            errors.append("- Row ").append(i + 1).append(": Protein must be between 0 and 1000.\n");
                            hasErrors = true;
                        }
                    } catch (NumberFormatException ex) {
                        errors.append("- Row ").append(i + 1).append(": Protein must be a valid number (0–1000).\n");
                        hasErrors = true;
                    }
                }

                // === FATS ===
                if (!fats.isEmpty()) {
                    try {
                        int fatsVal = Integer.parseInt(fats);
                        if (fatsVal < 0 || fatsVal > 20000) {
                            errors.append("- Row ").append(i + 1).append(": Fats must be between 0 and 20000.\n");
                            hasErrors = true;
                        }
                    } catch (NumberFormatException ex) {
                        errors.append("- Row ").append(i + 1).append(": Fats must be a valid number (0–20000).\n");
                        hasErrors = true;
                    }
                }

            }

            if (hasErrors) {
                JOptionPane.showMessageDialog(this, errors.toString(), "Validation Errors", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // If valid, submit all rows
            //String email = Session.getEmail();
            for (int i = 0; i < model.getRowCount(); i++) {
                try {
                    logic.upsertGoalHistory(
                            model.getValueAt(i, 0).toString(),
                            model.getValueAt(i, 1).toString(),
                            model.getValueAt(i, 2).toString(),
                            model.getValueAt(i, 3).toString(),
                            model.getValueAt(i, 4).toString(),
                            model.getValueAt(i, 5).toString(),
                            email
                    );
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            JOptionPane.showMessageDialog(this, "Updates submitted successfully!");
        });

        btnDelete.addActionListener(e -> {
            int[] selectedRows = goalTable.getSelectedRows();
            if (selectedRows.length > 0) {
                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to delete the selected row(s)?",
                        "Confirm Deletion",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirm != JOptionPane.YES_OPTION) return;

                List<String> datesToDelete = new ArrayList<>();
                for (int i = 0; i < selectedRows.length; i++) {
                    int modelIndex = goalTable.convertRowIndexToModel(selectedRows[i]);
                    String goalDate = model.getValueAt(modelIndex, 0).toString();
                    datesToDelete.add(goalDate);
                }

                try {
                    logic.deleteGoalHistoryRows(Session.getAccessToken(), datesToDelete);
                    // After successful deletion, remove from table model
                    for (int i = selectedRows.length - 1; i >= 0; i--) {
                        model.removeRow(goalTable.convertRowIndexToModel(selectedRows[i]));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Failed to delete selected rows from Supabase.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "No rows selected to delete.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });



        // === MAIN PANEL ===
        panel1 = new JPanel(new GridBagLayout());
        panel1.setBackground(Color.LIGHT_GRAY);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel lblHint = new JLabel("You can view, sort, edit, and delete your daily history here. Click the 'Submit Changes' button to save any changes made.");
        lblHint.setFont(headerFont);
        gbc.gridy = 0;
        panel1.add(lblHint, gbc);

        gbc.gridy = 1;
        panel1.add(scrollPane, gbc);

// Create a horizontal button panel to group Submit and Delete side by side
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0)); // 20px gap between buttons
        buttonPanel.setBackground(Color.LIGHT_GRAY);
        buttonPanel.add(btnSubmitUpdates);
        buttonPanel.add(btnDelete);

// Add the button panel instead of individual buttons
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel1.add(buttonPanel, gbc);


        // === FRAME SETTINGS ===
        setTitle("Goal History");
        setIconImage(new ImageIcon(getClass().getResource("/app_icon.png")).getImage());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(panel1, BorderLayout.CENTER);
        setVisible(true);
    }
}
