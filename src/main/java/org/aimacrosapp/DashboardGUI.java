package org.aimacrosapp;

import io.github.cdimascio.dotenv.Dotenv;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DashboardGUI extends JFrame {
    //dotenv allows me to save keys in env file
    private static final Dotenv dotenv = Dotenv.load();
    private static final String BOTPRESS_URL = dotenv.get("MACROS_APP_BOTPRESS_URL_LINK");

    private JButton btnBack, btnLogout, btnCopy;
    private JPanel panel1;
    private JPanel lblPanel;
    private JPanel botPanel;
    private JPanel topPanel;
    private JLabel lblWorkoutStatus, lblCalories, lblCarbs, lblProtein, lblFats, lblGoalWorkout, lblGoalCalories, lblGoalCarbs, lblGoalProtein, lblGoalFats, lblBot;

    public DashboardGUI() {
        //app icon
        setIconImage(new ImageIcon(getClass().getResource("/app_icon.png")).getImage());

        // Get the current local date in ISO format
        LocalDate today = LocalDate.now();
        String todayStr = today.format(DateTimeFormatter.ISO_LOCAL_DATE);

        try {
            //copy summary button
            btnCopy = new JButton("Copy Your Info");

            // Back button
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

            // Initialize all labels before setting text
            lblWorkoutStatus = new JLabel("Today's Workout: ");
            lblCalories = new JLabel("Today's Calories: ");
            lblCarbs = new JLabel("Today's Carbs: ");
            lblProtein = new JLabel("Today's Protein: ");
            lblFats = new JLabel("Today's Fats: ");
            lblGoalWorkout = new JLabel("Workouts per Week (Goal):");
            lblGoalCalories = new JLabel("Daily Calorie Goal: ");
            lblGoalCarbs = new JLabel("Daily Carbs Goal: ");
            lblGoalProtein = new JLabel("Daily Protein Goal: ");
            lblGoalFats = new JLabel("Daily Fats Goal: ");
            lblBot = new JLabel("Click on 'Joe', your AI fitness coach for daily macros, fitness, and health guidance!");

            // Fetch goal data using the session email
            String email = Session.getEmail();
            GoalsLogic logic = new GoalsLogic();
            UserGoalData goalData = logic.getUserGoalsAndHistoryByEmail(email);

            if (goalData != null) {
                // Set goal data to goal labels
                UserGoals goals = goalData.getUserGoals();
                if (goals != null) {
                    lblGoalWorkout.setText("Workouts per Week (Goal): " + goals.getWorkouts_per_week());
                    lblGoalCalories.setText("Daily Calorie Goal: " + goals.getDaily_calories() + " Cals");
                    lblGoalCarbs.setText("Daily Carbs Goal: " + goals.getDaily_carbs() + " Grams");
                    lblGoalProtein.setText("Daily Protein Goal: " + goals.getDaily_protein() + " Grams");
                    lblGoalFats.setText("Daily Fats Goal: " + goals.getDaily_fats() + " Grams");
                } else {
                    lblGoalWorkout.setText("Workouts per Week (Goal): No data");
                    lblGoalCalories.setText("Daily Calorie Goal: No data");
                    lblGoalCarbs.setText("Daily Carbs Goal: No data");
                    lblGoalProtein.setText("Daily Protein Goal: No data");
                    lblGoalFats.setText("Daily Fats Goal: No data");
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
                    lblWorkoutStatus.setText("Today's Workout: " + (todayHistory.isTodays_workout() ? "Yes" : "No"));
                    lblCalories.setText("Today's Calories: " + todayHistory.getTodays_calories() + " Cals");
                    lblCarbs.setText("Today's Carbs: " + todayHistory.getTodays_carbs() + " Grams");
                    lblProtein.setText("Today's Protein: " + todayHistory.getTodays_protein() + " Grams");
                    lblFats.setText("Today's Fats: " + todayHistory.getTodays_fats() + " Grams");
                } else {
                    lblWorkoutStatus.setText("Today's Workout: No data");
                    lblCalories.setText("Today's Calories: No data");
                    lblCarbs.setText("Today's Carbs: No data");
                    lblProtein.setText("Today's Protein: No data");
                    lblFats.setText("Today's Fats: No data");
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        //back button click event
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Navigate back
                new DashboardGUI();
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(panel1);
                if (topFrame != null) {
                    topFrame.dispose();
                }
            }
        });

        //logout button click event
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

        //copy info to clipboard click event
        btnCopy.addActionListener(e -> {
            AccountLogic logic = new AccountLogic();
            logic.copyUserSummaryToClipboard();
        });

        // Top panel for back button (left) and logout button (right)
        topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setPreferredSize(new Dimension(60, 60));

        btnBack.setPreferredSize(new Dimension(50, 50));
        btnLogout.setPreferredSize(new Dimension(100, 50)); // adjust size as needed

        topPanel.add(btnBack, BorderLayout.WEST);    // Puts back button on the far left
        topPanel.add(btnLogout, BorderLayout.EAST);  // Puts logout button on the far right

        // Main panel with GridBagLayout
        panel1 = new JPanel(new GridBagLayout());
        panel1.setBackground(Color.LIGHT_GRAY);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Create panel for icons (links to other pages)
        JPanel iconPanel = new JPanel(new GridLayout(1, 4, 20, 0)); // 4 icons in a row
        iconPanel.setBackground(Color.LIGHT_GRAY);

        // Icons for Account, SetGoals, GoalHistory, Help
        String[] iconPaths = {"/account.png", "/goals.png", "/goal_history.png", "/help.png"};
        String[] pageNames = {"Account", "Set Goals", "Goal History", "Help"};

        for (int i = 0; i < iconPaths.length; i++) {
            ImageIcon icon = getScaledIcon(iconPaths[i], 50, 50); // Fixed method call
            if (icon == null) continue; // Skip if icon is not found
            JButton btn = new JButton(icon);
            btn.setBorderPainted(false);
            btn.setContentAreaFilled(false);
            String page = pageNames[i];
            //set up preview text
            ToolTipManager.sharedInstance().setInitialDelay(100);
            btnBack.setToolTipText("Back");
            btnLogout.setToolTipText("Logout");
            btn.setToolTipText(page);

            //click events for navigation icons
            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        openPage(page);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });

            iconPanel.add(btn);
        }

        // Create the label panel to hold the goal progress labels
        lblPanel = new JPanel(new GridBagLayout());
        lblPanel.setBackground(Color.LIGHT_GRAY);

        // GridBag Constraints for Layout
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel1.add(iconPanel, gbc);
        panel1.add(iconPanel, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 2;
        JLabel lblWelcome = new JLabel("This is your personal dashboard. Click the displayed icons to explore!", SwingConstants.CENTER);

        //fonts and set fonts
        Font mainFont = new Font("Verdana", Font.PLAIN, 15);
        Font smallFont = new Font("Verdana", Font.BOLD, 12);
        Font boldFont = new Font("Verdana", Font.BOLD, 15);
        lblWelcome.setFont(new Font("Helvetica", Font.BOLD, 25));

        lblWorkoutStatus.setFont(mainFont);
        lblCalories.setFont(mainFont);
        lblCarbs.setFont(mainFont);
        lblProtein.setFont(mainFont);
        lblFats.setFont(mainFont);
        lblGoalWorkout.setFont(mainFont);
        lblGoalCalories.setFont(mainFont);
        lblGoalCarbs.setFont(mainFont);
        lblGoalProtein.setFont(mainFont);
        lblGoalFats.setFont(mainFont);
        lblBot.setFont(smallFont);
        panel1.add(lblWelcome, gbc);

        btnCopy.setFont(boldFont);
        btnCopy.setPreferredSize(new Dimension(180, 30));

        lblPanel = new JPanel(new GridBagLayout());
        lblPanel.setBackground(Color.LIGHT_GRAY);
        GridBagConstraints labelGbc = new GridBagConstraints();
        labelGbc.insets = new Insets(10, 30, 10, 30);
        labelGbc.fill = GridBagConstraints.HORIZONTAL;
        labelGbc.weightx = 1.0; // This is what allows them to push to edges

        // Workout
        labelGbc.gridy = 0;
        labelGbc.gridx = 0;
        labelGbc.anchor = GridBagConstraints.LINE_START;
        lblPanel.add(lblWorkoutStatus, labelGbc);
        labelGbc.gridx = 1;
        labelGbc.anchor = GridBagConstraints.LINE_END;
        lblPanel.add(lblGoalWorkout, labelGbc);

        // Calories
        labelGbc.gridy = 1;
        labelGbc.gridx = 0;
        labelGbc.anchor = GridBagConstraints.LINE_START;
        lblPanel.add(lblCalories, labelGbc);
        labelGbc.gridx = 1;
        labelGbc.anchor = GridBagConstraints.LINE_END;
        lblPanel.add(lblGoalCalories, labelGbc);

        // Carbs
        labelGbc.gridy = 2;
        labelGbc.gridx = 0;
        labelGbc.anchor = GridBagConstraints.LINE_START;
        lblPanel.add(lblCarbs, labelGbc);
        labelGbc.gridx = 1;
        labelGbc.anchor = GridBagConstraints.LINE_END;
        lblPanel.add(lblGoalCarbs, labelGbc);

        // Protein
        labelGbc.gridy = 3;
        labelGbc.gridx = 0;
        labelGbc.anchor = GridBagConstraints.LINE_START;
        lblPanel.add(lblProtein, labelGbc);
        labelGbc.gridx = 1;
        labelGbc.anchor = GridBagConstraints.LINE_END;
        lblPanel.add(lblGoalProtein, labelGbc);

        // Fats
        labelGbc.gridy = 4;
        labelGbc.gridx = 0;
        labelGbc.anchor = GridBagConstraints.LINE_START;
        lblPanel.add(lblFats, labelGbc);
        labelGbc.gridx = 1;
        labelGbc.anchor = GridBagConstraints.LINE_END;
        lblPanel.add(lblGoalFats, labelGbc);

        // Add lblPanel to main layout (centered)
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel1.add(lblPanel, gbc);

        // BotPanel at bottom
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;

        //bot pannel
        botPanel = new JPanel(new BorderLayout());
        botPanel.setBackground(Color.LIGHT_GRAY);

        lblBot.setHorizontalAlignment(SwingConstants.CENTER);
        botPanel.add(lblBot, BorderLayout.NORTH);

        //Joe button
        JButton btnBotpress = new JButton(getScaledIcon("/botpress_icon.png", 200, 200));
        btnBotpress.setBorderPainted(false);
        btnBotpress.setContentAreaFilled(false);
        botPanel.add(btnBotpress, BorderLayout.CENTER);
        btnBotpress.setToolTipText("AI Chatbot");

        panel1.add(botPanel, gbc);
        gbc.gridy++;
        //gbc.gridwidth = 1; // Don't let it span across columns
        gbc.anchor = GridBagConstraints.CENTER; // Or WEST if you want it aligned left
        gbc.fill = GridBagConstraints.NONE; // Prevent stretching

        panel1.add(btnCopy, gbc);

        //botpress button click event
        btnBotpress.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI(BOTPRESS_URL));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Setup JFrame
        JFrame frame = new JFrame("Dashboard");
        frame.setIconImage(new ImageIcon(getClass().getResource("/app_icon.png")).getImage());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1500, 1000);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1500, 1000);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);

        // Add components to JFrame
        frame.setLayout(new BorderLayout());
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(panel1, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    //method to open page in nav bar
    private void openPage(String page) throws IOException {
        switch (page) {
            case "Account":
                new AccountGUI();
                break;
            case "Set Goals":
                new GoalsGUI();
                break;
            case "Goal History":
                new GoalHistoryGUI();
                break;
            case "Help":
                new HelpGUI();
                break;
        }

        //close current page
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(panel1);
        if (topFrame != null) {
            topFrame.dispose();
        }
    }

    //method to scale icon down for better resolution
    private ImageIcon getScaledIcon(String path, int width, int height) {
        try {
            System.out.println("Attempting to load: " + path);
            java.net.URL resource = getClass().getResource(path);
            if (resource == null) {
                System.err.println("Resource not found: " + path);
                return null;
            }

            ImageIcon originalIcon = new ImageIcon(resource);
            Image originalImage = originalIcon.getImage();

            BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = scaledImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.drawImage(originalImage, 0, 0, width, height, null);
            g2d.dispose();

            return new ImageIcon(scaledImage);
        } catch (Exception e) {
            System.err.println("Failed to load or scale image: " + path);
            e.printStackTrace();
            return null;
        }
    }
}