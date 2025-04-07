package org.aimacrosapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GoalsGUI extends JFrame {
    private JLabel lblDirections, lblWorkoutsPer, lblCalories, lblCarbs, lblProtein, lblFats;
    //back arrow for every page
    ImageIcon backIcon;
    private JButton btnBack, btnSubmit;

    private JPanel panel1;
    private JPanel topPanel;

    private JComboBox<String> comboWorkoutsPer;

    private JTextField txtCalories = new JTextField(15);
    private JTextField txtCarbs = new JTextField(15);
    private JTextField txtProtein = new JTextField(15);
    private JTextField txtFats = new JTextField(15);

    public GoalsGUI(){
        //initialize elements
        lblDirections = new JLabel("Here, you can create/edit your fitness goals! Fill in the info and click 'submit' to save it!");
        lblWorkoutsPer = new JLabel("Workouts per week:");
        lblCalories = new JLabel("Calories per day:");
        lblCarbs = new JLabel("Carbs per day:");
        lblProtein = new JLabel("Protein per day: ");
        lblFats = new JLabel("Fats per day: ");
        btnSubmit = new JButton("Submit");

        String[] comboOptionsWorkouts = {"", "1", "2", "3", "4", "5", "6", "7"};

        comboWorkoutsPer = new JComboBox<>(comboOptionsWorkouts);

        comboWorkoutsPer.setPreferredSize(new Dimension(180, 25));

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
                GoalsLogic goalsLogic = new GoalsLogic();

                //text values
                String workouts_per = (String) comboWorkoutsPer.getSelectedItem();
                String calories = txtCalories.getText();
                String carbs = txtCarbs.getText();
                String protein = txtProtein.getText();
                String fats = txtFats.getText();

                //GOALS LOGIC HERE

                int option = JOptionPane.showOptionDialog(
                        panel1,
                        "Your goals have been updated! Thanks!",
                        "Message",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        new Object[]{"OK"},
                        "OK"
                );

                if (option == 0 || option == JOptionPane.CLOSED_OPTION) {

                }
            }
        });

        //resize all elements
        //Font headerFont = new Font("Helvetica", Font.BOLD, 20);
        Font headerFont = new Font("Helvetica", Font.BOLD, 15);
        Font mainFont = new Font("Verdana", Font.PLAIN, 15);
        Font boldFont = new Font("Verdana", Font.BOLD, 15);
        lblDirections.setFont(headerFont);
        lblWorkoutsPer.setFont(mainFont);
        lblCalories.setFont(mainFont);
        lblCarbs.setFont(mainFont);
        lblProtein.setFont(mainFont);
        lblFats.setFont(mainFont);

        btnSubmit.setFont(boldFont);

        comboWorkoutsPer.setFont(mainFont);
        txtCalories.setFont(mainFont);
        txtCarbs.setFont(mainFont);
        txtProtein.setFont(mainFont);
        txtFats.setFont(mainFont);

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
        panel1.add(lblWorkoutsPer, gbc);

        gbc.gridx = 1;
        comboWorkoutsPer.setPreferredSize(new Dimension(200, 30));
        panel1.add(comboWorkoutsPer, gbc);

        // Nickname label and text field
        gbc.gridx = 0;
        gbc.gridy++;
        panel1.add(lblCalories, gbc);

        gbc.gridx = 1;
        txtCalories.setPreferredSize(new Dimension(200, 30));
        panel1.add(txtCalories, gbc);

        // Password label and text field
        gbc.gridx = 0;
        gbc.gridy++;
        panel1.add(lblCarbs, gbc);

        gbc.gridx = 1;
        txtCarbs.setPreferredSize(new Dimension(200, 30));
        panel1.add(txtCarbs, gbc);

        // Confirm Password label and text field
        gbc.gridx = 0;
        gbc.gridy++;
        panel1.add(lblProtein, gbc);

        gbc.gridx = 1;
        txtProtein.setPreferredSize(new Dimension(200, 30));
        panel1.add(txtProtein, gbc);

        // Confirm Password label and text field
        gbc.gridx = 0;
        gbc.gridy++;
        panel1.add(lblFats, gbc);

        gbc.gridx = 1;
        txtFats.setPreferredSize(new Dimension(200, 30));
        panel1.add(txtFats, gbc);

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
