package org.aimacrosapp;

import io.github.cdimascio.dotenv.Dotenv;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.URI;

public class DashboardGUI extends JFrame {
    //dotenv allows me to save keys in env file
    private static final Dotenv dotenv = Dotenv.load();
    private static final String BOTPRESS_URL = dotenv.get("MACROS_APP_BOTPRESS_URL_LINK");

    private JButton btnBack;
    private JPanel panel1;
    private JPanel lblPanel;
    private JPanel botPanel;
    private JPanel topPanel;
    private JLabel lblPrimaryGoal, lblCalories, lblCarbs, lblProtein, lblFats, lblGoalCalories, lblGoalCarbs, lblGoalProtein, lblGoalFats, lblBot;

    public DashboardGUI() {
        // Back button
        ImageIcon backIcon = new ImageIcon(getClass().getResource("/back_arrow.png"));
        btnBack = new JButton(backIcon);
        btnBack.setBorderPainted(false);
        btnBack.setContentAreaFilled(false);

        lblPrimaryGoal = new JLabel("Primary Goal: ");
        lblCalories = new JLabel("Today's Calories: ");
        lblCarbs = new JLabel("Today's Carbs: ");
        lblProtein = new JLabel("Today's Protein: ");
        lblFats = new JLabel("Today's Fats: ");
        lblGoalCalories = new JLabel("Daily Calorie Goal: ");
        lblGoalCarbs = new JLabel("Daily Carbs Goal: ");
        lblGoalProtein = new JLabel("Daily Protein Goal: ");
        lblGoalFats = new JLabel("Daily Fats Goal: ");
        lblBot = new JLabel("Click on 'Joe', your AI fitness coach for daily macros, fitness, and health guidance!");

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

        // Top panel for back button
        topPanel = new JPanel(null);
        topPanel.setBackground(Color.WHITE);
        topPanel.setPreferredSize(new Dimension(60, 60));
        btnBack.setBounds(10, 10, 50, 50);
        topPanel.add(btnBack);

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
        String[] pageNames = {"Account", "SetGoals", "GoalHistory", "Help"};

        for (int i = 0; i < iconPaths.length; i++) {
            ImageIcon icon = getScaledIcon(iconPaths[i], 50, 50); // Fixed method call
            if (icon == null) continue; // Skip if icon is not found
            JButton btn = new JButton(icon);
            btn.setBorderPainted(false);
            btn.setContentAreaFilled(false);
            String page = pageNames[i];

            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    openPage(page);
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

        gbc.gridy = 1;
        gbc.gridwidth = 2;
        JLabel lblWelcome = new JLabel("This is your personal dashboard. Click the displayed icons to explore!", SwingConstants.CENTER);

        Font mainFont = new Font("Verdana", Font.PLAIN, 15);
        Font smallFont = new Font("Verdana", Font.BOLD, 12);
        Font boldFont = new Font("Verdana", Font.BOLD, 15);
        lblWelcome.setFont(new Font("Helvetica", Font.BOLD, 25));

        lblPrimaryGoal.setFont(mainFont);
        lblCalories.setFont(mainFont);
        lblCarbs.setFont(mainFont);
        lblProtein.setFont(mainFont);
        lblFats.setFont(mainFont);
        lblGoalCalories.setFont(mainFont);
        lblGoalCarbs.setFont(mainFont);
        lblGoalProtein.setFont(mainFont);
        lblGoalFats.setFont(mainFont);
        lblBot.setFont(smallFont);
        panel1.add(lblWelcome, gbc);

        lblPanel = new JPanel(new GridBagLayout());
        lblPanel.setBackground(Color.LIGHT_GRAY);
        GridBagConstraints labelGbc = new GridBagConstraints();
        labelGbc.insets = new Insets(10, 30, 10, 30);
        labelGbc.fill = GridBagConstraints.HORIZONTAL;
        labelGbc.weightx = 1.0; // This is what allows them to push to edges

// Row 0 - Primary Goal (left only)
        labelGbc.gridx = 0;
        labelGbc.gridy = 0;
        labelGbc.anchor = GridBagConstraints.LINE_START;
        lblPanel.add(lblPrimaryGoal, labelGbc);

// Row 1 - Calories
        labelGbc.gridy = 1;
        labelGbc.gridx = 0;
        labelGbc.anchor = GridBagConstraints.LINE_START;
        lblPanel.add(lblCalories, labelGbc);
        labelGbc.gridx = 1;
        labelGbc.anchor = GridBagConstraints.LINE_END;
        lblPanel.add(lblGoalCalories, labelGbc);

// Row 2 - Carbs
        labelGbc.gridy = 2;
        labelGbc.gridx = 0;
        labelGbc.anchor = GridBagConstraints.LINE_START;
        lblPanel.add(lblCarbs, labelGbc);
        labelGbc.gridx = 1;
        labelGbc.anchor = GridBagConstraints.LINE_END;
        lblPanel.add(lblGoalCarbs, labelGbc);

// Row 3 - Protein
        labelGbc.gridy = 3;
        labelGbc.gridx = 0;
        labelGbc.anchor = GridBagConstraints.LINE_START;
        lblPanel.add(lblProtein, labelGbc);
        labelGbc.gridx = 1;
        labelGbc.anchor = GridBagConstraints.LINE_END;
        lblPanel.add(lblGoalProtein, labelGbc);

// Row 4 - Fats
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

// --- Move botPanel to the bottom ---
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;

        botPanel = new JPanel(new BorderLayout());
        botPanel.setBackground(Color.LIGHT_GRAY);

        lblBot.setHorizontalAlignment(SwingConstants.CENTER);
        botPanel.add(lblBot, BorderLayout.NORTH);

        JButton btnBotpress = new JButton(getScaledIcon("/botpress_icon.png", 200, 200));
        btnBotpress.setBorderPainted(false);
        btnBotpress.setContentAreaFilled(false);
        botPanel.add(btnBotpress, BorderLayout.CENTER);

        panel1.add(botPanel, gbc);

        //botpress button click event
        btnBotpress.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openPage("Bot");
            }
        });

        // Setup JFrame
        JFrame frame = new JFrame("Dashboard");
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

    private void openPage(String page) {
        switch (page) {
            case "Account":
                new AccountGUI();
                break;
            case "SetGoals":
                new GoalsGUI();
                break;
            case "GoalHistory":
                new GoalHistoryGUI();
                break;
            case "Help":
                new HelpGUI();
                break;
            case "Bot": // <-- new case
                try {
                    Desktop.getDesktop().browse(new URI(BOTPRESS_URL));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
        }
        //close current page
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(panel1);
        if (topFrame != null) {
            topFrame.dispose();
        }
    }

    private ImageIcon getScaledIcon(String path, int width, int height) {
        try {
            ImageIcon originalIcon = new ImageIcon(getClass().getResource(path));
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
            return null;
        }
    }
}