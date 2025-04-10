package org.aimacrosapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class SignInGUI extends JFrame{
    private JLabel lblWelcome, lblEmail, lblPassword;
    //back arrow for every page
    ImageIcon backIcon;
    private JButton btnBack, btnCreate, btnLogin, btnForgot;

    private JPanel panel1;
    private JPanel topPanel;

    private JTextField txtEmail = new JTextField(15);
    private JTextField txtPassword = new JTextField(15);

    public SignInGUI(){
        //initialize elements
        lblWelcome = new JLabel("Welcome! Please sign in to start using the app, or sign up if you're new!");
        lblEmail = new JLabel("Username (Email):");
        lblPassword = new JLabel("Password:");
        btnCreate = new JButton("Sign Up");
        btnLogin = new JButton("Login");
        btnForgot = new JButton("Forgot Password");

        //back button image logic
        ImageIcon backIcon = new ImageIcon(getClass().getResource("/back_arrow.png"));
        btnBack = new JButton(backIcon);
        btnBack.setBorderPainted(false);
        btnBack.setContentAreaFilled(false);

        //hard code for testing
        txtEmail.setText("jdoe1@example.com");
        txtPassword.setText("jdoe123");

        //text values
        String email = txtEmail.getText();
        String password = txtPassword.getText();

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

        // Login button click event
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = txtEmail.getText(); // Assuming txtEmail is your JTextField for the email
                String password = new String(txtPassword.getText()); // Assuming txtPassword is your JPasswordField

                AccountLogic accountLogic = new AccountLogic();
                boolean isAuthenticated = accountLogic.signIn(email, password);

                // Proceed only if authentication is successful
                if (isAuthenticated) {
                    //create User Account
                    UserAccount userAccount = new UserAccount();
                    //static Session class used to get email, identifying user
                    Session.setEmail(email);
                    try {
                        //get user information from user and user_account
                        userAccount = accountLogic.getUserAndAccountByEmail(email);
                        if(userAccount != null) {
                            //set User object in session
                            Session.setCurrentUser(userAccount.getUser());
                            int option = JOptionPane.showOptionDialog(
                                    panel1,
                                    "Login successful!",
                                    "Message",
                                    JOptionPane.DEFAULT_OPTION,
                                    JOptionPane.INFORMATION_MESSAGE,
                                    null,
                                    new Object[]{"OK"},
                                    "OK"
                            );

                            if (option == 0 || option == JOptionPane.CLOSED_OPTION) {
                                new DashboardGUI();
                                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(panel1);
                                if (topFrame != null) {
                                    topFrame.dispose();
                                }
                            }
                        }
                        else{
                            JOptionPane.showMessageDialog(panel1, "User not found.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(panel1, "Invalid email or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
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

        //resize all elements
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

        // Top panel with null layout for the back button
        topPanel = new JPanel(null); // Null layout
        topPanel.setBackground(Color.WHITE);
        topPanel.setPreferredSize(new Dimension(60, 60)); // Fixed height
        btnBack.setBounds(10, 10, 50, 50); // Positioning manually
        topPanel.add(btnBack);

        // Set up panel
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

        // Reset gridwidth for other components
        gbc.gridwidth = 1;

        // Email label and text field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel1.add(lblEmail, gbc);

        gbc.gridx = 1;
        panel1.add(txtEmail, gbc);

        // Password label and text field
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

        // Setup JFrame
        JFrame frame = new JFrame("Sign In");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1500, 1000);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);

        // Set layout to BorderLayout and add components
        frame.setLayout(new BorderLayout());
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(panel1, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}
