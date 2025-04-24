package org.aimacrosapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;


    public class AccountGUI extends JFrame {
        private JLabel lblDirections, lblReq, lblFirst, lblLast, lblBirth, lblGender, lblHeightFeet, lblHeightInches, lblWeight, lblBodyType, lblExperience, lblActivity,
                lblPrimary, lblEmail, lblPassword, lblNickname, lblPhoneNumber, lblEmailSecond;

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

        private JComboBox<String> comboGender, comboBodyType, comboExperience, comboActivity, comboPrimary;

        //back arrow for every page
        ImageIcon backIcon;
        private JButton btnBack, btnLogout, btnUpdateAccount, btnResetPassword, btnDeleteAccount;
        private JPanel panel1;
        private JPanel topPanel;

        public AccountGUI(){

            //initialize elements
            lblDirections = new JLabel("Here is your profile information. Feel free to edit whenever you want!");
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
            lblNickname = new JLabel("* Nickname:");
            lblPhoneNumber = new JLabel("* Phone Number:");
            lblEmailSecond = new JLabel("Secondary Email:");
            btnUpdateAccount = new JButton("Update Account");
            btnResetPassword = new JButton("Reset Password");
            btnDeleteAccount = new JButton("Delete Account");

            ToolTipManager.sharedInstance().setInitialDelay(100);
            txtPassword.setToolTipText("Passwords are hidden for security purposes. If you forgot your password, click 'Reset Password' below.");

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
                    comboGender.setSelectedItem(user.getGender());
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
            btnBack.setFocusPainted(false);

            // Log Out
            ImageIcon logoutIcon = new ImageIcon(getClass().getResource("/logout.png"));
            btnLogout = new JButton(logoutIcon);
            btnLogout.setBorderPainted(false);
            btnLogout.setContentAreaFilled(false);

            ToolTipManager.sharedInstance().setInitialDelay(100);
            btnBack.setToolTipText("Back");
            btnLogout.setToolTipText("Logout");

            //back button click event
            btnBack.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //go back to Dashboard
                    new DashboardGUI();
                    //close current page
                    JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(panel1);
                    if (topFrame != null) {
                        topFrame.dispose();
                    }
                }
            });

            btnLogout.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int option = JOptionPane.showOptionDialog(
                            panel1,
                            "Are you sure you want to log out?!",
                            "Message",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.INFORMATION_MESSAGE,
                            null,
                            new Object[]{"Yes", "No"},
                            "Yes"
                    );
                    if (option == 0) {
                        Session.clear();
                        new SignInGUI();
                        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(panel1);
                        if (topFrame != null) {
                            topFrame.dispose();
                        }
                    } else if (option == 1 || option == JOptionPane.CLOSED_OPTION) {
                        //do nothing
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
                    String gender = (String) comboGender.getSelectedItem();
                    String height_feet_str = txtHeightFeet.getText();
                    String height_inches_str = txtHeightInches.getText();
                    String weight_lbs_str = txtWeight.getText();
                    String body_type = (String) comboBodyType.getSelectedItem();
                    String experienceLevel = (String) comboExperience.getSelectedItem();
                    String activity_goal = (String) comboActivity.getSelectedItem();
                    String primary_goal = (String) comboPrimary.getSelectedItem();
                    String email = txtEmail.getText();
                    String password = new String(txtPassword.getPassword());
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
                    if (!height_feet_str.matches("^[1-9]$")) errors.append("- Height (feet) must be between 1-9\n");
                    if (!height_inches_str.matches("^(0|[1-9]|1[01])$")) errors.append("- Height (inches) must be 0-11\n");
                    if (!weight_lbs_str.matches("^[1-9][0-9]{1,2}$")) errors.append("- Weight must be between 10-999\n");
                    if (!email.matches("^[\\w.-]+@[\\w-]+\\.[a-zA-Z]{2,}$")) errors.append("- Email invalid. Use format: user@example.com\n");
                    if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z\\d]).{6,}$"))
                        errors.append("- Password must be 6+ characters with at least one uppercase, lowercase, number, and special character\n");
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

            //reset password click event
            btnResetPassword.addActionListener(new ActionListener() {
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

            btnDeleteAccount.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int confirm = JOptionPane.showConfirmDialog(
                            panel1,
                            "Are you sure you want to delete your account?\nThis deletion cannot be undone.",
                            "Confirm Account Deletion",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );

                    if (confirm == JOptionPane.YES_OPTION) {
                        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
                        JTextField txtConfirmEmail = new JTextField();
                        JPasswordField txtConfirmPassword = new JPasswordField();

                        inputPanel.add(new JLabel("Enter your email:"));
                        inputPanel.add(txtConfirmEmail);
                        inputPanel.add(new JLabel("Enter your password:"));
                        inputPanel.add(txtConfirmPassword);

                        int credentialsResult = JOptionPane.showConfirmDialog(
                                panel1,
                                inputPanel,
                                "Confirm Credentials",
                                JOptionPane.OK_CANCEL_OPTION,
                                JOptionPane.PLAIN_MESSAGE
                        );

                        if (credentialsResult == JOptionPane.OK_OPTION) {
                            String confirmEmail = txtConfirmEmail.getText().trim();
                            String confirmPassword = new String(txtConfirmPassword.getPassword()).trim();

                            try {
                                String currentEmail = Session.getEmail();
                                AccountLogic logic = new AccountLogic();
                                UserAccount account = logic.getUserAndAccountByEmail(currentEmail);

                                if (account == null) {
                                    JOptionPane.showMessageDialog(panel1,
                                            "Could not retrieve current account details.",
                                            "Error",
                                            JOptionPane.ERROR_MESSAGE);
                                    return;
                                }

                                boolean isEmailMatch = confirmEmail.equals(currentEmail);
                                boolean isPasswordMatch = logic.checkPassword(confirmPassword, account.getPassword());

                                if (isEmailMatch && isPasswordMatch) {
                                    boolean deleted = logic.deleteAccount(confirmEmail, confirmPassword);

                                    if (deleted) {
                                        JOptionPane.showMessageDialog(panel1,
                                                "Account deleted successfully. Goodbye!",
                                                "Success",
                                                JOptionPane.INFORMATION_MESSAGE);
                                        Session.clear();
                                        new SignInGUI();
                                        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(panel1);
                                        if (topFrame != null) topFrame.dispose();
                                    } else {
                                        JOptionPane.showMessageDialog(panel1,
                                                "Account deletion failed. Please try again later.",
                                                "Deletion Failed",
                                                JOptionPane.ERROR_MESSAGE);
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(panel1,
                                            "The email or password you entered does not match your current account.",
                                            "Invalid Credentials",
                                            JOptionPane.ERROR_MESSAGE);
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                JOptionPane.showMessageDialog(panel1,
                                        "An error occurred while verifying credentials.",
                                        "Error",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
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
            lblNickname.setFont(mainFont);
            lblPhoneNumber.setFont(mainFont);
            lblEmailSecond.setFont(mainFont);

            btnUpdateAccount.setFont(boldFont);
            btnResetPassword.setFont(boldFont);
            btnDeleteAccount.setFont(boldFont);

            txtFirst.setFont(mainFont);
            txtLast.setFont(mainFont);
            txtBirth.setFont(mainFont);
            comboGender.setFont(comboFont);
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

            // Top panel for back button (left) and logout button (right)
            topPanel = new JPanel(new BorderLayout());
            topPanel.setBackground(Color.WHITE);
            topPanel.setPreferredSize(new Dimension(60, 60));

            btnBack.setPreferredSize(new Dimension(50, 50));
            btnLogout.setPreferredSize(new Dimension(100, 50)); // adjust size as needed

            topPanel.add(btnBack, BorderLayout.WEST);    // Puts back button on the far left
            topPanel.add(btnLogout, BorderLayout.EAST);  // Puts logout button on the far right

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
            panel1.add(comboGender, gbc);

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

            // Button for Updating Account
            gbc.gridx = 0;
            gbc.gridy++;
            gbc.gridwidth = 2; // Center button under both columns
            gbc.anchor = GridBagConstraints.WEST;
            panel1.add(btnUpdateAccount, gbc);

            // Button for Resetting Password
            gbc.gridx = 1;
            gbc.anchor = GridBagConstraints.EAST;
            gbc.insets = new Insets(10, 200, 10, 10); // top, left, bottom, right
            panel1.add(btnResetPassword, gbc);

            gbc.gridx = 2;
            gbc.anchor = GridBagConstraints.EAST;
            gbc.insets = new Insets(10, 200, 10, 10);
            panel1.add(btnDeleteAccount, gbc);


            //setup JFrame
            JFrame frame = new JFrame("Account");
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
