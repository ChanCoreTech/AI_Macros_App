package org.aimacrosapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ForgotPasswordGUI extends JFrame {
    private JLabel lblDirections, lblEmail, lblNickname;
    ImageIcon backIcon;
    private JButton btnBack, btnSubmit;

    private JPanel panel1;
    private JPanel topPanel;

    private JTextField txtEmail = new JTextField(15);
    private JTextField txtNickname = new JTextField(15);

    public ForgotPasswordGUI() {
        // Initialize elements
        lblDirections = new JLabel("Enter your email and nickname. A password reset link will be sent if you are verified.");
        lblEmail = new JLabel("Username (Email):");
        lblNickname = new JLabel("Nickname:");
        btnSubmit = new JButton("Submit");

        btnSubmit.setPreferredSize(new Dimension(150, 30));

        // Back button logic
        ImageIcon backIcon = new ImageIcon(getClass().getResource("/back_arrow.png"));
        btnBack = new JButton(backIcon);
        btnBack.setBorderPainted(false);
        btnBack.setContentAreaFilled(false);
        ToolTipManager.sharedInstance().setInitialDelay(100);
        btnBack.setToolTipText("Back");
        btnBack.addActionListener(e -> {
            new SignInGUI();
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(panel1);
            if (topFrame != null) topFrame.dispose();
        });

        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AccountLogic logic = new AccountLogic();
                String email = txtEmail.getText().trim();
                String nickname = txtNickname.getText().trim();

                String userId = logic.confirmAccount(email, nickname);
                if (userId != null) {
                    boolean sent = logic.sendPasswordResetEmail(email);
                    if (sent) {
                        JOptionPane.showMessageDialog(panel1,
                                "Password reset link sent to your email!",
                                "Email Sent",
                                JOptionPane.INFORMATION_MESSAGE);
                        new SignInGUI();
                        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(panel1);
                        if (topFrame != null) topFrame.dispose();
                    } else {
                        JOptionPane.showMessageDialog(panel1,
                                "Failed to send password reset email.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(panel1,
                            "The email or nickname entered is not registered.",
                            "Verification Failed",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Resize elements
        Font headerFont = new Font("Helvetica", Font.BOLD, 18);
        Font mainFont = new Font("Verdana", Font.PLAIN, 15);
        Font boldFont = new Font("Verdana", Font.BOLD, 15);
        lblDirections.setFont(headerFont);
        lblEmail.setFont(mainFont);
        lblNickname.setFont(mainFont);
        btnSubmit.setFont(boldFont);
        txtEmail.setFont(mainFont);
        txtNickname.setFont(mainFont);

        // Top panel
        topPanel = new JPanel(null);
        topPanel.setBackground(Color.WHITE);
        topPanel.setPreferredSize(new Dimension(60, 60));
        btnBack.setBounds(10, 10, 50, 50);
        topPanel.add(btnBack);

        // Main panel
        panel1 = new JPanel(new GridBagLayout());
        panel1.setBackground(Color.LIGHT_GRAY);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel1.add(lblDirections, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy++;
        panel1.add(lblEmail, gbc);
        gbc.gridx = 1;
        txtEmail.setPreferredSize(new Dimension(200, 30));
        panel1.add(txtEmail, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel1.add(lblNickname, gbc);
        gbc.gridx = 1;
        txtNickname.setPreferredSize(new Dimension(200, 30));
        panel1.add(txtNickname, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        panel1.add(btnSubmit, gbc);

        JFrame frame = new JFrame("Forgot Password");
        frame.setIconImage(new ImageIcon(getClass().getResource("/app_icon.png")).getImage());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1500, 1000);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(panel1, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}