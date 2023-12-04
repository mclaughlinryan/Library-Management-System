import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.Enumeration;

// Creation of the graphical user interface for a library book management system
// Allows users to check out books for a certain period of time and administrators to manage book checkouts
public class LibraryManagementSystem extends JFrame {
    // Graphical user interface components and class variables
    // Main panel with a card layout
    private final JPanel application;
    private final CardLayout cardLayout;

    // Panel for searching for library books
    private final JPanel searchPanel;
    private final JLabel startLabel1;
    private final JLabel startLabel2;
    private final SimpleDateFormat dateFormat1;
    private final SimpleDateFormat dateFormat2;
    private final Date date;
    private final JLabel dateLabel1;
    private final Calendar calendar;
    private final JButton adminLoginButton1;
    private final JButton adminSignOutButton1;
    private final JButton exitButton1;
    private final JTextField searchField;
    private final JComboBox<String> attributeComboBox;
    private static final String[] bookAttributes = {"Title", "Author", "Author First Name", "Author Last Name", "Genre"};
    private final Box searchCriteriaBox;
    private final JButton searchButton;
    private final Box searchBoxLeft;
    private final Box searchBoxRight;
    private final Box searchBox;

    // Database search components
    private Connection connection;
    private boolean connectedToDatabase = false;
    private Statement statement;
    private ResultSet bookResults;
    private static SearchResultsTableModel searchTableModel;
    private static JTable searchResultsTable;
    private static JScrollPane searchResultsScrollPane;

    // Library book checkout panel
    private final JPanel checkoutPanel;
    private final JLabel checkoutLabel;
    private final JLabel dateLabel2;
    private final JButton returnToSearchButton1;
    private final JButton adminLoginButton2;
    private final JButton adminSignOutButton2;
    private final JButton exitButton2;
    private final JLabel checkoutBookTitleLabel;
    private final JLabel checkoutBookAuthorLabel;
    private final JLabel checkoutTimeErrorLabel;
    private final JRadioButton checkoutOneWeekRadioButton;
    private final JRadioButton checkoutTwoWeeksRadioButton;
    private final ButtonGroup checkoutRadioButtonGroup;
    private final JButton checkoutButton;
    private final Box checkoutBoxLeft;
    private final Box checkoutBoxRight;
    private final Box checkoutBox;

    // View book information panel
    private final JPanel viewPanel;
    private final JLabel viewLabel;
    private final JLabel dateLabel3;
    private final JLabel viewBookTitleLabel;
    private final JLabel viewBookAuthorLabel;
    private final JLabel viewCheckoutDateLabel;
    private final JLabel viewCheckoutTimeLabel;
    private final JLabel viewCheckoutAvailableDateLabel;
    private final JButton returnToSearchButton2;
    private final JButton adminLoginButton3;
    private final JButton adminSignOutButton3;
    private final JButton exitButton3;
    private final Box viewBoxLeft;
    private final Box viewBoxRight;
    private final Box viewBox;

    // Administrator login panel
    private final JPanel adminLoginPanel;
    private final JLabel adminLoginLabel1;
    private final JLabel adminLoginLabel2;
    private final JLabel dateLabel4;
    private final JButton returnToSearchButton3;
    private final JButton exitButton4;
    private final JLabel adminLoginErrorLabel;
    private final JLabel adminUsernameLabel;
    private final JTextField adminUsernameField;
    private final JLabel adminPasswordLabel;
    private final JPasswordField adminPasswordField;
    private final JButton loginAdminButton;
    private final Box adminLoginBoxLeft;
    private final Box adminLoginBoxRight;
    private final Box adminLoginBox;
    private boolean adminLogin = false;
    private TableColumnModel searchTableColumnModel;
    private TableColumn adminTableColumn1;
    private TableColumn adminTableColumn2;
    private TableColumn adminTableColumn3;
    private TableColumn adminTableColumn4;
    private TableColumn adminTableColumn5;
    private boolean initialSearch = true;

    // Administrator edit book checkout information panel
    private final JPanel editPanel;
    private final JLabel editLabel;
    private final JLabel dateLabel5;
    private final JLabel bookTitleLabel;
    private final JLabel bookAuthorLabel;
    private final JLabel editCheckoutDateLabel;
    private final JLabel editCheckoutDateErrorLabel;
    private final JLabel editCheckoutTimeLabel;
    private final JLabel editCheckoutTimeErrorLabel1;
    private final JLabel editCheckoutTimeErrorLabel2;
    private final JButton returnToSearchButton4;
    private final JButton adminSignOutButton4;
    private final JButton exitButton5;
    private final JButton clearCheckoutButton;
    private final JButton clearCheckoutTimeSelectionButton;
    private final JButton saveButton;
    private final JTextField editCheckoutDateField;
    private final JRadioButton editCheckoutOneWeekRadioButton;
    private final JRadioButton editCheckoutTwoWeeksRadioButton;
    private final ButtonGroup editCheckoutRadioButtonGroup;
    private final Box editBoxLeft;
    private final Box editBoxRight;
    private final Box editBox;

