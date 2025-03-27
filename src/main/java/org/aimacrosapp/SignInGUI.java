package org.aimacrosapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SignInGUI extends JFrame{
    private JLabel lblWelcome, lblEmail, lblPassword;
    //back arrow for every page
    ImageIcon backIcon = new ImageIcon(getClass().getResource("/imgs/back_arrow.png"));
    private JButton btnBack = new JButton(backIcon);
    private JButton btnCreate;
    private JButton btnLogin;
    private JButton btnForgot;
    private JPanel panel1;

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

        //login click event
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //ADD IF CORRECT CREDENTIALS AND IF INCORRECT CREDENTIALS

                //When OK is clicked on the message box, open Dashboard
                int option = JOptionPane.showOptionDialog(
                        panel1,
                        "Login successful!",
                        "Message",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        new Object[]{"OK"},  // Custom button text
                        "OK"
                );
                //if anything is clicked
                if (option == 0 || option == JOptionPane.CLOSED_OPTION) {
                    //open a new page to start at the dashboard
                    new DashboardGUI();
                    //close current page
                    JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(panel1);
                    if (topFrame != null) {
                        topFrame.dispose();
                    }
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

        // Set up panel
        panel1 = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Welcome Label (Centered at the top)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span across two columns
        gbc.anchor = GridBagConstraints.CENTER;
        panel1.add(lblWelcome, gbc);

        // Reset gridwidth for other components
        gbc.gridwidth = 1;

        // Email Label (Left of text box)
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel1.add(lblEmail, gbc);

        // Email Text Field
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(txtEmail, gbc);

        // Password Label (Left of text box)
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        panel1.add(lblPassword, gbc);

        // Password Text Field
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(txtPassword, gbc);

        // Buttons (Next to each other)
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1; // Reset width
        panel1.add(btnLogin, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1; // Reset width
        panel1.add(btnCreate, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 1; // Reset width
        gbc.fill = GridBagConstraints.CENTER;
        panel1.add(btnForgot, gbc);

        //setup JFrame
        JFrame frame = new JFrame("Sign In");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1500, 1000);

        // Maximize the window to full-screen size, keeping window decorations
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setResizable(true);  // Allow window resizing if necessary
        frame.setLocationRelativeTo(null);  // Center the window

        //make visible
        frame.add(panel1);
        frame.setVisible(true);
    }
}
