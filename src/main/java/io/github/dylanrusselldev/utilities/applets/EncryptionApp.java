package io.github.dylanrusselldev.utilities.applets;

import io.github.dylanrusselldev.utilities.core.Constants;
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

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/*
 * Filename: EncryptionApp.java
 * Author: Dylan Russell
 * Purpose: A Java swing applet that accepts a string input and encrypts it to put into a properties file.
 */
public class EncryptionApp extends JPanel implements ActionListener {

    private static final ReadConfigFile readConfigFile = new ReadConfigFile(Constants.PROP_FILEPATH + "Automation.properties");

    private static final String OK = "OK";
    private final JFrame jFrame;
    private final JPasswordField jPasswordField;

    public EncryptionApp(JFrame f) {
        jFrame = f;
        jPasswordField = new JPasswordField(10);
        jPasswordField.setActionCommand(OK);
        jPasswordField.addActionListener(this);

        JLabel jLabel = new JLabel("Type the password to encrypt: ");
        jLabel.setLabelFor(jPasswordField);
        JComponent btnPane = createButtonPanel();

        JPanel jPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));

        jPanel.add(jLabel);
        jPanel.add(jPasswordField);
        add(jPanel);
        add(btnPane);
    }

    public static void main(String[] args) {
        createAndShowUI();
    }

    // Create the button panel
    protected JComponent createButtonPanel() {
        JPanel jPanel = new JPanel(new GridLayout(0, 1));
        JButton jButton = new JButton("Encrypt");
        jButton.setActionCommand(OK);
        jButton.addActionListener(this);
        jPanel.add(jButton);
        return jPanel;
    }

    // Handle the actions
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if (OK.equals(cmd)) {

            StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
            encryptor.setAlgorithm(readConfigFile.properties.getProperty("algorithm"));
            encryptor.setPassword(readConfigFile.properties.getProperty("secretPass"));
            encryptor.setIvGenerator(new RandomIvGenerator());
            encryptor.setKeyObtentionIterations(Integer.parseInt(readConfigFile.properties.getProperty("keyIterations")));

            char[] input = jPasswordField.getPassword();
            String in = String.valueOf(input);

            String encryptedPassword = encryptor.encrypt(in);

            JOptionPane.showInputDialog(jFrame, "Encrypted Password", encryptedPassword);
            jPasswordField.selectAll();
            resetFocus();

        } else {
            JOptionPane.showMessageDialog(jFrame, "Error Occured");
        }

    }

    // Reset focus to text box
    protected void resetFocus() {
        jPasswordField.requestFocusInWindow();
    }

    // Build the application UI and display
    private static void createAndShowUI() {
        JFrame frame = new JFrame("Encrypt a Password");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        final EncryptionApp encryptionApp = new EncryptionApp(frame);

        // Set content pane
        encryptionApp.setOpaque(true);
        frame.setContentPane(encryptionApp);
        frame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowActivated(WindowEvent e) {
                encryptionApp.resetFocus();
            }

        });

        frame.pack();
        frame.setVisible(true);

        // Launch app in the center of screen
        frame.setLocationRelativeTo(null);
    }

}
