package org.aimacrosapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import org.mindrot.jbcrypt.BCrypt;

//IF YOU HAVE TIME, ADD SECURITY QUESTIONS

public class CreateAccountGUI extends JFrame {
    private JLabel lblDirections, lblReq, lblFirst, lblLast, lblBirth, lblGender, lblHeightFeet, lblHeightInches, lblWeight, lblBodyType, lblExperience, lblActivity,
            lblPrimary, lblEmail, lblPassword, lblConfirmPassword, lblNickname, lblPhoneNumber, lblEmailSecond;

    private JTextField txtFirst = new JTextField(15),
            txtLast = new JTextField(15),
            txtBirth = new JTextField(15),
            txtHeightFeet = new JTextField(15),
            txtHeightInches = new JTextField(15),
            txtWeight = new JTextField(15),
            txtEmail = new JTextField(15),
            txtNickname = new JTextField(15),
            txtPhoneNumber = new JTextField(15),
            txtEmailSecond = new JTextField(15);

    private JPasswordField txtPassword = new JPasswordField(15);
    private JPasswordField txtConfirmPassword = new JPasswordField(15);

    private JComboBox<String> comboGender, comboBodyType, comboExperience, comboActivity, comboPrimary;

    //back arrow for every page
    ImageIcon backIcon;
    private JButton btnBack, btnCreateAccount;
    private JPanel panel1;
    private JPanel topPanel;

