package service;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import static constants.URLS.API_END_PONT;
import static constants.URLS.LOGIN_URL;

public class Login {

    private String username;
    private String password;

    private final String credentialsFilePath = System.getProperty("user.home") + File.separator + "credentials.json";

    public Login() {
        loadCredentials();
    }

    public void run() {
        if (this.username == null || this.password == null) {
            JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
            JLabel userLabel = new JLabel("Username:");
            JTextField userField = new JTextField(15);
            JLabel passLabel = new JLabel("Password:");
            JPasswordField passField = new JPasswordField(15);

            panel.add(userLabel);
            panel.add(userField);
            panel.add(passLabel);
            panel.add(passField);

            int option = JOptionPane.showConfirmDialog(null, panel, "SoftUni Login", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (option == JOptionPane.OK_OPTION) {
                this.username = userField.getText();
                this.password = new String(passField.getPassword());

                if (this.username.isEmpty() || this.password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Username and Password cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                saveCredentials(this.username, this.password);
            }
        }

        JSONObject json = new JSONObject();
        json.put("userName", this.username);
        json.put("password", this.password);
        json.put("rememberMe", false);

        HttpResponse<String> loginResponse = Unirest.post(LOGIN_URL)
                .header("Content-Type", "application/json")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36")
                .header("Accept-Language", "bg-BG,bg;q=0.9")
                .body(json.toString())
                .asString();

        if (loginResponse.getStatus() == 200) {
            String apiUrl = JOptionPane.showInputDialog(null, "Enter the URL of the judge:", "Judge URL", JOptionPane.QUESTION_MESSAGE);
            if (apiUrl == null || apiUrl.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "URL cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String[] splited = apiUrl.split("/");
            apiUrl = splited[splited.length - 2];
            if (apiUrl == null || apiUrl.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "URL is incorrect.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select the directory to save files");
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int userSelection = fileChooser.showSaveDialog(null);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File selectedDir = fileChooser.getSelectedFile();

                JDialog loadingDialog = new JDialog();
                loadingDialog.setTitle("Processing...");
                loadingDialog.setSize(400, 150);
                loadingDialog.setLocationRelativeTo(null);
                loadingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

                JPanel loadingPanel = new JPanel(new BorderLayout());
                JLabel statusLabel = new JLabel("Login successful! Creating files...", JLabel.CENTER);
                JProgressBar progressBar = new JProgressBar();
                progressBar.setIndeterminate(true);

                loadingPanel.add(statusLabel, BorderLayout.NORTH);
                loadingPanel.add(progressBar, BorderLayout.CENTER);
                loadingDialog.add(loadingPanel);
                loadingDialog.setVisible(true);

                String finalApiUrl = apiUrl;
                new Thread(() -> {
                    Download download = new Download(selectedDir.toString(), String.format(API_END_PONT, finalApiUrl, "true"));
                    download.download();
                    SwingUtilities.invokeLater(() -> {
                        statusLabel.setText("Completed! Files have been created.");
                        progressBar.setIndeterminate(false);
                        progressBar.setValue(100);
                    });

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    loadingDialog.dispose();
                }).start();

            }

        } else {
            JOptionPane.showMessageDialog(null, "Login failed. Please check your credentials.", "Error", JOptionPane.ERROR_MESSAGE);
            clearCredentials();
        }
    }

    private void saveCredentials(String username, String password) {
        try (FileWriter file = new FileWriter(credentialsFilePath)) {
            JSONObject json = new JSONObject();
            json.put("username", username);
            json.put("password", password);
            file.write(json.toString());
            System.out.println("Saving credentials to: " + credentialsFilePath);
            JOptionPane.showMessageDialog(null, "Working directory: " + credentialsFilePath);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Failed to save credentials", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadCredentials() {
        try (FileReader reader = new FileReader(credentialsFilePath)) {
            Scanner scanner = new Scanner(reader);
            StringBuilder jsonStr = new StringBuilder();
            while (scanner.hasNext()) {
                jsonStr.append(scanner.nextLine());
            }
            JSONObject json = new JSONObject(jsonStr.toString());
            this.username = json.getString("username");
            this.password = json.getString("password");
        } catch (IOException e) {
        }
    }

    private void clearCredentials() {
        File file = new File("credentials.json");
        if (file.exists()) {
            file.delete();
        }
    }
}
