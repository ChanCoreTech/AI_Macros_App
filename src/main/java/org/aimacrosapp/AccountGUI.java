package org.aimacrosapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;


    public class AccountGUI extends JFrame {
        private JLabel lblDirections, lblFirst, lblLast, lblBirth, lblGender, lblHeightFeet, lblHeightInches, lblWeight, lblBodyType, lblExperience, lblActivity, lblPrimary,
                lblEmail, lblPassword, lblNickname, lblPhoneNumber, lblEmailSecond;

        private JTextField txtFirst = new JTextField(15),
                txtLast = new JTextField(15),
                txtBirth = new JTextField(15),
                txtGender = new JTextField(15),
                txtHeightFeet = new JTextField(15),
                txtHeightInches = new JTextField(15),
                txtWeight = new JTextField(15),
                txtEmail = new JTextField(15),
                txtPassword = new JTextField(15),
                txtNickname = new JTextField(15),
                txtPhoneNumber = new JTextField(15),
                txtEmailSecond = new JTextField(15);

        private JComboBox<String> comboBodyType, comboExperience, comboActivity, comboPrimary;

        //back arrow for every page
        ImageIcon backIcon;
        private JButton btnBack, btnUpdateAccount;
        private JPanel panel1;
        private JPanel topPanel;

        public AccountGUI(){
            //initialize elements
            lblDirections = new JLabel("Below is all of your profile information.");
            lblFirst = new JLabel("First Name:");
            lblLast = new JLabel("Last Name:");
            lblBirth = new JLabel("Birth Date:");
            lblGender = new JLabel("Gender:");
            lblHeightFeet = new JLabel("Height (Feet):");
            lblHeightInches = new JLabel("Height (Inches):");
            lblWeight = new JLabel("Weight (lbs):");
            lblBodyType = new JLabel("Body Type:");
            lblExperience = new JLabel("Experience Level:");
            lblActivity = new JLabel("Activity Level:");
            lblPrimary = new JLabel("Primary Goal:");
            lblEmail = new JLabel("Email:");
            lblPassword = new JLabel("Password:");
            lblNickname = new JLabel("Nickname:");
            lblPhoneNumber = new JLabel("Phone Number:");
            lblEmailSecond = new JLabel("Secondary Email:");
            btnUpdateAccount = new JButton("Update Account");

            //placeholders
            txtFirst.setText("John");
            txtLast.setText("Doe");
            txtBirth.setText("1960/08/08");
            txtGender.setText("Male");
            txtHeightFeet.setText("5");
            txtHeightInches.setText("9");
            txtWeight.setText("140");
            txtEmail.setText("jdoe1@example.com");
            txtPassword.setText("jdoe123");
            txtNickname.setText("AWESOME");
            txtPhoneNumber.setText("440-658-8956");

            String[] comboOptionsBodyType = {"", "Slim", "Standard", "Athletic", "Muscular", "Bodybuilder", "Heavyset", "Obese"};
            String[] comboOptionsExperience = {"", "Beginner", "Intermediate", "Advanced"};
            String[] comboOptionsActivity = {"", "Sedentary (0-1 days a week)", "Light (1-3 days a week)", "Moderate (3-5 days a week)", "Extreme (5-7 days a week)"};
            String[] comboOptionsPrimary = {"", "Improve Overall Fitness", "Improve Heart Health", "Improve Conditioning", "Physical Therapy", "Lose Weight", "Lose Weight (Rapid)", "Gain Weight", "Gain Weight (Rapid)", "Gain Muscle (Lean)", "Gain Muscle (Bulk)"};

            comboBodyType = new JComboBox<>(comboOptionsBodyType);
            comboExperience = new JComboBox<>(comboOptionsExperience);
            comboActivity = new JComboBox<>(comboOptionsActivity);
            comboPrimary = new JComboBox<>(comboOptionsPrimary);

            comboBodyType.setPreferredSize(new Dimension(180, 25));
            comboExperience.setPreferredSize(new Dimension(180, 25));
            comboActivity.setPreferredSize(new Dimension(180, 25));
            comboPrimary.setPreferredSize(new Dimension(180, 25));

            //you cant change password on this screen
            txtPassword.setEditable(false);

            //autofill account information
            try {
                AccountLogic logic = new AccountLogic();
                UserAccount account = logic.getUserAccountForSession();

                if (account != null) {
                    User user = account.getUser();

                    txtFirst.setText(user.getFirst_name());
                    txtLast.setText(user.getLast_name());
                    txtBirth.setText(user.getBirth_date());
                    txtGender.setText(user.getGender());
                    txtHeightFeet.setText(String.valueOf(user.getHeight_feet()));
                    txtHeightInches.setText(String.valueOf(user.getHeight_inches()));
                    txtWeight.setText(String.valueOf(user.getWeight_lbs()));
                    comboBodyType.setSelectedItem(user.getBody_type());
                    comboExperience.setSelectedItem(user.getExperience_level());
                    comboActivity.setSelectedItem(user.getActivity_level());
                    comboPrimary.setSelectedItem(user.getPrimary_goal());

                    txtEmail.setText(account.getEmail());
                    txtPassword.setText(account.getPassword());
                    txtNickname.setText(account.getNickname());
                    txtPhoneNumber.setText(account.getPhone_number());
                    txtEmailSecond.setText(account.getEmail_second());
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }


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

            //click event to submit new user info
            btnUpdateAccount.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Values from text fields
                    String first_name = txtFirst.getText();
                    String last_name = txtLast.getText();
                    String birth_date = txtBirth.getText();
                    String gender = txtGender.getText();
                    String height_feet_str = txtHeightFeet.getText();
                    String height_inches_str = txtHeightInches.getText();
                    String weight_lbs_str = txtWeight.getText();
                    String body_type = (String) comboBodyType.getSelectedItem();
                    String experienceLevel = (String) comboExperience.getSelectedItem();
                    String activity_goal = (String) comboActivity.getSelectedItem();
                    String primary_goal = (String) comboPrimary.getSelectedItem();
                    String email = txtEmail.getText();
                    String password = txtPassword.getText();
                    String nickname = txtNickname.getText();
                    String phone_number = txtPhoneNumber.getText();
                    String email_second = txtEmailSecond.getText();

                    // Parse height and weight safely
                    int height_feet, height_inches, weight_lbs;
                    try {
                        height_feet = Integer.parseInt(height_feet_str);
                        height_inches = Integer.parseInt(height_inches_str);
                        weight_lbs = Integer.parseInt(weight_lbs_str);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(panel1, "Please enter valid numbers for height and weight.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Create user and user account objects
                    User user = new User(first_name, last_name, birth_date, gender, height_feet, height_inches,
                            weight_lbs, body_type, experienceLevel, activity_goal, primary_goal);
                    UserAccount userAccount = new UserAccount(user, email, password, nickname, phone_number, email_second);

                    AccountLogic accountLogic = new AccountLogic();

                    try {
                        // Attempt to update user table
                        boolean userUpdated = accountLogic.updateUser(
                                email, user.getFirst_name(), user.getLast_name(), user.getBirth_date(), user.getGender(),
                                user.getHeight_feet(), user.getHeight_inches(), user.getWeight_lbs(),
                                user.getBody_type(), user.getExperience_level(), user.getActivity_level(), user.getPrimary_goal()
                        );

                        if (userUpdated) {
                            // Then attempt to update user_account table
                            boolean userAccountUpdated = accountLogic.updateUserAccount(
                                    userAccount.getEmail(), userAccount.getPassword(),
                                    userAccount.getNickname(), userAccount.getPhone_number(), userAccount.getEmail_second()
                            );

                            if (userAccountUpdated) {
                                JOptionPane.showMessageDialog(panel1, "Thanks! Your account has been updated!");
                            } else {
                                JOptionPane.showMessageDialog(panel1, "User account update failed.");
                            }
                        } else {
                            JOptionPane.showMessageDialog(panel1, "User update failed.");
                        }

                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(panel1, "An error occurred while updating your account.");
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });


            //resize all elements
            Font headerFont = new Font("Helvetica", Font.BOLD, 20);
            Font mainFont = new Font("Verdana", Font.PLAIN, 15);
            Font comboFont = new Font("Verdana", Font.PLAIN, 12);
            Font boldFont = new Font("Verdana", Font.BOLD, 15);
            lblDirections.setFont(headerFont);
            lblFirst.setFont(mainFont);
            lblLast.setFont(mainFont);
            lblBirth.setFont(mainFont);
            lblGender.setFont(mainFont);
            lblHeightFeet.setFont(mainFont);
            lblHeightInches.setFont(mainFont);
            lblWeight.setFont(mainFont);
            lblBodyType.setFont(mainFont);
            lblExperience.setFont(mainFont);
            lblActivity.setFont(mainFont);
            lblPrimary.setFont(mainFont);
            lblEmail.setFont(mainFont);
            lblPassword.setFont(mainFont);
            lblNickname.setFont(mainFont);
            lblPhoneNumber.setFont(mainFont);
            lblEmailSecond.setFont(mainFont);

            btnUpdateAccount.setFont(boldFont);

            txtFirst.setFont(mainFont);
            txtLast.setFont(mainFont);
            txtBirth.setFont(mainFont);
            txtGender.setFont(mainFont);
            txtHeightFeet.setFont(mainFont);
            txtHeightInches.setFont(mainFont);
            txtWeight.setFont(mainFont);
            txtEmail.setFont(mainFont);
            txtPassword.setFont(mainFont);
            txtNickname.setFont(mainFont);
            txtPhoneNumber.setFont(mainFont);
            txtEmailSecond.setFont(mainFont);
            comboBodyType.setFont(comboFont);
            comboExperience.setFont(comboFont);
            comboActivity.setFont(comboFont);
            comboPrimary.setFont(comboFont);

            // Top panel with null layout for the back button
            topPanel = new JPanel(null); // Null layout
            topPanel.setBackground(Color.WHITE);
            topPanel.setPreferredSize(new Dimension(60, 60)); // Fixed height
            btnBack.setBounds(10, 10, 50, 50); // Positioning manually
            topPanel.add(btnBack);

            //set up panel
            panel1 = new JPanel(new GridBagLayout());
            panel1.setBackground(Color.LIGHT_GRAY);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 4; // Span both columns
            gbc.anchor = GridBagConstraints.CENTER;
            panel1.add(lblDirections, gbc);
            gbc.gridwidth = 1; // Reset grid width

            // Reset to left alignment
            gbc.anchor = GridBagConstraints.WEST;

            // First Name
            gbc.gridx = 0;
            gbc.gridy++;
            panel1.add(lblFirst, gbc);
            gbc.gridx = 1;
            panel1.add(txtFirst, gbc);

            //Email
            gbc.gridx = 2;
            panel1.add(lblEmail, gbc);
            gbc.gridx = 3;
            panel1.add(txtEmail, gbc);

            // Last Name
            gbc.gridx = 0;
            gbc.gridy++;
            panel1.add(lblLast, gbc);
            gbc.gridx = 1;
            panel1.add(txtLast, gbc);

            //Password
            gbc.gridx = 2;
            panel1.add(lblPassword, gbc);
            gbc.gridx = 3;
            panel1.add(txtPassword, gbc);

            // Birth Date
            gbc.gridx = 0;
            gbc.gridy++;
            panel1.add(lblBirth, gbc);
            gbc.gridx = 1;
            panel1.add(txtBirth, gbc);

            //Nickname
            gbc.gridx = 2;
            panel1.add(lblNickname, gbc);
            gbc.gridx = 3;
            panel1.add(txtNickname, gbc);

            // Gender
            gbc.gridx = 0;
            gbc.gridy++;
            panel1.add(lblGender, gbc);
            gbc.gridx = 1;
            panel1.add(txtGender, gbc);

            //Phone Number
            gbc.gridx = 2;
            panel1.add(lblPhoneNumber, gbc);
            gbc.gridx = 3;
            panel1.add(txtPhoneNumber, gbc);

            // Height Feet
            gbc.gridx = 0;
            gbc.gridy++;
            panel1.add(lblHeightFeet, gbc);
            gbc.gridx = 1;
            panel1.add(txtHeightFeet, gbc);

            //Secondary Email
            gbc.gridx = 2;
            panel1.add(lblEmailSecond, gbc);
            gbc.gridx = 3;
            panel1.add(txtEmailSecond, gbc);

            // Height Inches
            gbc.gridx = 0;
            gbc.gridy++;
            panel1.add(lblHeightInches, gbc);
            gbc.gridx = 1;
            panel1.add(txtHeightInches, gbc);

            // Weight
            gbc.gridx = 0;
            gbc.gridy++;
            panel1.add(lblWeight, gbc);
            gbc.gridx = 1;
            panel1.add(txtWeight, gbc);

            // Body Type
            gbc.gridx = 0;
            gbc.gridy++;
            panel1.add(lblBodyType, gbc);
            gbc.gridx = 1;
            panel1.add(comboBodyType, gbc);

            // Experience
            gbc.gridx = 0;
            gbc.gridy++;
            panel1.add(lblExperience, gbc);
            gbc.gridx = 1;
            panel1.add(comboExperience, gbc);

            // Activity
            gbc.gridx = 0;
            gbc.gridy++;
            panel1.add(lblActivity, gbc);
            gbc.gridx = 1;
            panel1.add(comboActivity, gbc);

            // Primary Goal
            gbc.gridx = 0;
            gbc.gridy++;
            panel1.add(lblPrimary, gbc);
            gbc.gridx = 1;
            panel1.add(comboPrimary, gbc);

            // Button
            gbc.gridx = 0;
            gbc.gridy++;
            gbc.gridwidth = 2; // Center button under both columns
            gbc.anchor = GridBagConstraints.CENTER;
            panel1.add(btnUpdateAccount, gbc);

            //setup JFrame
            JFrame frame = new JFrame("Create Account");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1500, 1000);

            // Maximize the window to full-screen size, keeping window decorations
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setResizable(true);  // Allow window resizing if necessary
            frame.setLocationRelativeTo(null);  // Center the window

            // Set layout to BorderLayout and add components
            frame.setLayout(new BorderLayout());
            frame.add(topPanel, BorderLayout.NORTH);
            frame.add(panel1, BorderLayout.CENTER);
            frame.setVisible(true);
        }
    }
