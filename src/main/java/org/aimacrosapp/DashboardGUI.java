package org.aimacrosapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class DashboardGUI extends JFrame {
    private JButton btnBack;
    private JPanel panel1;
    private JPanel lblPanel;
    private JPanel botPanel;
    private JPanel topPanel;
    private JLabel lblPrimaryGoal, lblCalories, lblCarbs, lblProtein, lblFats;

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
        lblWelcome.setFont(new Font("Helvetica", Font.BOLD, 20));
        panel1.add(lblWelcome, gbc);

        // Now add labels to lblPanel
        gbc.gridx = 0;
        gbc.gridy = 0;
        lblPanel.add(lblPrimaryGoal, gbc);
        gbc.gridy = 1;
        lblPanel.add(lblCalories, gbc);
        gbc.gridy = 2;
        lblPanel.add(lblCarbs, gbc);
        gbc.gridy = 3;
        lblPanel.add(lblProtein, gbc);
        gbc.gridy = 4;
        lblPanel.add(lblFats, gbc);

        // Add lblPanel below the main content (iconPanel and welcome label)
        gbc.gridy = 2; // Place lblPanel below the welcome message
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel1.add(lblPanel, gbc);

        // Create and setup bot panel on the right
        botPanel = new JPanel(new BorderLayout());
        botPanel.setBackground(Color.LIGHT_GRAY);
        JButton btnBotpress = new JButton(getScaledIcon("/botpress_icon.png", 200, 200));
        btnBotpress.setBorderPainted(false);
        btnBotpress.setContentAreaFilled(false);
        botPanel.add(btnBotpress, BorderLayout.CENTER);

        // Add botPanel to the right side of the frame
        gbc.gridx = 2;
        gbc.gridwidth = 1;
        panel1.add(botPanel, gbc);

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