        public CreateAccountGUI(){
            //initialize elements
            lblDirections = new JLabel("Please enter your personal and fitness info below and then click 'Create Account' when finished!");
            lblReq = new JLabel("* Required Fields");
            lblFirst = new JLabel("* First Name:");
            lblLast = new JLabel("* Last Name:");
            lblBirth = new JLabel("* Birth Date:");
            lblGender = new JLabel("* Gender:");
            lblHeightFeet = new JLabel("* Height (Feet):");
            lblHeightInches = new JLabel("* Height (Inches):");
            lblWeight = new JLabel("* Weight (lbs):");
            lblBodyType = new JLabel("Body Type:");
            lblExperience = new JLabel("Experience Level:");
            lblActivity = new JLabel("Activity Level:");
            lblPrimary = new JLabel("Primary Goal:");
            lblEmail = new JLabel("* Email:");
            lblPassword = new JLabel("* Password:");
            lblConfirmPassword = new JLabel("* Confirm Password:");
            lblNickname = new JLabel("* Nickname:");
            lblPhoneNumber = new JLabel("* Phone Number:");
            lblEmailSecond = new JLabel("Secondary Email:");
            btnCreateAccount = new JButton("Create Account");

            String[] comboOptionsGender = {"", "Male", "Female"};
            String[] comboOptionsBodyType = {"", "Slim", "Standard", "Athletic", "Muscular", "Bodybuilder", "Heavyset", "Obese"};
            String[] comboOptionsExperience = {"", "Beginner", "Intermediate", "Advanced"};
            String[] comboOptionsActivity = {"", "Sedentary (0-1 days a week)", "Light (1-3 days a week)", "Moderate (3-5 days a week)", "Extreme (5-7 days a week)"};
            String[] comboOptionsPrimary = {"", "Improve Overall Fitness", "Improve Heart Health", "Improve Conditioning", "Physical Therapy", "Lose Weight", "Lose Weight (Rapid)", "Gain Weight", "Gain Weight (Rapid)", "Gain Muscle (Lean)", "Gain Muscle (Bulk)"};

            comboGender = new JComboBox<>(comboOptionsGender);
            comboBodyType = new JComboBox<>(comboOptionsBodyType);
            comboExperience = new JComboBox<>(comboOptionsExperience);
            comboActivity = new JComboBox<>(comboOptionsActivity);
            comboPrimary = new JComboBox<>(comboOptionsPrimary);

            comboGender.setPreferredSize(new Dimension(180, 25));
            comboBodyType.setPreferredSize(new Dimension(180, 25));
            comboExperience.setPreferredSize(new Dimension(180, 25));
            comboActivity.setPreferredSize(new Dimension(180, 25));
            comboPrimary.setPreferredSize(new Dimension(180, 25));

            //back button image logic
            ImageIcon backIcon = new ImageIcon(getClass().getResource("/back_arrow.png"));
            btnBack = new JButton(backIcon);
            btnBack.setBorderPainted(false);
            btnBack.setContentAreaFilled(false);

            ToolTipManager.sharedInstance().setInitialDelay(100);
            btnBack.setToolTipText("Back");

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
            btnCreateAccount.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String first_name = txtFirst.getText();
                    String last_name = txtLast.getText();
                    String birth_date = txtBirth.getText();
                    String gender = (String) comboGender.getSelectedItem();
                    String height_feet = txtHeightFeet.getText();
                    String height_inches = txtHeightInches.getText();
                    String weight_lbs = txtWeight.getText();
                    String body_type = (String) comboBodyType.getSelectedItem();
                    String experienceLevel = (String) comboExperience.getSelectedItem();
                    String activity_goal = (String) comboActivity.getSelectedItem();
                    String primary_goal = (String) comboPrimary.getSelectedItem();
                    String email = txtEmail.getText().trim();
                    String password = new String(txtPassword.getPassword());
                    String passwordConfirm = new String(txtConfirmPassword.getPassword());
                    String nickname = txtNickname.getText();
                    String phone_number = txtPhoneNumber.getText();
                    String email_second = txtEmailSecond.getText();

                    StringBuilder errors = new StringBuilder();

                    // === VALIDATION SECTION ===
                    if (!first_name.matches("^[a-zA-Z\\-]{1,35}$")) errors.append("- Invalid first name\n");
                    if (!last_name.matches("^[a-zA-Z\\-]{1,35}$")) errors.append("- Invalid last name\n");
                    if (!birth_date.matches("^(\\d{4}/\\d{2}/\\d{2})|(\\d{2}/\\d{2}/\\d{4})|(\\d{2}-\\d{2}-\\d{4})|(\\d{4}-\\d{2}-\\d{2})$")) {
                        errors.append("- Invalid birth date format (use YYYY/MM/DD, MM/DD/YYYY, MM-DD-YYYY, or YYYY-MM-DD)\n");
                    }
                    if (gender == null || gender.trim().isEmpty()) errors.append("- Gender is required\n");
                    if (!height_feet.matches("^[1-9]$")) errors.append("- Height (feet) must be between 1-9\n");
                    if (!height_inches.matches("^(0|[1-9]|1[01])$")) errors.append("- Height (inches) must be 0-11\n");
                    if (!weight_lbs.matches("^[1-9][0-9]{1,2}$")) errors.append("- Weight must be between 10-999\n");
                    if (!email.matches("^[\\w.-]+@[\\w-]+\\.[a-zA-Z]{2,}$")) errors.append("- Email invalid. Use format: user@example.com\n");
                    if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z\\d]).{6,}$"))
                        errors.append("- Password must be 6+ characters with at least one uppercase, lowercase, number, and special character\n");
                    if (!password.equals(passwordConfirm)) errors.append("- Passwords do not match! Please re-enter password and the confirmed password\n");
                    if (!nickname.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$"))
                        errors.append("- Nickname must be at least 6 characters and include letters and numbers\n");
                    if (!phone_number.matches("^\\(?\\d{3}\\)?[-.\\s]?\\d{3}[-.\\s]?\\d{4}$"))
                        errors.append("- Invalid phone number format (Use a format like: 111-111-1111 OR 1111111111)\n");
                    if (!email_second.isEmpty() && !email_second.matches("^[\\w.-]+@[\\w-]+\\.[a-zA-Z]{2,}$"))
                        errors.append("- Secondary email is not valid. Use format: user@example.com\n");

                    if (errors.length() > 0) {
                        JOptionPane.showMessageDialog(panel1, "Please fix the following issues:\n\n" + errors.toString(), "Validation Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Create objects
                    User user = new User(first_name, last_name, birth_date, gender,
                            Integer.parseInt(height_feet), Integer.parseInt(height_inches),
                            Integer.parseInt(weight_lbs), body_type, experienceLevel, activity_goal, primary_goal);

                    UserAccount userAccount = new UserAccount(user, email, password, nickname, phone_number, email_second);
                    AccountLogic accountLogic = new AccountLogic();

                    try {
                        // Step 1: Sign up with Supabase Auth and get user_id + token
                        String userId = accountLogic.signUpUser(email, password);

                        if (userId != null) {
                            // Step 2: Insert into users table using userId
                            String userTableId = accountLogic.createUser(
                                    userId,
                                    user.getFirst_name(),
                                    user.getLast_name(),
                                    user.getBirth_date(),
                                    user.getGender(),
                                    user.getHeight_feet(),
                                    user.getHeight_inches(),
                                    user.getWeight_lbs(),
                                    user.getBody_type(),
                                    user.getExperience_level(),
                                    user.getActivity_level(),
                                    user.getPrimary_goal()
                            );

                            // Step 3: Insert into user_account table
                            if (userTableId != null) {
                                boolean userAccountCreated = accountLogic.createUserAccount(
                                        email,
                                        password,
                                        nickname,
                                        phone_number,
                                        email_second,
                                        userTableId
                                );

                                if (userAccountCreated) {
                                    JOptionPane.showMessageDialog(panel1, "Thanks! Your account has been created!");
                                    new SignInGUI();
                                    JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(panel1);
                                    if (topFrame != null) topFrame.dispose();
                                    return;
                                }
                            }
                        }

                        JOptionPane.showMessageDialog(panel1, "Account creation failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(panel1, "An error occurred. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            //resize all elements
            Font headerFont = new Font("Helvetica", Font.BOLD, 20);
            Font mainFont = new Font("Verdana", Font.PLAIN, 15);
            Font comboFont = new Font("Verdana", Font.PLAIN, 12);
            Font boldFont = new Font("Verdana", Font.BOLD, 15);
            Font italicFont = new Font("Verdana", Font.ITALIC, 12);
            lblDirections.setFont(headerFont);
            lblReq.setFont(italicFont);
            lblReq.setForeground(Color.RED);
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
            lblConfirmPassword.setFont(mainFont);
            lblNickname.setFont(mainFont);
            lblPhoneNumber.setFont(mainFont);
            lblEmailSecond.setFont(mainFont);

            btnCreateAccount.setFont(boldFont);

            txtFirst.setFont(mainFont);
            txtLast.setFont(mainFont);
            txtBirth.setFont(mainFont);
            comboGender.setFont(mainFont);
            txtHeightFeet.setFont(mainFont);
            txtHeightInches.setFont(mainFont);
            txtWeight.setFont(mainFont);
            txtEmail.setFont(mainFont);
            txtPassword.setFont(mainFont);
            txtConfirmPassword.setFont(mainFont);
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

            gbc.gridy = 1;
            gbc.anchor = GridBagConstraints.WEST;
            panel1.add(lblReq, gbc);

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

            //Confirm Password
            gbc.gridx = 2;
            panel1.add(lblConfirmPassword, gbc);
            gbc.gridx = 3;
            panel1.add(txtConfirmPassword, gbc);

            // Gender
            gbc.gridx = 0;
            gbc.gridy++;
            panel1.add(lblGender, gbc);
            gbc.gridx = 1;
            panel1.add(comboGender, gbc);

            //Nickname
            gbc.gridx = 2;
            panel1.add(lblNickname, gbc);
            gbc.gridx = 3;
            panel1.add(txtNickname, gbc);

            // Height Feet
            gbc.gridx = 0;
            gbc.gridy++;
            panel1.add(lblHeightFeet, gbc);
            gbc.gridx = 1;
            panel1.add(txtHeightFeet, gbc);

            //Phone Number
            gbc.gridx = 2;
            panel1.add(lblPhoneNumber, gbc);
            gbc.gridx = 3;
            panel1.add(txtPhoneNumber, gbc);

            // Height Inches
            gbc.gridx = 0;
            gbc.gridy++;
            panel1.add(lblHeightInches, gbc);
            gbc.gridx = 1;
            panel1.add(txtHeightInches, gbc);

            //Secondary Email
            gbc.gridx = 2;
            panel1.add(lblEmailSecond, gbc);
            gbc.gridx = 3;
            panel1.add(txtEmailSecond, gbc);

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
            panel1.add(btnCreateAccount, gbc);

            //setup JFrame
            JFrame frame = new JFrame("Create Account");
            frame.setIconImage(new ImageIcon(getClass().getResource("/app_icon.png")).getImage());
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
