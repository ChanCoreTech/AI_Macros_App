package org.aimacrosapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SignInGUI {
    private JLabel lblWelcome;
    private JButton btnCreate;
    private JPanel panel1;

    public SignInGUI(){
        //initialize elements
        lblWelcome = new JLabel("Welcome! Please sign in to start using the app, or sign up if you're new!");
        btnCreate = new JButton("Sign Up");

        //create account click event
        btnCreate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //open a new page
                new CreateAccountGUI();
                //JOptionPane.showMessageDialog(panel1, "Button clicked!");
            }
        });

        //set up panel
        panel1 = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Make label span two columns
        gbc.anchor = GridBagConstraints.CENTER;
        panel1.add(lblWelcome, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1; // Reset width to 1
        gbc.fill = GridBagConstraints.CENTER;
        panel1.add(btnCreate, gbc);

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
