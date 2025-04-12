package org.aimacrosapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HelpGUI extends JFrame {

    private JButton btnBack;

    public HelpGUI() {
        // === BACK BUTTON ===
        ImageIcon backIcon = new ImageIcon(getClass().getResource("/back_arrow.png"));
        btnBack = new JButton(backIcon);
        btnBack.setBorderPainted(false);
        btnBack.setContentAreaFilled(false);
        btnBack.setBounds(10, 10, 50, 50);
        btnBack.addActionListener(e -> {
            dispose();
            new DashboardGUI();
        });

        // === TOP PANEL ===
        JPanel topPanel = new JPanel(null);
        topPanel.setBackground(Color.WHITE);
        topPanel.setPreferredSize(new Dimension(60, 60));
        topPanel.add(btnBack);

        // === FONTS ===
        Font headerFont = new Font("Helvetica", Font.BOLD, 22);
        Font mainFont = new Font("Verdana", Font.PLAIN, 15);

        // === LEFT CONTENT PANEL ===
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.LIGHT_GRAY);
        JLabel headerLbl1 = new JLabel("What's the app's purpose?");
        headerLbl1.setFont(headerFont);
        headerLbl1.setHorizontalAlignment(SwingConstants.CENTER);
        headerLbl1.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        JTextArea textArea1 = new JTextArea("Wondering where to start? The Macros Tracker with AI Assistant is an application created with the purpose of helping YOU" +
                "improve your overall fitness and customize your goals! Just create an account if you haven't, sign in, and you will be brought to the main dashboard." +
                "You will be able to do the following:\n" +
                "• Personalize your profile and fitness goals.\n" +
                "• Set your daily goals for macros amounts (calories, carbs, protein and fats.)" +
                "• Enter your daily macros amounts and log your history.\n" +
                "• Interact with Joe, your AI fitness assistant.\n" +
                "\n" +
                "Utilizing all of these features ensures you have the best experience possible when using the app. If you would like to improve heart health," +
                "lose weight, gain muscle, or have any other fitness goal, you can easily plan, set goals, and track goals this way. ");
        textArea1.setLineWrap(true);
        textArea1.setWrapStyleWord(true);
        textArea1.setEditable(false);
        textArea1.setOpaque(false);
        textArea1.setFocusable(false);
        textArea1.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        textArea1.setFont(mainFont);
        textArea1.setPreferredSize(new Dimension(500, 200));

        leftPanel.add(headerLbl1, BorderLayout.NORTH);
        leftPanel.add(textArea1, BorderLayout.CENTER);

        // === RIGHT CONTENT PANEL ===
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.LIGHT_GRAY);
        JLabel headerLbl2 = new JLabel("Page-By-Page Guide:");
        headerLbl2.setFont(headerFont);
        headerLbl2.setHorizontalAlignment(SwingConstants.CENTER);
        headerLbl2.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        JTextArea textArea2 = new JTextArea(
                "This is a page-by-page walkthrough of the app’s functions. This will help you understand each section and how to use it most effectively.");
        textArea2.setLineWrap(true);
        textArea2.setWrapStyleWord(true);
        textArea2.setEditable(false);
        textArea2.setOpaque(false);
        textArea2.setFocusable(false);
        textArea2.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        textArea2.setFont(mainFont);
        textArea2.setPreferredSize(new Dimension(500, 200));

        rightPanel.add(headerLbl2, BorderLayout.NORTH);
        rightPanel.add(textArea2, BorderLayout.CENTER);

        // === COMBINE LEFT & RIGHT INTO CONTENT PANEL ===
        JPanel contentPanel = new JPanel(new GridLayout(1, 2));
        contentPanel.add(leftPanel);
        contentPanel.add(rightPanel);

        // === FRAME SETTINGS ===
        setTitle("Help");
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
