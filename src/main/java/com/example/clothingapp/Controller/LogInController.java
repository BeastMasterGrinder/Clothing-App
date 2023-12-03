package com.example.clothingapp.Controller;
import com.example.clothingapp.Model.Administrator;
import com.example.clothingapp.Model.Customer;
import com.example.clothingapp.Model.StoreOwner;
import com.example.clothingapp.Model.User;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LogInController {


    //Log In fields
    @FXML
    private TextField usernameFieldLogIn;
    @FXML
    private PasswordField passwordFieldLogIn;
    @FXML
    private ChoiceBox<String> roleChoiceBox;

    @FXML
    private Button logInButton;

    @FXML
    void initialize() {
        roleChoiceBox.getItems().removeAll();
        roleChoiceBox.getItems().addAll("Customer", "Administrator", "Shop Owner");
        roleChoiceBox.setValue("Customer");
    }

    @FXML
    private void LogInButtonClicked() {
        String username = usernameFieldLogIn.getText();
        String password = passwordFieldLogIn.getText();

        String role = roleChoiceBox.getValue();

        // Attempt to authenticate the user
        User authenticatedUser = User.login(username, password, role);

        if (authenticatedUser != null) {
            System.out.println("User authenticated. Signing in...");
            // Add your sign-in logic here
            if(authenticatedUser instanceof Administrator){
                Administrator admin = (Administrator) authenticatedUser;
                System.out.println("Admin");
                loadAdminPage(admin);
            }
            else if(authenticatedUser instanceof Customer){
                Customer customer = (Customer) authenticatedUser;
                System.out.println("Customer");
                loadCustomerPage(customer);
            }
            else if(authenticatedUser instanceof StoreOwner){
                StoreOwner storeOwner = (StoreOwner) authenticatedUser;
                System.out.println("Store Owner");
                loadStoreOwner(storeOwner);
            }
        } else {
            System.out.println("Authentication failed. Creating a new user...");
            // Sign up the user
            // Alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Authentication failed.");
            //Open the SignIn.fxml

        }
    }
    @FXML
    private void openAnotherPage() {
        // Assuming you have a method to load the signup page.
        loadSignupPage();
    }
    private void loadSignupPage() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/clothingapp/SignIn.fxml"));

            Stage stage = (Stage) logInButton.getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load(), 600, 700);

            stage.setScene(scene);


            // Assuming your signup page controller has a method to set up initial data.
            SignInController signupPageController = fxmlLoader.getController();
            signupPageController.initialize();  // Adjust this line accordingly.

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception appropriately, e.g., show an error message.
        }
    }

    private void loadAdminPage(User user){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/clothingapp/Admin.fxml"));

            Stage stage = (Stage) logInButton.getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load(), 600, 700);

            AdminController adminController = fxmlLoader.getController();
            adminController.initialize(user);

            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadCustomerPage(User user){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/clothingapp/Customer.fxml"));

            Stage stage = (Stage) logInButton.getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load(), 600, 700);

            CustomerController customerController = fxmlLoader.getController();
            customerController.initialize(user);

            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadStoreOwner(User user){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/clothingapp/StoreOwner.fxml"));

            Stage stage = (Stage) logInButton.getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load(), 600, 700);

            StoreOwnerController storeOwnerController = fxmlLoader.getController();
            storeOwnerController.initialize(user);


            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void signUpUser(String username, String email, String password, String role) {
        // Create a new user and save to the database
        User newUser = User.signIn(username, email, password, role);
    }
}