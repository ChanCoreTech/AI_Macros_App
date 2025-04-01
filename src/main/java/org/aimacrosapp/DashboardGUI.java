package org.aimacrosapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;

public class DashboardGUI extends JFrame {
    private JButton btnBack;
    private JPanel panel1;
    private JPanel topPanel;

    public DashboardGUI() {
        // Back button
        ImageIcon backIcon = new ImageIcon(getClass().getResource("/back_arrow.png"));
        btnBack = new JButton(backIcon);
        btnBack.setBorderPainted(false);
        btnBack.setContentAreaFilled(false);

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

        // Create a black container panel for the icons
//        JPanel iconContainer = new JPanel();
//        iconContainer.setBackground(Color.BLACK);
//        iconContainer.setLayout(new GridLayout(1, 4, 10, 10)); // 1 row, 4 columns, spacing
//        iconContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding

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

        // Add the icon container to the main panel
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        gbc.gridwidth = 2;
//        panel1.add(iconContainer, gbc);

        // Welcome label
        //resize all elements
        Font headerFont = new Font("Helvetica", Font.BOLD, 20);
        Font mainFont = new Font("Verdana", Font.PLAIN, 15);
        Font boldFont = new Font("Verdana", Font.BOLD, 15);
        JLabel lblWelcome = new JLabel("Welcome!", SwingConstants.CENTER);
        lblWelcome.setFont(headerFont);

        // Botpress chatbot image link (Right side)
        ImageIcon botpressIcon = getScaledIcon("/botpress_icon.png", 100, 100);
        JButton btnBotpress = new JButton(botpressIcon);
        btnBotpress.setBorderPainted(false);
        btnBotpress.setContentAreaFilled(false);

        btnBotpress.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BotLogic botLogic = new BotLogic();
                botLogic.linkToBot();
            }
        });

        // Left side labels for Goal Progress
        JPanel goalPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        goalPanel.setBackground(Color.LIGHT_GRAY);
        goalPanel.add(new JLabel("Today's Goal Progress:", SwingConstants.LEFT));
        goalPanel.add(new JLabel("Today's Calories: "));
        goalPanel.add(new JLabel("Today's Carbs: "));
        goalPanel.add(new JLabel("Today's Protein: "));
        goalPanel.add(new JLabel("Today's Fats: "));

        // GridBag Constraints for Layout
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel1.add(iconPanel, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panel1.add(lblWelcome, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(goalPanel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel1.add(btnBotpress, gbc);

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
        ImageIcon icon = new ImageIcon(getClass().getResource(path));
        Image img = icon.getImage();
        Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImg);
    }

}
