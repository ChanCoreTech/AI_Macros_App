package org.aimacrosapp;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Comparator;

public class GoalHistoryGUI extends JFrame {

    private JPanel panel1;
    private JButton btnBack, btnLogout;

    public GoalHistoryGUI() throws IOException {

        // === BACK BUTTON ===
        ImageIcon backIcon = new ImageIcon(getClass().getResource("/back_arrow.png"));
        btnBack = new JButton(backIcon);
        btnBack.setBorderPainted(false);
        btnBack.setContentAreaFilled(false);
        btnBack.setFocusPainted(false);
        btnBack.setBounds(10, 10, 50, 50);
        btnBack.addActionListener(e -> {
            dispose();
            new DashboardGUI();
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
        btnSubmitUpdates.addActionListener(e -> {
            if (goalTable.isEditing()) goalTable.getCellEditor().stopCellEditing();

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

        // === MAIN PANEL ===
        panel1 = new JPanel(new GridBagLayout());
        panel1.setBackground(Color.LIGHT_GRAY);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel lblHint = new JLabel("You can view, sort, and edit your daily history here. Click the 'Submit Changes' button to save any changes made.");
        lblHint.setFont(headerFont);
        gbc.gridy = 0;
        panel1.add(lblHint, gbc);

        gbc.gridy = 1;
        panel1.add(scrollPane, gbc);

        gbc.gridy = 2;
        panel1.add(btnSubmitUpdates, gbc);

        // === FRAME SETTINGS ===
        setTitle("Goal History");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(panel1, BorderLayout.CENTER);
        setVisible(true);
    }
}
