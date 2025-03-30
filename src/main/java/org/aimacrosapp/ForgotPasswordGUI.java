package org.aimacrosapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ForgotPasswordGUI extends JFrame {
    private JLabel lblDirections, lblEmail, lblNickname, lblPassword, lblConfirmPass;
    //back arrow for every page
    ImageIcon backIcon;
    private JButton btnBack, btnSubmit;

    private JPanel panel1;
    private JPanel topPanel;

    private JTextField txtEmail = new JTextField(15);
    private JTextField txtNickname = new JTextField(15);
    private JTextField txtPassword = new JTextField(15);
    private JTextField txtConfirmPass = new JTextField(15);

    public ForgotPasswordGUI(){
        //initialize elements
        lblDirections = new JLabel("First, enter your email & nickname. Click the submit button. Then, enter your password & confirm your password. Click the submit button again to reset your password.");
        //lblDirections = new JLabel("Click the submit button to reset your password.");
        lblEmail = new JLabel("Username (Email):");
        lblNickname = new JLabel("Nickname:");
        lblPassword = new JLabel("Password:");
        lblConfirmPass = new JLabel("Confirm Password:");
        btnSubmit = new JButton("Submit");

        //disable the password text fields to start with
        txtPassword.setEnabled(false);
        txtConfirmPass.setEnabled(false);

        //back button image logic
        ImageIcon backIcon = new ImageIcon(getClass().getResource("/back_arrow.png"));
        btnBack = new JButton(backIcon);
        btnBack.setBorderPainted(false);
        btnBack.setContentAreaFilled(false);

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

        //submit entries click event; first accepts or declines security questions and then accepts or declines new password
        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Account Logic object
                AccountLogic accountLogic = new AccountLogic();

                //text values
                String email = txtEmail.getText();
                String nickname = txtNickname.getText();
                String password = txtPassword.getText();
                String confirmPass = txtConfirmPass.getText();

                boolean isVerified = accountLogic.confirmAccount(email, nickname);
                int clickStage = 0;

                if(isVerified){
                    clickStage = 1;
                    int option = JOptionPane.showOptionDialog(
                            panel1,
                            "Thanks for verifying you identity! Please proceed with the password reset",
                            "Message",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.INFORMATION_MESSAGE,
                            null,
                            new Object[]{"OK"},
                            "OK"
                    );

                    if (option == 0 || option == JOptionPane.CLOSED_OPTION) {
                        txtEmail.setEnabled(false);
                        txtNickname.setEnabled(false);
                        txtPassword.setEnabled(true);
                        txtConfirmPass.setEnabled(true);
                    }
                }
                else{
                    int option = JOptionPane.showOptionDialog(
                            panel1,
                            "Sorry; the email or nickname entered is not registered in our system. Please enter correct entries and try again!",
                            "Message",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.INFORMATION_MESSAGE,
                            null,
                            new Object[]{"OK"},
                            "OK"
                    );
                }

                if(clickStage == 1){
                    boolean u = accountLogic.updatePassword();
                }

//                //open a new page to create your account
//                new CreateAccountGUI();
//                //close current page
//                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(panel1);
//                if (topFrame != null) {
//                    topFrame.dispose();
//                }
            }
        });

        //resize all elements
        //Font headerFont = new Font("Helvetica", Font.BOLD, 20);
        Font headerFont = new Font("Helvetica", Font.BOLD, 15);
        Font mainFont = new Font("Verdana", Font.PLAIN, 15);
        Font boldFont = new Font("Verdana", Font.BOLD, 15);
        lblDirections.setFont(headerFont);
        lblEmail.setFont(mainFont);
        lblNickname.setFont(mainFont);
        lblPassword.setFont(mainFont);
        lblConfirmPass.setFont(mainFont);

        btnSubmit.setFont(boldFont);

        txtEmail.setFont(mainFont);
        txtNickname.setFont(mainFont);
        txtPassword.setFont(mainFont);
        txtConfirmPass.setFont(mainFont);

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

        // Directions label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel1.add(lblDirections, gbc);

        // Reset gridwidth for other components
        gbc.gridwidth = 1;

        // Email label and text field
        gbc.gridx = 0;
        gbc.gridy++;
        panel1.add(lblEmail, gbc);

        gbc.gridx = 1;
        txtEmail.setPreferredSize(new Dimension(200, 30));
        panel1.add(txtEmail, gbc);

        // Nickname label and text field
        gbc.gridx = 0;
        gbc.gridy++;
        panel1.add(lblNickname, gbc);

        gbc.gridx = 1;
        txtNickname.setPreferredSize(new Dimension(200, 30));
        panel1.add(txtNickname, gbc);

        // Password label and text field
        gbc.gridx = 0;
        gbc.gridy++;
        panel1.add(lblPassword, gbc);

        gbc.gridx = 1;
        txtPassword.setPreferredSize(new Dimension(200, 30));
        panel1.add(txtPassword, gbc);

        // Confirm Password label and text field
        gbc.gridx = 0;
        gbc.gridy++;
        panel1.add(lblConfirmPass, gbc);

        gbc.gridx = 1;
        txtConfirmPass.setPreferredSize(new Dimension(200, 30));
        panel1.add(txtConfirmPass, gbc);

        // Submit Button
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        panel1.add(btnSubmit, gbc);

        // Setup JFrame
        JFrame frame = new JFrame("Forgot Password");
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
