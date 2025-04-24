package org.aimacrosapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HelpGUI extends JFrame {

    private JButton btnBack, btnLogout;

    public HelpGUI() {
        // back button
        ImageIcon backIcon = new ImageIcon(getClass().getResource("/back_arrow.png"));
        btnBack = new JButton(backIcon);
        btnBack.setBorderPainted(false);
        btnBack.setContentAreaFilled(false);
        btnBack.setFocusPainted(false);
        btnBack.setBounds(10, 10, 50, 50);

        // Log Out
        ImageIcon logoutIcon = new ImageIcon(getClass().getResource("/logout.png"));
        btnLogout = new JButton(logoutIcon);
        btnLogout.setBorderPainted(false);
        btnLogout.setContentAreaFilled(false);

        ToolTipManager.sharedInstance().setInitialDelay(100);
        btnBack.setToolTipText("Back");
        btnLogout.setToolTipText("Logout");

        // top panel
        JPanel topPanel = new JPanel(null);
        // Top panel for back button (left) and logout button (right)
        topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setPreferredSize(new Dimension(60, 60));

        btnBack.setPreferredSize(new Dimension(50, 50));
        btnLogout.setPreferredSize(new Dimension(100, 50)); // adjust size as needed

        topPanel.add(btnBack, BorderLayout.WEST);    // Puts back button on the far left
        topPanel.add(btnLogout, BorderLayout.EAST);  // Puts logout button on the far right

        // fonts
        Font headerFont = new Font("Helvetica", Font.BOLD, 22);
        Font mainFont = new Font("Verdana", Font.PLAIN, 15);

        // left content
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.LIGHT_GRAY);
        JLabel headerLbl1 = new JLabel("What's the App's Purpose?");
        headerLbl1.setFont(headerFont);
        headerLbl1.setHorizontalAlignment(SwingConstants.CENTER);
        headerLbl1.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        //text area for app purpose
        JTextArea textArea1 = new JTextArea("Wondering where to start? The Macros Tracker with AI Assistant is an application created with the purpose of helping YOU " +
                "improve your overall fitness and customize your goals! Just create an account if you haven't, sign in, and you will be brought to the main dashboard. " +
                "You will be able to do the following:\n\n" +
                "• Personalize your profile and fitness goals.\n" +
                "• Set your daily goals for macros amounts (calories, carbs, protein and fats.)\n" +
                "• Enter your daily macros amounts and log your history.\n" +
                "• Interact with Joe, your AI fitness assistant.\n\n" +
                "Utilizing all of these features ensures you have the best experience possible when using the app. If you would like to improve heart health, " +
                "lose weight, gain muscle, or have any other fitness goal, you can easily plan, set goals, and track goals this way. You can be sure that our " +
                "advanced AI chatbot coach will help you to plan out your meals, exercises, and macros goals. Then, enter your goals, track them, and add them up " +
                "using your your AI health coach or calculated on your own. The purpose of this app is to make all of these things simple, easy, and effective!\n\n" +
                "If you have any further questions, contact us! We'd be happy to help!\n\n" +
                "Contact Email: fitness-freaks@gmail.com\n\n" +
                "For specific questions you have about the app, you can head back to the dashboard and access your AI trainer, Joe, for real-time support!");
        textArea1.setLineWrap(true);
        textArea1.setWrapStyleWord(true);
        textArea1.setEditable(false);
        textArea1.setOpaque(false);
        textArea1.setFocusable(false);
        textArea1.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        textArea1.setFont(mainFont);
        textArea1.setPreferredSize(new Dimension(500, 200));

        //make pane1 scrollable
        JScrollPane scrollPane1 = new JScrollPane(textArea1);
        scrollPane1.setOpaque(true);
        scrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        leftPanel.add(headerLbl1, BorderLayout.NORTH);
        leftPanel.add(scrollPane1, BorderLayout.CENTER);
        // right panel for directions
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.LIGHT_GRAY);
        JLabel headerLbl2 = new JLabel("Page-By-Page Guide:");
        headerLbl2.setFont(headerFont);
        headerLbl2.setHorizontalAlignment(SwingConstants.CENTER);
        headerLbl2.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        //second text area gives page-by-page guide
        JTextArea textArea2 = new JTextArea("Sign-In Page (Landing Page):\n\n" +
                "• Enter your username and password and click the 'Login' button to login and be sent to the main dashboard\n" +
                "• Click the 'Sign Up' if you do not yet have an account\n" +
                "• Click the 'Forgot Password' button if you need to reset your password or if you forgot your login\n\n" +
                "Dashboard Page (Main Page):\n\n" +
                "• Hover over the top icons to see the page names. Click it, and you will be sent to that page. (Account/Settings, Goals, Goal History, and Help)\n" +
                "• Below the icons, view the on-screen stats on the left and right for your set daily goals (left) and today's progress (right)\n" +
                "• Below the stats, click on the icon of a man to access Joe, your AI fitness coach who will aid you\n" +
                "• There is a copy info button at the bottom of the dashboard that can be used to copy the profile information that will be helpful for your chatbot to aid you!\n\n" +
                "AI Coach Chatbot (External Link):\n\n" +
                "• After clicking on the image of 'Joe', you will be sent to an external web page with your AI chatbot\n" +
                "• You can start by providing Joe with your age, height, weight, body type, gender, fitness goals, and any other relevant information\n" +
                "• Use Joe to complete various tasks, such as asking health questions, planning meals and workouts, get help forming goals, and adding daily macros\n\n" +
                "Account/Settings Page (1st icon from the left on the dashboard):\n\n" +
                "• After clicking on the gear icon, you will be brought to the page where you can view and enter your information into the text boxes to edit your profile\n" +
                "• Click the 'Update' button to save the changes made in the text boxes\n" +
                "• Click the 'Forgot Password' button to be sent to the page to reset your password\n" +
                "• Click the 'Delete Account' button and enter your email and password if you need to delete your account\n\n" +
                "Goals Page (2nd icon from the left on the dashboard:)\n\n" +
                "• After clicking on the target icon, you will be brought to the page where you can set your workout and macros goals\n" +
                "• On the left side, you can enter your fitness goals and click 'Submit' to save changes\n" +
                "• On the right, you can enter today's macros and progress on your goals and click 'Update' to save changes\n\n" +
                "Goal History Page (3rd icon from the left on the dashboard:)\n\n" +
                "• After clicking on the list icon, you will be brought to the page where you can view all of your saved goal-related data from the past\n" +
                "• You can double click any field to edit it and click the 'Submit Changes' button to save your changes\n" +
                "• Click the top column names in order to sort by ascending or descending values\n" +
                "• You can select a row by clicking on it or multiple rows with Ctrl+Click and click the 'Delete Button' to delete those selected rows\n\n" +
                "Help Page (4th icon from the left on the dashboard):\n\n" +
                "• After clicking the question mark icon, you will be brought to this page, which details the app's purpose of usage and how to use the app in detail\n\n" +
                "Sign-Up Page (bottom left button on the Sign-In page):\n\n" +
                "• After clicking the 'Sign Up' button, you will be brought to the page where you can enter your account information to create a new account\n" +
                "• After using the text boxes and drop down boxes to enter your information, you can click the 'Create Account' button at the bottom left to register your new account\n\n" +
                "Forgot Password Page (bottom right button on the Sign-In page):\n\n" +
                "• After clicking the 'Forgot Password' button, you will be brought to the page where you can reset your password for an existing account\n" +
                "• Firstly, enter your email address and nickname you set for the account as a security question. Then click the 'Submit' button to verify\n" +
                "• Next, enter your new password and re-enter your new password. Click the 'Submit' button again to reset your password. Sign in once you are redirected\n\n" +
                "Back Button (every page):\n\n" +
                "• The back button appears at the top left corner of every page. Click it to go back to the home page (the sign in page if you're signed out, and the dashboard if you're signed in)\n\n" +
                "Logout Button (every page after login):\n\n" +
                "• The logout button appears at the top right corner of every page. Click it to log out of your session.");
        textArea2.setLineWrap(true);
        textArea2.setWrapStyleWord(true);
        textArea2.setEditable(false);
        textArea2.setOpaque(true);
        textArea2.setFocusable(false);
        textArea2.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        textArea2.setFont(mainFont);

        JScrollPane scrollPane2 = new JScrollPane(textArea2);
        scrollPane2.setOpaque(true);
        scrollPane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        rightPanel.add(headerLbl2, BorderLayout.NORTH);
        rightPanel.add(scrollPane2, BorderLayout.CENTER);

        // combine left and right panel
        JPanel contentPanel = new JPanel(new GridLayout(1, 2));
        contentPanel.add(leftPanel);
        contentPanel.add(rightPanel);

        // frame settings
        setTitle("Help");
        setIconImage(new ImageIcon(getClass().getResource("/app_icon.png")).getImage());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(topPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        setVisible(true);

        //back button click event
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //go back to Dashboard
                new DashboardGUI();
                //close current page
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(leftPanel);
                if (topFrame != null) {
                    topFrame.dispose();
                }
            }
        });

        //logout click event
        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int option = JOptionPane.showOptionDialog(
                        leftPanel,
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
                    JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(leftPanel);
                    if (topFrame != null) {
                        topFrame.dispose();
                    }
                } else if (option == 1 || option == JOptionPane.CLOSED_OPTION) {
                    //do nothing
                }
            }
        });
    }
}
