package org.aimacrosapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DashboardGUI extends JFrame{
    //private JLabel lblDashboard;
    private JButton btnBack;

    private JPanel panel1;
    private JPanel topPanel;

    public DashboardGUI(){
        //lblDashboard = new JLabel("DASHBOARD");
        ImageIcon backIcon = new ImageIcon(getClass().getResource("/back_arrow.png"));
        btnBack = new JButton(backIcon);
        btnBack.setBorderPainted(false);
        btnBack.setContentAreaFilled(false);

        //back button click event
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //go back to Sign In
                new DashboardGUI();
                //close current page
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(panel1);
                if (topFrame != null) {
                    topFrame.dispose();
                }
            }
        });
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
