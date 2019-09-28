package com.greetings.desktop.adapters.ui;

import com.greetings.adapters.infrastructure.SimpleCommandBus;
import com.greetings.adapters.logging.ConsoleLogger;
import com.greetings.core.logging.Logger;
import com.greetings.core.usecases.EmployeeDTO;
import com.greetings.core.usecases.*;
import com.greetings.desktop.adapters.domain.DesktopGreetingsNotifier;
import com.greetings.desktop.adapters.domain.PlainTextEmployeeRepository;
import com.greetings.desktop.adapters.notification.SMTPEmailGateway;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.List;

public class MainWindow extends JFrame {

    private JButton sendGreetingsButton;
    private JTable employeesTable;
    private JButton selectDatabaseFileButton;
    private JPanel mainPanel;
    private JButton refreshButton;
    private DefaultTableModel tableModel;

    public MainWindow(CommandBus bus, StorageSettings storageSettings) {

        setTitle("Greetings Kata v1.0");
        setContentPane(mainPanel);

        tableModel = new DefaultTableModel();

        tableModel.addColumn("ID");
        tableModel.addColumn("First name");
        tableModel.addColumn("Last name");
        tableModel.addColumn("Email");
        tableModel.addColumn("Phone number");
        tableModel.addColumn("Birth date");
        tableModel.addColumn("Will receive an email?");

        employeesTable.setModel(tableModel);

        MainController controller = new MainController(this, bus, storageSettings);

        sendGreetingsButton.addActionListener(controller);
        selectDatabaseFileButton.addActionListener(controller);
        refreshButton.addActionListener(controller);
    }

    void showEmployees(List<EmployeeDTO> employees) {
        tableModel.setRowCount(0);
        for (EmployeeDTO it : employees)
            tableModel.addRow(new Object[]{it.getId(), it.getFirstName(), it.getLastName(), it.getEmail(),
                    it.getPhoneNumber(), it.getBirthDate(), it.getBirthdayToday() ? "Yes" : "No"});
    }

    void displayNow() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    void showErrorMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    void showInfoMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Info", JOptionPane.INFORMATION_MESSAGE);
    }


    File showFileSelector(File root) {
        JFileChooser fileChooser = new JFileChooser(root);
        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }

    public static void main(String[] args) {
        StorageSettings storageSettings = new StorageSettings();

        PlainTextEmployeeRepository repository = new PlainTextEmployeeRepository(storageSettings);
        Logger logging = new ConsoleLogger();

        SMTPEmailGateway emailGateway = new SMTPEmailGateway(logging);

        DesktopGreetingsNotifier notifier = new DesktopGreetingsNotifier(emailGateway);

        SimpleCommandBus commandBus = new SimpleCommandBus();
        commandBus.register(new FindAllUseCase(repository), new FindAllCommand());
        commandBus.register(new SendGreetingsUseCase(repository, notifier), new SendGreetingsCommand());

        MainWindow window = new MainWindow(commandBus, storageSettings);

        window.displayNow();
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        sendGreetingsButton = new JButton();
        sendGreetingsButton.setActionCommand("send");
        sendGreetingsButton.setIcon(new ImageIcon(getClass().getResource("/icons8-birthday-26.png")));
        sendGreetingsButton.setText("Send congrats");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(sendGreetingsButton, gbc);
        selectDatabaseFileButton = new JButton();
        selectDatabaseFileButton.setActionCommand("change_db");
        selectDatabaseFileButton.setIcon(new ImageIcon(getClass().getResource("/icons8-file-32.png")));
        selectDatabaseFileButton.setText("Select database file");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(selectDatabaseFileButton, gbc);
        final JScrollPane scrollPane1 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(scrollPane1, gbc);
        employeesTable = new JTable();
        employeesTable.setAutoResizeMode(2);
        scrollPane1.setViewportView(employeesTable);
        refreshButton = new JButton();
        refreshButton.setActionCommand("refresh");
        refreshButton.setIcon(new ImageIcon(getClass().getResource("/icons8-refresh-32.png")));
        refreshButton.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(refreshButton, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}