    // Constructor for the library book management system which creates the graphical user interface
    public LibraryManagementSystem() {
        super("Library Management System");

        setAutoRequestFocus(false);

        // Initialization of the main application panel and card layout
        application = new JPanel();
        cardLayout = new CardLayout();
        application.setLayout(cardLayout);
        application.requestFocus(false);

        // Initialization of the library book search panel
        searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS));
        searchPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Initialization of the library book search panel components
        startLabel1 = new JLabel("Welcome to The Public Library.");
        startLabel1.setAlignmentX(Component.LEFT_ALIGNMENT);
        dateFormat1 = new SimpleDateFormat("MM/dd/yyyy");
        dateFormat2 = new SimpleDateFormat("dd-MMM-yy");
        date = new Date();
        dateLabel1 = new JLabel(dateFormat1.format(date));
        dateLabel1.setAlignmentX(Component.RIGHT_ALIGNMENT);
        calendar = Calendar.getInstance();
        startLabel2 = new JLabel("Explore our collection of books by searching by Title, Author, or Genre.");
        startLabel2.setAlignmentX(Component.LEFT_ALIGNMENT);
        adminLoginButton1 = new JButton("Admin Login");
        ButtonHandler adminLoginButtonHandler1 = new ButtonHandler();
        adminLoginButton1.addActionListener(adminLoginButtonHandler1);
        adminLoginButton1.setAlignmentX(Component.RIGHT_ALIGNMENT);
        adminSignOutButton1 = new JButton("Sign Out");
        ButtonHandler adminSignOutButtonHandler1 = new ButtonHandler();
        adminSignOutButton1.addActionListener(adminSignOutButtonHandler1);
        adminSignOutButton1.setVisible(false);
        adminSignOutButton1.setAlignmentX(Component.RIGHT_ALIGNMENT);
        exitButton1 = new JButton("Exit");
        ButtonHandler exitButtonHandler1 = new ButtonHandler();
        exitButton1.addActionListener(exitButtonHandler1);
        exitButton1.setAlignmentX(Component.RIGHT_ALIGNMENT);
        searchField = new JTextField(35);
        searchField.setMaximumSize(searchField.getPreferredSize());
        attributeComboBox = new JComboBox<String>(bookAttributes);
        attributeComboBox.setMaximumSize(new Dimension(200,30));
        searchCriteriaBox = Box.createHorizontalBox();
        searchCriteriaBox.add(searchField);
        searchCriteriaBox.add(attributeComboBox);
        searchCriteriaBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        searchButton = new JButton("Search");
        ButtonHandler searchButtonHandler = new ButtonHandler();
        searchButton.addActionListener(searchButtonHandler);
        searchButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        searchTableModel = new SearchResultsTableModel();
        searchResultsTable = new JTable(searchTableModel);
        searchResultsScrollPane = new JScrollPane(searchResultsTable);
        searchResultsScrollPane.setVisible(false);

        // Organization of the components for the search panel
        searchBoxLeft = Box.createVerticalBox();
        searchBoxLeft.add(startLabel1);
        searchBoxLeft.add(startLabel2);
        searchBoxLeft.add(searchCriteriaBox);
        searchBoxLeft.add(searchButton);
        searchBoxRight = Box.createVerticalBox();
        searchBoxRight.add(dateLabel1);
        searchBoxRight.add(adminLoginButton1);
        searchBoxRight.add(adminSignOutButton1);
        searchBoxRight.add(exitButton1);
        searchBoxRight.add(Box.createVerticalStrut(15));

        // Adding the components to the search panel
        searchBox = Box.createHorizontalBox();
        searchBox.add(searchBoxLeft);
        searchBox.add(Box.createHorizontalGlue());
        searchBox.add(searchBoxRight);
        searchPanel.add(searchBox);
        searchPanel.add(searchResultsScrollPane);

        // Initialization of the book checkout panel
        checkoutPanel = new JPanel();
        checkoutPanel.setLayout(new BoxLayout(checkoutPanel, BoxLayout.Y_AXIS));
        checkoutPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Initialization of the book checkout panel components
        checkoutLabel = new JLabel("How long would you like to checkout this book for?");
        checkoutLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        dateLabel2 = new JLabel(dateFormat1.format(date));
        dateLabel2.setAlignmentX(Component.RIGHT_ALIGNMENT);
        returnToSearchButton1 = new JButton("Return To Search");
        ButtonHandler returnToSearchButtonHandler1 = new ButtonHandler();
        returnToSearchButton1.addActionListener(returnToSearchButtonHandler1);
        returnToSearchButton1.setAlignmentX(Component.RIGHT_ALIGNMENT);
        adminLoginButton2 = new JButton("Admin Login");
        ButtonHandler adminLoginButtonHandler2 = new ButtonHandler();
        adminLoginButton2.addActionListener(adminLoginButtonHandler2);
        adminLoginButton2.setAlignmentX(Component.RIGHT_ALIGNMENT);
        adminSignOutButton2 = new JButton("Sign Out");
        ButtonHandler adminSignOutButtonHandler2 = new ButtonHandler();
        adminSignOutButton2.addActionListener(adminSignOutButtonHandler2);
        adminSignOutButton2.setVisible(false);
        adminSignOutButton2.setAlignmentX(Component.RIGHT_ALIGNMENT);
        exitButton2 = new JButton("Exit");
        ButtonHandler exitButtonHandler2 = new ButtonHandler();
        exitButton2.addActionListener(exitButtonHandler2);
        exitButton2.setAlignmentX(Component.RIGHT_ALIGNMENT);
        checkoutBookTitleLabel = new JLabel();
        checkoutBookTitleLabel.setPreferredSize(new Dimension(800, 15));
        checkoutBookTitleLabel.setMaximumSize(checkoutBookTitleLabel.getPreferredSize());
        checkoutBookTitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        checkoutBookAuthorLabel = new JLabel();
        checkoutBookAuthorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        checkoutTimeErrorLabel = new JLabel("Please select the length of time you would like to check this book out for.");
        checkoutTimeErrorLabel.setVisible(false);
        checkoutTimeErrorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        checkoutOneWeekRadioButton = new JRadioButton("One Week");
        checkoutOneWeekRadioButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        checkoutTwoWeeksRadioButton = new JRadioButton("Two Weeks");
        checkoutTwoWeeksRadioButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        checkoutRadioButtonGroup = new ButtonGroup();
        checkoutRadioButtonGroup.add(checkoutOneWeekRadioButton);
        checkoutRadioButtonGroup.add(checkoutTwoWeeksRadioButton);
        checkoutButton = new JButton("Checkout");
        ButtonHandler checkoutButtonHandler = new ButtonHandler();
        checkoutButton.addActionListener(checkoutButtonHandler);
        checkoutButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Organization of the components for the book checkout panel
        checkoutBoxLeft = Box.createVerticalBox();
        checkoutBoxLeft.add(checkoutLabel);
        checkoutBoxLeft.add(checkoutBookTitleLabel);
        checkoutBoxLeft.add(checkoutBookAuthorLabel);
        checkoutBoxLeft.add(checkoutTimeErrorLabel);
        checkoutBoxLeft.add(checkoutOneWeekRadioButton);
        checkoutBoxLeft.add(checkoutTwoWeeksRadioButton);
        checkoutBoxLeft.add(checkoutButton);
        checkoutBoxLeft.add(Box.createVerticalGlue());
        checkoutBoxRight = Box.createVerticalBox();
        checkoutBoxRight.add(dateLabel2);
        checkoutBoxRight.add(returnToSearchButton1);
        checkoutBoxRight.add(adminLoginButton2);
        checkoutBoxRight.add(adminSignOutButton2);
        checkoutBoxRight.add(exitButton2);
        checkoutBoxRight.add(Box.createVerticalGlue());

        // Adding the components to the book checkout panel
        checkoutBox = Box.createHorizontalBox();
        checkoutBox.add(checkoutBoxLeft);
        checkoutBox.add(Box.createHorizontalGlue());
        checkoutBox.add(checkoutBoxRight);
        checkoutPanel.add(checkoutBox);

        // Initialization of the panel for viewing book checkout details
        viewPanel = new JPanel();
        viewPanel.setLayout(new BoxLayout(viewPanel, BoxLayout.Y_AXIS));
        viewPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Initialization of the book details components
        viewLabel = new JLabel("Book Checkout Details");
        viewLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        dateLabel3 = new JLabel(dateFormat1.format(date));
        dateLabel3.setAlignmentX(Component.RIGHT_ALIGNMENT);
        returnToSearchButton2 = new JButton("Return To Search");
        ButtonHandler returnToSearchButtonHandler2 = new ButtonHandler();
        returnToSearchButton2.addActionListener(returnToSearchButtonHandler2);
        returnToSearchButton2.setAlignmentX(Component.RIGHT_ALIGNMENT);
        adminLoginButton3 = new JButton("Admin Login");
        ButtonHandler adminLoginButtonHandler3 = new ButtonHandler();
        adminLoginButton3.addActionListener(adminLoginButtonHandler3);
        adminLoginButton3.setAlignmentX(Component.RIGHT_ALIGNMENT);
        adminSignOutButton3 = new JButton("Sign Out");
        ButtonHandler adminSignOutButtonHandler3 = new ButtonHandler();
        adminSignOutButton3.addActionListener(adminSignOutButtonHandler3);
        adminSignOutButton3.setVisible(false);
        adminSignOutButton3.setAlignmentX(Component.RIGHT_ALIGNMENT);
        exitButton3 = new JButton("Exit");
        ButtonHandler exitButtonHandler3 = new ButtonHandler();
        exitButton3.addActionListener(exitButtonHandler3);
        exitButton3.setAlignmentX(Component.RIGHT_ALIGNMENT);
        viewBookTitleLabel = new JLabel();
        viewBookTitleLabel.setPreferredSize(new Dimension(800, 15));
        viewBookTitleLabel.setMaximumSize(viewBookTitleLabel.getPreferredSize());
        viewBookTitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        viewBookAuthorLabel = new JLabel();
        viewBookAuthorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        viewCheckoutDateLabel = new JLabel();
        viewCheckoutDateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        viewCheckoutTimeLabel = new JLabel();
        viewCheckoutTimeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        viewCheckoutAvailableDateLabel = new JLabel();
        viewCheckoutAvailableDateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Organization of the components for the book details panel
        viewBoxLeft = Box.createVerticalBox();
        viewBoxLeft.add(viewLabel);
        viewBoxLeft.add(viewBookTitleLabel);
        viewBoxLeft.add(viewBookAuthorLabel);
        viewBoxLeft.add(viewCheckoutDateLabel);
        viewBoxLeft.add(viewCheckoutTimeLabel);
        viewBoxLeft.add(viewCheckoutAvailableDateLabel);
        viewBoxLeft.add(Box.createVerticalGlue());
        viewBoxRight = Box.createVerticalBox();
        viewBoxRight.add(dateLabel3);
        viewBoxRight.add(returnToSearchButton2);
        viewBoxRight.add(adminLoginButton3);
        viewBoxRight.add(adminSignOutButton3);
        viewBoxRight.add(exitButton3);
        viewBoxRight.add(Box.createVerticalGlue());

        // Adding the components to the book details panel
        viewBox = Box.createHorizontalBox();
        viewBox.add(viewBoxLeft);
        viewBox.add(Box.createHorizontalGlue());
        viewBox.add(viewBoxRight);
        viewPanel.add(viewBox);

        // Initialization of the admin login panel
        adminLoginPanel = new JPanel();
        adminLoginPanel.setLayout(new BoxLayout(adminLoginPanel, BoxLayout.Y_AXIS));
        adminLoginPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Initialization of admin login panel components
        adminLoginLabel1 = new JLabel("Admin Login");
        adminLoginLabel1.setAlignmentX(Component.LEFT_ALIGNMENT);
        dateLabel4 = new JLabel(dateFormat1.format(date));
        dateLabel4.setAlignmentX(Component.RIGHT_ALIGNMENT);
        adminLoginLabel2 = new JLabel("Log in to edit the library database and change book availability.");
        adminLoginLabel2.setAlignmentX(Component.LEFT_ALIGNMENT);
        returnToSearchButton3 = new JButton("Return To Search");
        ButtonHandler returnToSearchButtonHandler3 = new ButtonHandler();
        returnToSearchButton3.addActionListener(returnToSearchButtonHandler3);
        returnToSearchButton3.setAlignmentX(Component.RIGHT_ALIGNMENT);
        exitButton4 = new JButton("Exit");
        ButtonHandler exitButtonHandler4 = new ButtonHandler();
        exitButton4.addActionListener(exitButtonHandler4);
        exitButton4.setAlignmentX(Component.RIGHT_ALIGNMENT);
        adminLoginErrorLabel = new JLabel("Incorrect username and/or password. Please enter the correct username and password to log in as an administrator.");
        adminLoginErrorLabel.setVisible(false);
        adminLoginErrorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        adminUsernameLabel = new JLabel("Username");
        adminUsernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        adminUsernameField = new JTextField(20);
        adminUsernameField.setMaximumSize(adminUsernameField.getPreferredSize());
        adminUsernameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        adminPasswordLabel = new JLabel("Password");
        adminPasswordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        adminPasswordField = new JPasswordField(20);
        adminPasswordField.setMaximumSize(adminPasswordField.getPreferredSize());
        adminPasswordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginAdminButton = new JButton("Log In");
        ButtonHandler loginAdminButtonHandler = new ButtonHandler();
        loginAdminButton.addActionListener(loginAdminButtonHandler);
        loginAdminButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Organization of the admin login panel components
        adminLoginBoxLeft = Box.createVerticalBox();
        adminLoginBoxLeft.add(adminLoginLabel1);
        adminLoginBoxLeft.add(adminLoginLabel2);
        adminLoginBoxLeft.add(adminLoginErrorLabel);
        adminLoginBoxLeft.add(adminUsernameLabel);
        adminLoginBoxLeft.add(adminUsernameField);
        adminLoginBoxLeft.add(adminPasswordLabel);
        adminLoginBoxLeft.add(adminPasswordField);
        adminLoginBoxLeft.add(loginAdminButton);
        adminLoginBoxLeft.add(Box.createVerticalGlue());
        adminLoginBoxRight = Box.createVerticalBox();
        adminLoginBoxRight.add(dateLabel4);
        adminLoginBoxRight.add(returnToSearchButton3);
        adminLoginBoxRight.add(exitButton4);
        adminLoginBoxRight.add(Box.createVerticalGlue());

        // Adding the admin login components to the panel
        adminLoginBox = Box.createHorizontalBox();
        adminLoginBox.add(adminLoginBoxLeft);
        adminLoginBox.add(Box.createHorizontalGlue());
        adminLoginBox.add(adminLoginBoxRight);
        adminLoginPanel.add(adminLoginBox);

        // Initialization of the panel for editing book checkout details
        editPanel = new JPanel();
        editPanel.setLayout(new BoxLayout(editPanel, BoxLayout.Y_AXIS));
        editPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Initialization of the edit book checkout components
        editLabel = new JLabel("Edit the checkout for this book.");
        editLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        dateLabel5 = new JLabel(dateFormat1.format(date));
        dateLabel5.setAlignmentX(Component.RIGHT_ALIGNMENT);
        returnToSearchButton4 = new JButton("Return To Search");
        ButtonHandler returnToSearchButtonHandler4 = new ButtonHandler();
        returnToSearchButton4.addActionListener(returnToSearchButtonHandler4);
        returnToSearchButton4.setAlignmentX(Component.RIGHT_ALIGNMENT);
        adminSignOutButton4 = new JButton("Sign Out");
        ButtonHandler adminSignOutButtonHandler4 = new ButtonHandler();
        adminSignOutButton4.addActionListener(adminSignOutButtonHandler4);
        adminSignOutButton4.setAlignmentX(Component.RIGHT_ALIGNMENT);
        exitButton5 = new JButton("Exit");
        ButtonHandler exitButtonHandler5 = new ButtonHandler();
        exitButton5.addActionListener(exitButtonHandler5);
        exitButton5.setAlignmentX(Component.RIGHT_ALIGNMENT);
        bookTitleLabel = new JLabel();
        bookTitleLabel.setPreferredSize(new Dimension(800, 15));
        bookTitleLabel.setMaximumSize(bookTitleLabel.getPreferredSize());
        bookTitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        bookAuthorLabel = new JLabel();
        bookAuthorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        editCheckoutDateLabel = new JLabel("Checkout Date");
        editCheckoutDateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        editCheckoutDateErrorLabel = new JLabel("Invalid date format. Please format date as dd-MMM-yy.");
        editCheckoutDateErrorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        editCheckoutDateErrorLabel.setVisible(false);
        editCheckoutDateErrorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        editCheckoutDateField = new JTextField(10);
        editCheckoutDateField.setMaximumSize(editCheckoutDateField.getPreferredSize());
        editCheckoutDateField.setAlignmentX(Component.LEFT_ALIGNMENT);
        clearCheckoutButton = new JButton("Set As Available");
        ButtonHandler clearCheckoutButtonHandler = new ButtonHandler();
        clearCheckoutButton.addActionListener(clearCheckoutButtonHandler);
        clearCheckoutButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        editCheckoutTimeLabel = new JLabel("How long is this book checked out for?");
        editCheckoutTimeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        editCheckoutTimeErrorLabel1 = new JLabel("Please select the length of time this book is checked out for.");
        editCheckoutTimeErrorLabel1.setVisible(false);
        editCheckoutTimeErrorLabel1.setAlignmentX(Component.LEFT_ALIGNMENT);
        editCheckoutTimeErrorLabel2 = new JLabel("Please clear the checkout time selection if this book is no longer checked out or input a checkout date.");
        editCheckoutTimeErrorLabel2.setVisible(false);
        editCheckoutTimeErrorLabel2.setAlignmentX(Component.LEFT_ALIGNMENT);
        clearCheckoutTimeSelectionButton = new JButton("Clear Selection");
        ButtonHandler clearCheckoutTimeSelectionButtonHandler = new ButtonHandler();
        clearCheckoutTimeSelectionButton.addActionListener(clearCheckoutTimeSelectionButtonHandler);
        clearCheckoutTimeSelectionButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        editCheckoutOneWeekRadioButton = new JRadioButton("One Week");
        editCheckoutOneWeekRadioButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        editCheckoutTwoWeeksRadioButton = new JRadioButton("Two Weeks");
        editCheckoutTwoWeeksRadioButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        editCheckoutRadioButtonGroup = new ButtonGroup();
        editCheckoutRadioButtonGroup.add(editCheckoutOneWeekRadioButton);
        editCheckoutRadioButtonGroup.add(editCheckoutTwoWeeksRadioButton);
        saveButton = new JButton("Save");
        ButtonHandler saveButtonHandler = new ButtonHandler();
        saveButton.addActionListener(saveButtonHandler);
        saveButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Organization of the components for the edit book checkout panel
        editBoxLeft = Box.createVerticalBox();
        editBoxLeft.add(editLabel);
        editBoxLeft.add(bookTitleLabel);
        editBoxLeft.add(bookAuthorLabel);
        editBoxLeft.add(editCheckoutDateLabel);
        editBoxLeft.add(editCheckoutDateErrorLabel);
        editBoxLeft.add(editCheckoutDateField);
        editBoxLeft.add(clearCheckoutButton);
        editBoxLeft.add(editCheckoutTimeLabel);
        editBoxLeft.add(editCheckoutTimeErrorLabel1);
        editBoxLeft.add(editCheckoutTimeErrorLabel2);
        editBoxLeft.add(clearCheckoutTimeSelectionButton);
        editBoxLeft.add(editCheckoutOneWeekRadioButton);
        editBoxLeft.add(editCheckoutTwoWeeksRadioButton);
        editBoxLeft.add(saveButton);
        editBoxLeft.add(Box.createVerticalGlue());
        editBoxRight = Box.createVerticalBox();
        editBoxRight.add(dateLabel5);
        editBoxRight.add(returnToSearchButton4);
        editBoxRight.add(adminSignOutButton4);
        editBoxRight.add(exitButton5);
        editBoxRight.add(Box.createVerticalGlue());

        // Addition of the components to the edit book checkout panel
        editBox = Box.createHorizontalBox();
        editBox.add(editBoxLeft);
        editBox.add(Box.createHorizontalGlue());
        editBox.add(editBoxRight);
        editPanel.add(editBox);

        // Addition of the panels to the main panel
        application.add("searchView", searchPanel);
        application.add("checkoutView", checkoutPanel);
        application.add("viewView", viewPanel);
        application.add("adminLoginView", adminLoginPanel);
        application.add("editView", editPanel);
        cardLayout.show(application, "searchView");

        // Addition of the main panel to the graphical user interface frame
        add(application);
    }

    // Responses of the application for the various buttons in the interface
    private class ButtonHandler implements ActionListener {
        // Button press event cases
        @Override
        public void actionPerformed(ActionEvent event) {
            // Connects to a MySQL library book database with JDBC when a user searches for books by various criteria
            if (((JButton) event.getSource()).getText().matches("Search")) {
                if (connectedToDatabase == false) {
                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/books","root","password");
                        connectedToDatabase = true;
                        searchQuery();
                    }
                    catch (ClassNotFoundException classNotFoundException) {
                        throw new RuntimeException(classNotFoundException);
                    }
                    catch (SQLException sqlException) {
                        throw new RuntimeException(sqlException);
                    }
                }
                else {
                    searchQuery();
                }
            }
            // Closes the application when the user presses the exit button
            else if (((JButton) event.getSource()).getText().matches("Exit")) {
                LibraryManagementSystem.this.dispose();
            }
            // Checks out a book for the selected period of time when the user presses the checkout button and writes this information to the library book database
            else if (((JButton) event.getSource()).getText().matches("Checkout")) {
                if (checkoutRadioButtonGroup.getSelection() == null) {
                    checkoutTimeErrorLabel.setVisible(true);
                }
                else {
                    for (Enumeration<AbstractButton> checkoutRadioButtonGroupElements = checkoutRadioButtonGroup.getElements(); checkoutRadioButtonGroupElements.hasMoreElements(); ) {
                        AbstractButton checkoutSelectionButton = checkoutRadioButtonGroupElements.nextElement();

                        if (checkoutSelectionButton.isSelected()) {
                            try {
                                searchTableModel.getResultSet().absolute(searchResultsTable.getEditingRow() + 1);
                                searchTableModel.getResultSet().updateString(searchTableModel.getColumnCount() - 5, dateFormat2.format(date));
                                searchTableModel.getResultSet().updateString(searchTableModel.getColumnCount() - 4, checkoutSelectionButton.getText());
                                calendar.setTime(date);

                                if (checkoutSelectionButton.getText() == "One Week") {
                                    calendar.add(Calendar.DATE, 7);
                                } else if (checkoutSelectionButton.getText() == "Two Weeks") {
                                    calendar.add(Calendar.DATE, 14);
                                }

                                searchTableModel.getResultSet().updateString(searchTableModel.getColumnCount() - 3, dateFormat2.format(calendar.getTime()));
                                searchTableModel.getResultSet().updateString(searchTableModel.getColumnCount() - 2, "Checked Out");
                                searchTableModel.getResultSet().updateString(searchTableModel.getColumnCount() - 1, "View Details");
                                searchTableModel.getResultSet().updateRow();

                                cardLayout.show(application, "searchView");
                            } catch (SQLException sqlException) {
                                throw new RuntimeException(sqlException);
                            }
                        }
                    }
                }
            }
            // Returns to the search panel from whichever panel the user was viewing
            else if (((JButton) event.getSource()).getText().matches("Return To Search")) {
                cardLayout.show(application, "searchView");
            }
            // Shows the admin login panel where the user can sign in as an administrator
            else if (((JButton) event.getSource()).getText().matches("Admin Login")) {
                adminLoginErrorLabel.setVisible(false);
                adminUsernameField.setText("");
                adminPasswordField.setText("");

                cardLayout.show(application, "adminLoginView");
            }
            // Signs out as an administrator when the user presses the button to sign out
            else if (((JButton) event.getSource()).getText().matches("Sign Out")) {
                adminLogin = false;

                adminSignOutButton1.setVisible(false);
                adminLoginButton1.setVisible(true);
                adminSignOutButton2.setVisible(false);
                adminLoginButton2.setVisible(true);
                adminSignOutButton3.setVisible(false);
                adminLoginButton3.setVisible(true);

                if (connectedToDatabase == true) {
                    searchTableColumnModel.removeColumn(adminTableColumn1);
                    searchTableColumnModel.removeColumn(adminTableColumn2);
                    searchTableColumnModel.removeColumn(adminTableColumn3);
                    searchTableColumnModel.removeColumn(adminTableColumn4);
                    searchTableColumnModel.removeColumn(adminTableColumn5);
                }

                cardLayout.show(application, "searchView");
            }
            // Logs in as an administrator when the user presses the login button with the correct administrator credentials or shows an error message if the credentials are incorrect
            else if (((JButton) event.getSource()).getText().matches("Log In")) {
                if (adminUsernameField.getText().equals("admin") && adminPasswordField.getText().equals("admin")) {
                    adminLogin = true;

                    adminLoginErrorLabel.setVisible(false);
                    adminUsernameField.setText("");
                    adminPasswordField.setText("");
                    adminLoginButton1.setVisible(false);
                    adminSignOutButton1.setVisible(true);
                    adminLoginButton2.setVisible(false);
                    adminSignOutButton2.setVisible(true);
                    adminLoginButton3.setVisible(false);
                    adminSignOutButton3.setVisible(true);

                    if (connectedToDatabase == true) {
                        searchTableColumnModel.addColumn(adminTableColumn1);
                        searchTableColumnModel.addColumn(adminTableColumn2);
                        searchTableColumnModel.addColumn(adminTableColumn3);
                        searchTableColumnModel.addColumn(adminTableColumn4);
                        searchTableColumnModel.addColumn(adminTableColumn5);

                        searchTableColumnModel.moveColumn(searchTableModel.getColumnCount() - 5, searchTableModel.getColumnCount() - 16);
                        searchTableColumnModel.moveColumn(searchTableModel.getColumnCount() - 4, searchTableModel.getColumnCount() - 6);
                        searchTableColumnModel.moveColumn(searchTableModel.getColumnCount() - 3, searchTableModel.getColumnCount() - 5);
                        searchTableColumnModel.moveColumn(searchTableModel.getColumnCount() - 2, searchTableModel.getColumnCount() - 4);
                        searchTableColumnModel.moveColumn(searchTableModel.getColumnCount() - 1, searchTableModel.getColumnCount() - 1);

                        adminTableColumn1 = searchTableColumnModel.getColumn(searchTableModel.getColumnCount() - 16);
                        adminTableColumn2 = searchTableColumnModel.getColumn(searchTableModel.getColumnCount() - 6);
                        adminTableColumn3 = searchTableColumnModel.getColumn(searchTableModel.getColumnCount() - 5);
                        adminTableColumn4 = searchTableColumnModel.getColumn(searchTableModel.getColumnCount() - 4);
                        adminTableColumn5 = searchTableColumnModel.getColumn(searchTableModel.getColumnCount() - 1);
                    }

                    cardLayout.show(application, "searchView");
                }
                else {
                    adminLoginErrorLabel.setVisible(true);
                }
            }
            // Sets a library book as available for checkout when editing the checkout details for the book
            else if (((JButton) event.getSource()).getText().matches("Set As Available")) {
                editCheckoutDateErrorLabel.setVisible(false);
                editCheckoutDateField.setText("");
                editCheckoutTimeErrorLabel1.setVisible(false);
                editCheckoutTimeErrorLabel2.setVisible(false);
                editCheckoutRadioButtonGroup.clearSelection();
            }
            // Clears the checkout duration button selection when editing the checkout details for a book
            else if (((JButton) event.getSource()).getText().matches("Clear Selection")) {
                editCheckoutRadioButtonGroup.clearSelection();
            }
            // Saves the checkout details for a book when editing the checkout details
            else if (((JButton) event.getSource()).getText().matches("Save")) {
                boolean checkoutDateChanged = false;
                boolean checkoutTimeChanged = false;
                Date editCheckoutDate = null;
                String editCheckoutDateFieldText = editCheckoutDateField.getText();
                String editCheckoutTime = null;

                editCheckoutDateErrorLabel.setVisible(false);
                editCheckoutTimeErrorLabel1.setVisible(false);
                editCheckoutTimeErrorLabel2.setVisible(false);

                try {
                    searchTableModel.getResultSet().absolute(searchResultsTable.getEditingRow() + 1);

                    if (!editCheckoutDateFieldText.equals(searchTableModel.getResultSet().getString(searchTableModel.getColumnCount() - 5))) {
                        checkoutDateChanged = true;
                    }

                    for (Enumeration<AbstractButton> editCheckoutRadioButtonGroupElements = editCheckoutRadioButtonGroup.getElements(); editCheckoutRadioButtonGroupElements.hasMoreElements();) {
                        AbstractButton editCheckoutSelectionButton = editCheckoutRadioButtonGroupElements.nextElement();

                        if (editCheckoutSelectionButton.isSelected()) {
                            if (editCheckoutSelectionButton.getText() != searchTableModel.getResultSet().getString(searchTableModel.getColumnCount() - 4)) {
                                checkoutTimeChanged = true;
                                editCheckoutTime = editCheckoutSelectionButton.getText();
                            }
                        }
                    }

                    if (editCheckoutRadioButtonGroup.getSelection() == null && searchTableModel.getResultSet().getString(searchTableModel.getColumnCount() - 4) != "") {
                        checkoutTimeChanged = true;
                        editCheckoutTime = "";
                    }

                    if (!editCheckoutDateFieldText.equals("")) {
                        try {
                            editCheckoutDate = dateFormat2.parse(editCheckoutDateFieldText);

                            if (!editCheckoutDateFieldText.equals(dateFormat2.format(editCheckoutDate))) {
                                editCheckoutDate = null;
                            }
                        } catch (ParseException parseException) {
                            throw new RuntimeException(parseException);
                        }
                    }

                    if (checkoutDateChanged == true || checkoutTimeChanged == true) {
                        if (editCheckoutDate == null && !editCheckoutDateFieldText.equals("")) {
                            editCheckoutDateErrorLabel.setVisible(true);

                            if (editCheckoutRadioButtonGroup.getSelection() == null) {
                                editCheckoutTimeErrorLabel1.setVisible(true);
                            }
                        }
                        else if (editCheckoutDate != null && editCheckoutRadioButtonGroup.getSelection() == null) {
                            editCheckoutTimeErrorLabel1.setVisible(true);
                        } else if (editCheckoutDateFieldText.equals("") && editCheckoutRadioButtonGroup.getSelection() != null) {
                            editCheckoutTimeErrorLabel2.setVisible(true);
                        }

                        if (editCheckoutDateErrorLabel.isVisible() == false && editCheckoutTimeErrorLabel1.isVisible() == false && editCheckoutTimeErrorLabel2.isVisible() == false) {
                            if (!editCheckoutDateFieldText.equals("")) {
                                searchTableModel.getResultSet().updateString(searchTableModel.getColumnCount() - 5, editCheckoutDateFieldText);
                                searchTableModel.getResultSet().updateString(searchTableModel.getColumnCount() - 4, editCheckoutTime);
                                calendar.setTime(editCheckoutDate);

                                if (editCheckoutTime == "One Week") {
                                    calendar.add(Calendar.DATE, 7);
                                }
                                else if (editCheckoutTime == "Two Weeks") {
                                    calendar.add(Calendar.DATE, 14);
                                }

                                searchTableModel.getResultSet().updateString(searchTableModel.getColumnCount() - 3, dateFormat2.format(calendar.getTime()));
                                searchTableModel.getResultSet().updateString(searchTableModel.getColumnCount() - 2, "Checked Out");
                                searchTableModel.getResultSet().updateString(searchTableModel.getColumnCount() - 1, "View Details");
                            }
                            else {
                                searchTableModel.getResultSet().updateString(searchTableModel.getColumnCount() - 5, "");
                                searchTableModel.getResultSet().updateString(searchTableModel.getColumnCount() - 4, "");
                                searchTableModel.getResultSet().updateString(searchTableModel.getColumnCount() - 3, "");
                                searchTableModel.getResultSet().updateString(searchTableModel.getColumnCount() - 2, "Available");
                                searchTableModel.getResultSet().updateString(searchTableModel.getColumnCount() - 1, "Checkout");
                            }

                            searchTableModel.getResultSet().updateRow();

                            cardLayout.show(application, "searchView");
                        }
                    }
                }
                catch (SQLException sqlException) {
                    throw new RuntimeException(sqlException);
                }
            }
        }

        // Processes a query to the library book database to find matching results, stores the results, and shows a table of the results in the interface
        public void searchQuery () {
            try {
                String searchTerms = searchField.getText();
                String constructedQuery = constructQuery(searchTerms);
                statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                bookResults = statement.executeQuery(constructedQuery);
                searchTableModel.setResultSet(bookResults);
                searchTableModel.setMetaData(bookResults.getMetaData());
                searchTableModel.setNumberOfRows(bookResults);
                searchTableModel.fireTableStructureChanged();
                searchResultsTable.getColumn("Checkout").setCellRenderer(new ButtonRenderer());
                searchResultsTable.getColumn("Edit").setCellRenderer(new ButtonRenderer());

                if (initialSearch == true) {
                    searchTableColumnModel = searchResultsTable.getColumnModel();
                    initialSearch = false;
                }

                adminTableColumn1 = searchTableColumnModel.getColumn(searchTableModel.getColumnCount() - 16);
                adminTableColumn2 = searchTableColumnModel.getColumn(searchTableModel.getColumnCount() - 6);
                adminTableColumn3 = searchTableColumnModel.getColumn(searchTableModel.getColumnCount() - 5);
                adminTableColumn4 = searchTableColumnModel.getColumn(searchTableModel.getColumnCount() - 4);
                adminTableColumn5 = searchTableColumnModel.getColumn(searchTableModel.getColumnCount() - 1);

                if (adminLogin == false) {
                    searchTableColumnModel.removeColumn(adminTableColumn1);
                    searchTableColumnModel.removeColumn(adminTableColumn2);
                    searchTableColumnModel.removeColumn(adminTableColumn3);
                    searchTableColumnModel.removeColumn(adminTableColumn4);
                    searchTableColumnModel.removeColumn(adminTableColumn5);
                }

                searchResultsScrollPane.setVisible(true);
                getContentPane().validate();
                getContentPane().repaint();
            }
            catch (SQLException sqlException) {
                throw new RuntimeException(sqlException);
            }
        }

        // Constructs a query to the MySQL database depending on the user search terms
        public String constructQuery (String searchTerms) {
            String constructedQuery;

            if (searchTerms.equals("")) {
                constructedQuery = "SELECT * FROM books";
            }
            else if (attributeComboBox.getSelectedItem().toString() == "Title") {
                constructedQuery = "SELECT * FROM books WHERE title LIKE '%" + searchTerms + "%' COLLATE utf8mb4_general_ci";
            }
            else if (attributeComboBox.getSelectedItem().toString() == "Author") {
                constructedQuery = "SELECT * FROM books WHERE author LIKE '%" + searchTerms + "%' COLLATE utf8mb4_general_ci";
            }
            else if (attributeComboBox.getSelectedItem().toString() == "Author First Name") {
                constructedQuery = "SELECT * FROM books WHERE author_first_name LIKE '%" + searchTerms + "%' COLLATE utf8mb4_general_ci";
            }
            else if (attributeComboBox.getSelectedItem().toString() == "Author Last Name") {
                constructedQuery = "SELECT * FROM books WHERE author_last_name LIKE '%" + searchTerms + "%' COLLATE utf8mb4_general_ci";
            }
            else if (attributeComboBox.getSelectedItem().toString() == "Genre") {
                constructedQuery = "SELECT * FROM books WHERE genres LIKE '%" + searchTerms + "%' COLLATE utf8mb4_general_ci";
            }
            else {
                constructedQuery = "SELECT NULL LIMIT 0";
            }

            return constructedQuery;
        }
    }

    // Table model of the library book search results with various functions for retrieving and setting table model parameters and data
    private class SearchResultsTableModel extends DefaultTableModel {
        private ResultSet resultSet;
        private ResultSetMetaData resultSetMetaData;
        private int numberOfRows;

        // Returns for a specified cell in the table model that it is not editable
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

        // Returns the book result set from the book search
        public ResultSet getResultSet() {
            return resultSet;
        }

        // Returns the book result metadata from the book search
        public ResultSetMetaData getResultSetMetaData() {
            return resultSetMetaData;
        }

        // Returns the number of columns in the table model
        @Override
        public int getColumnCount() {
            try {
                if (resultSetMetaData != null) {
                    return resultSetMetaData.getColumnCount();
                }
                else {
                    return 0;
                }
            }
            catch (SQLException sqlException) {
                throw new RuntimeException(sqlException);
            }
        }

        // Returns the name of the input column in the table model
        @Override
        public String getColumnName(int column) {
            try {
                return resultSetMetaData.getColumnName(column + 1);
            }
            catch (SQLException sqlException) {
                throw new RuntimeException(sqlException);
            }
        }

        // Returns the number of rows in the table model
        @Override
        public int getRowCount() {
            return numberOfRows;
        }

        // Returns the value of the specified cell in the table model
        @Override
        public Object getValueAt(int row, int column) {
            try {
                resultSet.absolute(row + 1);
                return resultSet.getObject(column + 1);
            }
            catch (SQLException sqlException) {
                throw new RuntimeException(sqlException);
            }
        }

        // Sets the number of rows in the table model
        public void setNumberOfRows(ResultSet searchResultSet) {
            try {
                searchResultSet.last();
                numberOfRows = searchResultSet.getRow();
            }
            catch (SQLException sqlException) {
                throw new RuntimeException(sqlException);
            }
        }

        // Sets the book result set to the specified result set
        public void setResultSet(ResultSet searchResultSet) {
            resultSet = searchResultSet;
        }

        // Sets the book metadata to the specified metadata
        public void setMetaData(ResultSetMetaData searchResultMetaData) {
            resultSetMetaData = searchResultMetaData;
        }
    }

    // Renders and executes responses for the table buttons in the book search result table
    class ButtonRenderer extends JButton implements TableCellRenderer {
        private int tableColumn;

        // Constructor for the table button renderer
        public ButtonRenderer() {
            setOpaque(true);
        }

        // Returns specified table button and executes responses for selected table button
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (adminLogin == true) {
                tableColumn = column;
            }
            else {
                tableColumn = column + 4;
            }

            if (searchTableModel.getValueAt(row, tableColumn).toString().equals("Checkout")) {
                setVisible(true);
                setEnabled(true);
                setForeground(table.getForeground());
                setBackground(UIManager.getColor("Button.background"));
            }
            else if (searchTableModel.getValueAt(row, tableColumn).toString().equals("View Details")) {
                setVisible(true);
                setEnabled(true);
                setForeground(table.getForeground());
                setBackground(UIManager.getColor("Button.background"));
            }
            else if (searchTableModel.getValueAt(row, tableColumn).toString().equals("Edit")) {
                setVisible(true);
                setEnabled(true);
                setForeground(table.getForeground());
                setBackground(UIManager.getColor("Button.background"));
            }
            else {
                setEnabled(false);
                setVisible(false);
            }

            if (hasFocus) {
                // Shows book checkout panel if the checkout table button is pressed
                if (searchTableModel.getValueAt(row, tableColumn).toString().equals("Checkout")) {
                    searchResultsTable.setEditingRow(row);
                    searchResultsTable.clearSelection();
                    checkoutBookTitleLabel.setText("Book: " + searchTableModel.getValueAt(row, searchTableModel.getColumnCount() - 15).toString());
                    checkoutBookAuthorLabel.setText("Author: " + searchTableModel.getValueAt(row, searchTableModel.getColumnCount() - 14).toString());
                    checkoutTimeErrorLabel.setVisible(false);
                    checkoutRadioButtonGroup.clearSelection();
                    cardLayout.show(application, "checkoutView");
                }
                // Shows book checkout details panel if the view details table button is pressed
                else if (searchTableModel.getValueAt(row, tableColumn).toString().equals("View Details")) {
                    searchResultsTable.setEditingRow(row);
                    searchResultsTable.clearSelection();
                    viewBookTitleLabel.setText("Book: " + searchTableModel.getValueAt(row, searchTableModel.getColumnCount() - 15).toString());
                    viewBookAuthorLabel.setText("Author: " + searchTableModel.getValueAt(row, searchTableModel.getColumnCount() - 14).toString());
                    viewCheckoutDateLabel.setText("Checkout Date: " + searchTableModel.getValueAt(row, searchTableModel.getColumnCount() - 6).toString());
                    viewCheckoutTimeLabel.setText("Checkout Time: " + searchTableModel.getValueAt(row, searchTableModel.getColumnCount() - 5).toString());
                    viewCheckoutAvailableDateLabel.setText("Available Date: " + searchTableModel.getValueAt(row, searchTableModel.getColumnCount() - 4).toString());
                    cardLayout.show(application, "viewView");
                }
                // Shows edit book checkout details panel if the edit table button is pressed
                else if (searchTableModel.getValueAt(row, tableColumn).toString().equals("Edit")) {
                    searchResultsTable.setEditingRow(row);
                    searchResultsTable.clearSelection();
                    bookTitleLabel.setText("Book: " + searchTableModel.getValueAt(row, searchTableModel.getColumnCount() - 15).toString());
                    bookAuthorLabel.setText("Author: " + searchTableModel.getValueAt(row, searchTableModel.getColumnCount() - 14).toString());
                    editCheckoutDateErrorLabel.setVisible(false);
                    editCheckoutDateField.setText(searchTableModel.getValueAt(row, searchTableModel.getColumnCount() - 6).toString());
                    editCheckoutTimeErrorLabel1.setVisible(false);
                    editCheckoutTimeErrorLabel2.setVisible(false);

                    if (searchTableModel.getValueAt(row, searchTableModel.getColumnCount() - 5).toString().equals("One Week")) {
                        editCheckoutOneWeekRadioButton.setSelected(true);
                    }
                    else if (searchTableModel.getValueAt(row, searchTableModel.getColumnCount() - 5).toString().equals("Two Weeks")) {
                        editCheckoutTwoWeeksRadioButton.setSelected(true);
                    }
                    else {
                        editCheckoutRadioButtonGroup.clearSelection();
                    }

                    cardLayout.show(application, "editView");
                }
            }

            setText((value == null) ? "" : value.toString());

            return this;
        }
    }
}
