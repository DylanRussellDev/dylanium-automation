/*
 * Filename: EncryptionApp.java
 * Author: Dylan Russell
 * Purpose: A Java swing applet that accepts a string input and encrypts it to put into a properties file.
 */

package io.github.dylanrusselldev.utilities.applets;

import io.github.dylanrusselldev.utilities.filereaders.ReadConfigFile;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;

import javax.swing.JFrame;
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class EncryptionApp extends JPanel implements ActionListener {

    private static final ReadConfigFile propFile = new ReadConfigFile();

    private static final String OK = "OK";
    private final JFrame controlFrame;
    private final JPasswordField passwordField;

    public EncryptionApp(JFrame f) {
        controlFrame = f;
        passwordField = new JPasswordField(10);
        passwordField.setActionCommand(OK);
        passwordField.addActionListener(this);

        JLabel label = new JLabel("Type the password to encrypt: ");
        label.setLabelFor(passwordField);
        JComponent btnPane = createButtonPanel();

        JPanel textPane = new JPanel(new FlowLayout(FlowLayout.TRAILING));

        textPane.add(label);
        textPane.add(passwordField);
        add(textPane);
        add(btnPane);
    } // end constructor

    // Main method
    public static void main(String[] args) {
        createAndShowUI();
    } // end main()

    // Create the button panel
    protected JComponent createButtonPanel() {
        JPanel p = new JPanel(new GridLayout(0, 1));
        JButton encryptButton = new JButton("Encrypt");
        encryptButton.setActionCommand(OK);
        encryptButton.addActionListener(this);
        p.add(encryptButton);
        return p;
    } // end createButtonPanel()

    // Handle the actions
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if (OK.equals(cmd)) {

            StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
            encryptor.setAlgorithm(propFile.properties.getProperty("algorithm"));
            encryptor.setPassword(propFile.properties.getProperty("secretPass"));
            encryptor.setIvGenerator(new RandomIvGenerator());
            encryptor.setKeyObtentionIterations(Integer.parseInt(propFile.properties.getProperty("keyIterations")));

            char[] input = passwordField.getPassword();
            String in = String.valueOf(input);

            String encryptedPassword = encryptor.encrypt(in);

            JOptionPane.showInputDialog(controlFrame, "Encrypted Password", encryptedPassword);
            passwordField.selectAll();
            resetFocus();

        } else {
            JOptionPane.showMessageDialog(controlFrame, "Error Occured");
        } // end if-else
    } // end actionPerformed()

    // Reset focus to text box
    protected void resetFocus() {
        passwordField.requestFocusInWindow();
    } // end resetFocus()

    // Build the application UI and display
    private static void createAndShowUI() {
        JFrame frame = new JFrame("Encrypt a Password");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final EncryptionApp newContentPane = new EncryptionApp(frame);

        // Set content pane
        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);
        frame.addWindowListener(new WindowAdapter() {
            public void windowActivated(WindowEvent e) {
                newContentPane.resetFocus();
            } // end windowActivated
        });

        frame.pack();
        frame.setVisible(true);

        // Launch app in the center of screen
        frame.setLocationRelativeTo(null);
    } // end createAndShowUI
    
} // end Encryption.java
