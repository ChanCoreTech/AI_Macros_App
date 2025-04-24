package org.aimacrosapp;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class GoalsGUI extends JFrame {
    private JLabel lblDirections, lblWorkoutsPer, lblCalories, lblCarbs, lblProtein, lblFats;
    private JLabel lblTodayWorkout, lblTodayCalories, lblTodayCarbs, lblTodayProtein, lblTodayFats, lblTodayDate;

    private JButton btnBack, btnLogout, btnSubmit, btnUpdate;

    private JPanel panel1;
    private JPanel topPanel;

    private JComboBox<String> comboWorkoutsPer, comboTodayWorkout;

    private JTextField txtCalories = new JTextField(15);
    private JTextField txtCarbs = new JTextField(15);
    private JTextField txtProtein = new JTextField(15);
    private JTextField txtFats = new JTextField(15);

    private JTextField txtTodayDate = new JTextField(15);
    private JTextField txtTodayCalories = new JTextField(15);
    private JTextField txtTodayCarbs = new JTextField(15);
    private JTextField txtTodayProtein = new JTextField(15);
    private JTextField txtTodayFats = new JTextField(15);

    public GoalsGUI() throws IOException {
        // Get the current local date in ISO format
        LocalDate today = LocalDate.now();
        String todayStr = today.format(DateTimeFormatter.ISO_LOCAL_DATE);

        // === LABELS ===
        lblDirections = new JLabel("Here, you can create/edit your general goals or enter today's daily macros to track your progress!");
        lblWorkoutsPer = new JLabel("Workouts per week:");
        lblCalories = new JLabel("Calories per day:");
        lblCarbs = new JLabel("Carbs per day (grams):");
        lblProtein = new JLabel("Protein per day (grams):");
        lblFats = new JLabel("Fats per day (grams):");
        lblTodayDate = new JLabel("Today's Date:");
        lblTodayWorkout = new JLabel("Did You Workout Today?:");
        lblTodayCalories = new JLabel("Today's Calories:");
        lblTodayCarbs = new JLabel("Today's Carbs (grams):");
        lblTodayProtein = new JLabel("Today's Protein (grams):");
        lblTodayFats = new JLabel("Today's Fats (grams):");
        btnSubmit = new JButton("Submit");
        btnUpdate = new JButton("Update");

        String[] comboOptionsWorkouts = {"", "1", "2", "3", "4", "5", "6", "7"};
        String[] comboOptionsTodayWorkout = {"", "true", "false"};
        comboWorkoutsPer = new JComboBox<>(comboOptionsWorkouts);
        comboTodayWorkout = new JComboBox<>(comboOptionsTodayWorkout);

        comboWorkoutsPer.setPreferredSize(new Dimension(180, 25));
        comboTodayWorkout.setPreferredSize(new Dimension(180, 25));

        // Fetch goal data using the session email
        String email = Session.getEmail();
        GoalsLogic logic = new GoalsLogic();
        UserGoalData goalData = logic.getUserGoalsAndHistoryByEmail(email);

        if (goalData != null) {
            // Set goal data to goal labels
            UserGoals goals = goalData.getUserGoals();
            if (goals != null) {
                comboWorkoutsPer.setSelectedItem(goals.getWorkouts_per_week());
                txtCalories.setText(String.valueOf(goals.getDaily_calories()));
                txtCarbs.setText(String.valueOf(goals.getDaily_carbs()));
                txtProtein.setText(String.valueOf(goals.getDaily_protein()));
                txtFats.setText(String.valueOf(goals.getDaily_fats()));
            } else {
                comboWorkoutsPer.setSelectedItem("");
                txtCalories.setText("");
                txtCarbs.setText("");
                txtProtein.setText("");
                txtFats.setText("");
            }

            // Look for today's history in the list
            java.util.List<UserGoalHistory> historyList = goalData.getHistory();
            UserGoalHistory todayHistory = null;
            for (UserGoalHistory history : historyList) {
                if (history.getGoal_date() != null && history.getGoal_date().equals(todayStr)) {
                    todayHistory = history;
                    break;
                }
            }

            if (todayHistory != null) {
                txtTodayDate.setText(todayStr);
                comboTodayWorkout.setSelectedItem(String.valueOf(todayHistory.isTodays_workout()));
                txtTodayCalories.setText(String.valueOf(todayHistory.getTodays_calories()));
                txtTodayCarbs.setText(String.valueOf(todayHistory.getTodays_carbs()));
                txtTodayProtein.setText(String.valueOf(todayHistory.getTodays_protein()));
                txtTodayFats.setText(String.valueOf(todayHistory.getTodays_fats()));
            } else {
                txtTodayDate.setText(todayStr);
                comboTodayWorkout.setSelectedItem("");
                txtTodayCalories.setText("");
                txtTodayCarbs.setText("");
                txtTodayProtein.setText("");
                txtTodayFats.setText("");
            }
        }

        ImageIcon backIcon = new ImageIcon(getClass().getResource("/back_arrow.png"));
        btnBack = new JButton(backIcon);
        btnBack.setBorderPainted(false);
        btnBack.setContentAreaFilled(false);
        btnBack.setFocusPainted(false);

        // Log Out
        ImageIcon logoutIcon = new ImageIcon(getClass().getResource("/logout.png"));
        btnLogout = new JButton(logoutIcon);
        btnLogout.setBorderPainted(false);
        btnLogout.setContentAreaFilled(false);

        ToolTipManager.sharedInstance().setInitialDelay(100);
        btnBack.setToolTipText("Back");
        btnLogout.setToolTipText("Logout");

        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DashboardGUI();
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(panel1);
                if (topFrame != null) {
                    topFrame.dispose();
                }
            }
        });

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

        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GoalsLogic goalsLogic = new GoalsLogic();

                String workouts_per = (String) comboWorkoutsPer.getSelectedItem();
                String calories = txtCalories.getText().trim();
                String carbs = txtCarbs.getText().trim();
                String protein = txtProtein.getText().trim();
                String fats = txtFats.getText().trim();

                StringBuilder errors = new StringBuilder();

                if (!calories.isEmpty()) {
                    try {
                        int cal = Integer.parseInt(calories);
                        if (cal < 0 || cal > 10000) errors.append("- Calories must be between 0 and 10000\n");
                    } catch (NumberFormatException ex) {
                        errors.append("- Calories must be a number\n");
                    }
                }

                if (!carbs.isEmpty()) {
                    try {
                        int carb = Integer.parseInt(carbs);
                        if (carb < 0 || carb > 20000) errors.append("- Carbs must be between 0 and 20000\n");
                    } catch (NumberFormatException ex) {
                        errors.append("- Carbs must be a number\n");
                    }
                }

                if (!protein.isEmpty()) {
                    try {
                        int prot = Integer.parseInt(protein);
                        if (prot < 0 || prot > 1000) errors.append("- Protein must be between 0 and 1000\n");
                    } catch (NumberFormatException ex) {
                        errors.append("- Protein must be a number\n");
                    }
                }

                if (!fats.isEmpty()) {
                    try {
                        int fat = Integer.parseInt(fats);
                        if (fat < 0 || fat > 20000) errors.append("- Fats must be between 0 and 20000\n");
                    } catch (NumberFormatException ex) {
                        errors.append("- Fats must be a number\n");
                    }
                }

                if (errors.length() > 0) {
                    JOptionPane.showMessageDialog(panel1, errors.toString(), "Validation Errors", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    goalsLogic.upsertGoals(workouts_per, calories, carbs, protein, fats, Session.getEmail());
                    JOptionPane.showMessageDialog(panel1, "Your goals have been updated! Thanks!");
                } catch (IOException | InterruptedException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(panel1, "Failed to update goals.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GoalsLogic goalsLogic = new GoalsLogic();

                String today_date = txtTodayDate.getText().trim();
                String today_workout = (String) comboTodayWorkout.getSelectedItem();
                String todayCalories = txtTodayCalories.getText().trim();
                String todayCarbs = txtTodayCarbs.getText().trim();
                String todayProtein = txtTodayProtein.getText().trim();
                String todayFats = txtTodayFats.getText().trim();

                StringBuilder errors = new StringBuilder();

                if (!today_date.matches("^(\\d{4}[-/]\\d{2}[-/]\\d{2})|(\\d{2}[-/]\\d{2}[-/]\\d{4})$")) {
                    errors.append("- Invalid date format (use MM/DD/YYYY, YYYY/MM/DD, MM-DD-YYYY, or YYYY-MM-DD)\n");
                }

                if (!todayCalories.isEmpty()) {
                    try {
                        int cal = Integer.parseInt(todayCalories);
                        if (cal < 0 || cal > 10000) errors.append("- Calories must be between 0 and 10000\n");
                    } catch (NumberFormatException ex) {
                        errors.append("- Calories must be a number\n");
                    }
                }

                if (!todayCarbs.isEmpty()) {
                    try {
                        int carb = Integer.parseInt(todayCarbs);
                        if (carb < 0 || carb > 20000) errors.append("- Carbs must be between 0 and 20000\n");
                    } catch (NumberFormatException ex) {
                        errors.append("- Carbs must be a number\n");
                    }
                }

                if (!todayProtein.isEmpty()) {
                    try {
                        int prot = Integer.parseInt(todayProtein);
                        if (prot < 0 || prot > 1000) errors.append("- Protein must be between 0 and 1000\n");
                    } catch (NumberFormatException ex) {
                        errors.append("- Protein must be a number\n");
                    }
                }

                if (!todayFats.isEmpty()) {
                    try {
                        int fat = Integer.parseInt(todayFats);
                        if (fat < 0 || fat > 20000) errors.append("- Fats must be between 0 and 20000\n");
                    } catch (NumberFormatException ex) {
                        errors.append("- Fats must be a number\n");
                    }
                }

                if (errors.length() > 0) {
                    JOptionPane.showMessageDialog(panel1, errors.toString(), "Validation Errors", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    goalsLogic.upsertGoalHistory(today_date, today_workout, todayCalories, todayCarbs, todayProtein, todayFats, Session.getEmail());
                    JOptionPane.showMessageDialog(panel1, "Your daily goal progress has been updated! Thanks!");
                } catch (IOException | InterruptedException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(panel1, "Failed to update today's goals.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        Font headerFont = new Font("Helvetica", Font.BOLD, 22);
        Font mainFont = new Font("Verdana", Font.PLAIN, 15);
        Font boldFont = new Font("Verdana", Font.BOLD, 15);

        lblDirections.setFont(headerFont);
        lblWorkoutsPer.setFont(mainFont);
        lblCalories.setFont(mainFont);
        lblCarbs.setFont(mainFont);
        lblProtein.setFont(mainFont);
        lblFats.setFont(mainFont);
        lblTodayDate.setFont(mainFont);
        lblTodayWorkout.setFont(mainFont);
        lblTodayCalories.setFont(mainFont);
        lblTodayCarbs.setFont(mainFont);
        lblTodayProtein.setFont(mainFont);
        lblTodayFats.setFont(mainFont);
        btnSubmit.setFont(boldFont);
        btnUpdate.setFont(boldFont);

        comboWorkoutsPer.setFont(mainFont);
        comboTodayWorkout.setFont(mainFont);

        txtCalories.setFont(mainFont);
        txtCarbs.setFont(mainFont);
        txtProtein.setFont(mainFont);
        txtFats.setFont(mainFont);
        txtTodayDate.setFont(mainFont);
        txtTodayCalories.setFont(mainFont);
        txtTodayCarbs.setFont(mainFont);
        txtTodayProtein.setFont(mainFont);
        txtTodayFats.setFont(mainFont);

        // Top panel for back button (left) and logout button (right)
        topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setPreferredSize(new Dimension(60, 60));

        btnBack.setPreferredSize(new Dimension(50, 50));
        btnLogout.setPreferredSize(new Dimension(100, 50)); // adjust size as needed

        topPanel.add(btnBack, BorderLayout.WEST);    // Puts back button on the far left
        topPanel.add(btnLogout, BorderLayout.EAST);  // Puts logout button on the far right

        // MAIN GOALS PANEL
        panel1 = new JPanel(new GridBagLayout());
        panel1.setBackground(Color.LIGHT_GRAY);
        GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.insets = new Insets(10, 10, 10, 10);
        gbc1.fill = GridBagConstraints.HORIZONTAL;

        int y = 0;
        gbc1.gridx = 0;
        gbc1.gridy = y++;
        gbc1.gridwidth = 2;
        gbc1.anchor = GridBagConstraints.CENTER;
        JLabel lblGoalsHeader = new JLabel("Create or Edit Your Goals!");
        lblGoalsHeader.setFont(headerFont);
        panel1.add(lblGoalsHeader, gbc1);

        gbc1.gridwidth = 1;
        gbc1.anchor = GridBagConstraints.WEST;

        gbc1.gridy = y++;
        panel1.add(lblWorkoutsPer, gbc1);
        gbc1.gridx = 1;
        panel1.add(comboWorkoutsPer, gbc1);

        gbc1.gridx = 0;
        gbc1.gridy = y++;
        panel1.add(lblCalories, gbc1);
        gbc1.gridx = 1;
        panel1.add(txtCalories, gbc1);

        gbc1.gridx = 0;
        gbc1.gridy = y++;
        panel1.add(lblCarbs, gbc1);
        gbc1.gridx = 1;
        panel1.add(txtCarbs, gbc1);

        gbc1.gridx = 0;
        gbc1.gridy = y++;
        panel1.add(lblProtein, gbc1);
        gbc1.gridx = 1;
        panel1.add(txtProtein, gbc1);

        gbc1.gridx = 0;
        gbc1.gridy = y++;
        panel1.add(lblFats, gbc1);
        gbc1.gridx = 1;
        panel1.add(txtFats, gbc1);

        gbc1.gridx = 0;
        gbc1.gridy = y++;
        gbc1.gridwidth = 2;
        gbc1.anchor = GridBagConstraints.CENTER;
        panel1.add(btnSubmit, gbc1);

        // TODAY'S STATS PANEL
        JPanel panel2 = new JPanel(new GridBagLayout());
        panel2.setBackground(Color.LIGHT_GRAY);
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(10, 10, 10, 10);
        gbc2.fill = GridBagConstraints.HORIZONTAL;

        y = 0;
        gbc2.gridx = 0;
        gbc2.gridy = y++;
        gbc2.gridwidth = 2;
        gbc2.anchor = GridBagConstraints.CENTER;
        JLabel lblTodayHeader = new JLabel("Log Today's Goal Progress!");
        lblTodayHeader.setFont(headerFont);
        panel2.add(lblTodayHeader, gbc2);

        gbc2.gridwidth = 1;
        gbc2.anchor = GridBagConstraints.WEST;

        gbc2.gridx = 0;
        gbc2.gridy = y++;
        panel2.add(lblTodayDate, gbc2);
        gbc2.gridx = 1;
        panel2.add(txtTodayDate, gbc2);

        gbc2.gridx = 0;
        gbc2.gridy = y++;
        panel2.add(lblTodayWorkout, gbc2);
        gbc2.gridx = 1;
        panel2.add(comboTodayWorkout, gbc2);

        gbc2.gridx = 0;
        gbc2.gridy = y++;
        panel2.add(lblTodayCalories, gbc2);
        gbc2.gridx = 1;
        panel2.add(txtTodayCalories, gbc2);

        gbc2.gridx = 0;
        gbc2.gridy = y++;
        panel2.add(lblTodayCarbs, gbc2);
        gbc2.gridx = 1;
        panel2.add(txtTodayCarbs, gbc2);

        gbc2.gridx = 0;
        gbc2.gridy = y++;
        panel2.add(lblTodayProtein, gbc2);
        gbc2.gridx = 1;
        panel2.add(txtTodayProtein, gbc2);

        gbc2.gridx = 0;
        gbc2.gridy = y++;
        panel2.add(lblTodayFats, gbc2);
        gbc2.gridx = 1;
        panel2.add(txtTodayFats, gbc2);

        gbc1.gridx = 0;
        gbc1.gridy = y++;
        gbc1.gridwidth = 2;
        gbc1.anchor = GridBagConstraints.CENTER;
        panel2.add(btnUpdate, gbc1);

        // CONTENT PANEL
        JPanel contentPanel = new JPanel(new GridLayout(1, 2));
        contentPanel.add(panel1);
        contentPanel.add(panel2);

        // FRAME SETTINGS
        setTitle("Goals");
        setIconImage(new ImageIcon(getClass().getResource("/app_icon.png")).getImage());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        setVisible(true);
    }
}