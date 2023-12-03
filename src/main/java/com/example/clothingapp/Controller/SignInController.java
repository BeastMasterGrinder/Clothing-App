package com.example.clothingapp.Controller;
import com.example.clothingapp.Model.User;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SignInController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ChoiceBox<String> signinroleChoiceBox;

    @FXML
    private Button signInButton;

    @FXML
    void initialize() {
        signinroleChoiceBox.getItems().removeAll();

        signinroleChoiceBox.getItems().addAll("Customer", "Administrator", "Shop Owner");
        signinroleChoiceBox.setValue("Customer");
    }

    @FXML
    private void signInButtonClicked() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String role = signinroleChoiceBox.getValue();

        // Attempt to authenticate the user
        User authenticatedUser = User.login(username, password, role);

        if (authenticatedUser != null) {
            System.out.println("Already have an account with this User Name.");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Authentication failed.");
            alert.setContentText("Already have an account with this User Name.\nPlease try again.");

            // Add your sign-in logic here
        } else {
            System.out.println("Creating a new user...");
            // Sign up the user
            signUpUser(username, email, password, role);
            openAnotherPage();
            // Perform sign-in logic here
        }
    }
    @FXML
    private void openAnotherPage() {
        // Assuming you have a method to load the signup page.
        loadLogInPage();
    }
    private void loadLogInPage() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/clothingapp/LogIn.fxml"));

            Stage stage = (Stage) signInButton.getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load(), 600, 700);

            stage.setScene(scene);


            // Assuming your signup page controller has a method to set up initial data.
            LogInController loginPageController = fxmlLoader.getController();
            loginPageController.initialize();  // Adjust this line accordingly.

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception appropriately, e.g., show an error message.
        }
    }

    private void signUpUser(String username, String email, String password, String role) {
        // Create a new user and save to the database
        User newUser = User.signIn(username, email, password, role);
    }
}