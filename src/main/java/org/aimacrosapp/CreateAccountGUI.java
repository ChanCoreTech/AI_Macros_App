package org.aimacrosapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateAccountGUI {
    private JLabel lblWelcome;
    private JButton btnCreateAccount;
    private JPanel panel1;

        public CreateAccountGUI(){
            //initialize elements
            lblWelcome = new JLabel("Please enter your personal and fitness info below and then click 'Create Account' when finished!");
            btnCreateAccount = new JButton("Create Account");

            //click event to submit new user info
            btnCreateAccount.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Action to be performed when the button is clicked

                    JOptionPane.showOptionDialog(panel1, "Your account has been created!!");
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
            panel1.add(lblDirections, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = 1; // Reset width to 1
            gbc.fill = GridBagConstraints.CENTER;
            panel1.add(btnCreateAccount, gbc);

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
