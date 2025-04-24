package org.aimacrosapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class SignInGUI extends JFrame {
    private JLabel lblWelcome, lblEmail, lblPassword;
    private JButton btnBack, btnCreate, btnLogin, btnForgot;
    private JPanel panel1, topPanel, logoPanel;

    private JTextField txtEmail = new JTextField(15);
    private JPasswordField txtPassword = new JPasswordField(15);

    public SignInGUI() {
        // icon panel/nav
        Image scaledImage = new ImageIcon(getClass().getResource("/app_icon.png"))
                .getImage()
                .getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        JLabel lblIcon = new JLabel(new ImageIcon(scaledImage));
        logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoPanel.setBackground(Color.LIGHT_GRAY);
        logoPanel.add(lblIcon);

        // back button
        ImageIcon backIcon = new ImageIcon(getClass().getResource("/back_arrow.png"));
        btnBack = new JButton(backIcon);
        btnBack.setBorderPainted(false);
        btnBack.setContentAreaFilled(false);
        btnBack.setBounds(10, 10, 50, 50);
        ToolTipManager.sharedInstance().setInitialDelay(100);
        btnBack.setToolTipText("Back");

        topPanel = new JPanel(null);
        topPanel.setBackground(Color.WHITE);
        topPanel.setPreferredSize(new Dimension(60, 60));
        topPanel.add(btnBack);

        // main content
        lblWelcome = new JLabel("Welcome! Please sign in to start using the app, or sign up if you're new!");
        lblEmail = new JLabel("Username (Email):");
        lblPassword = new JLabel("Password:");
        btnCreate = new JButton("Sign Up");
        btnLogin = new JButton("Login");
        btnForgot = new JButton("Forgot Password");

        // Style elements
        Font headerFont = new Font("Helvetica", Font.BOLD, 20);
        Font mainFont = new Font("Verdana", Font.PLAIN, 15);
        Font boldFont = new Font("Verdana", Font.BOLD, 15);
        lblWelcome.setFont(headerFont);
        lblEmail.setFont(mainFont);
        lblPassword.setFont(mainFont);
        btnLogin.setFont(boldFont);
        btnCreate.setFont(boldFont);
        btnForgot.setFont(boldFont);
        txtEmail.setFont(mainFont);
        txtPassword.setFont(mainFont);

        panel1 = new JPanel(new GridBagLayout());
        panel1.setBackground(Color.LIGHT_GRAY);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Welcome label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel1.add(lblWelcome, gbc);
        gbc.gridwidth = 1;

        // Email
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel1.add(lblEmail, gbc);
        gbc.gridx = 1;
        panel1.add(txtEmail, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel1.add(lblPassword, gbc);
        gbc.gridx = 1;
        panel1.add(txtPassword, gbc);

        // Buttons
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel1.add(btnLogin, gbc);
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel1.add(btnCreate, gbc);
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 1;
        gbc.gridy = 4;
        panel1.add(btnForgot, gbc);

        // layout wrapping
        // Combine logoPanel and panel1 into a vertically stacked box
        JPanel stackedPanel = new JPanel();
        stackedPanel.setLayout(new BoxLayout(stackedPanel, BoxLayout.Y_AXIS));
        stackedPanel.setBackground(Color.LIGHT_GRAY);
        stackedPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        stackedPanel.setAlignmentY(Component.CENTER_ALIGNMENT);

        // Align and space the contents
        logoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel1.setAlignmentX(Component.CENTER_ALIGNMENT);

        stackedPanel.add(logoPanel);
        stackedPanel.add(Box.createRigidArea(new Dimension(0, 20))); // vertical spacing
        stackedPanel.add(panel1);

        // Wrap stackedPanel in a full screen GridBagLayout to center it
        JPanel fullWrapper = new JPanel(new GridBagLayout());
        fullWrapper.setBackground(Color.LIGHT_GRAY);
        GridBagConstraints fullGbc = new GridBagConstraints();
        fullGbc.gridx = 0;
        fullGbc.gridy = 0;
        // Anchor it higher vertically
        fullGbc.anchor = GridBagConstraints.NORTH;
        // Add padding from the top (smaller padding = higher on screen)
        fullGbc.insets = new Insets(0, 0, 0, 0);
        fullWrapper.add(stackedPanel, fullGbc);

        // setup frame
        setTitle("Sign In");
        setIconImage(new ImageIcon(getClass().getResource("/app_icon.png")).getImage());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(fullWrapper, BorderLayout.CENTER);
        setVisible(true);

        //back button click event
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //go back to Sign In
                new SignInGUI();
                //close current page
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(panel1);
                if (topFrame != null) {
                    topFrame.dispose();
                }
            }
        });

        //create account click event
        btnCreate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //open a new page to create your account
                new CreateAccountGUI();
                //close current page
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(panel1);
                if (topFrame != null) {
                    topFrame.dispose();
                }
            }
        });

        //forgot password click event
        btnForgot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //open a new page to reset forget password
                new ForgotPasswordGUI();
                //close current page
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(panel1);
                if (topFrame != null) {
                    topFrame.dispose();
                }
            }
        });

        //login click event
        btnLogin.addActionListener(e -> {
            String email = txtEmail.getText().trim();
            String password = new String(txtPassword.getPassword());

            if (!email.matches("^[\\w-\\.]+@[\\w-]+\\.[a-z]{2,6}$")) {
                JOptionPane.showMessageDialog(panel1, "Email is in an invalid format.", "Invalid Email", JOptionPane.ERROR_MESSAGE);
                return;
            }
            AccountLogic accountLogic = new AccountLogic();
            try {
                boolean isAuthenticated = accountLogic.signIn(email, password);
                if (isAuthenticated) {
                    Session.setEmail(email);
                    UserAccount userAccount = accountLogic.getUserAndAccountByEmail(email);
                    if (userAccount != null && userAccount.getUser() != null) {
                        Session.setCurrentUser(userAccount.getUser());
                        Session.setCurrentUserAccount(userAccount);
                        JOptionPane.showMessageDialog(panel1, "Login successful!");
                        new DashboardGUI();
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(panel1,
                                "Login successful but user data is missing.\nPlease recreate your profile.",
                                "Data Missing", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(panel1, "Invalid email or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(panel1, "An error occurred during login.", "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
