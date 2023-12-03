package com.example.clothingapp.Controller;

import com.example.clothingapp.Model.Administrator;
import com.example.clothingapp.Model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminController {

    @FXML
    private TextField taxrateTextField;

    @FXML
    private ChoiceBox<String> currencyChoiceBox;

    @FXML
    private Button setSettingsButton;

    @FXML
    private Button backButton;


    Administrator admin;

    @FXML
    public void initialize(User user) {
        currencyChoiceBox.getItems().addAll("USD$", "EUR€", "GBP£", "CAD$", "AUD$", "JPY¥", "CNY¥");
        currencyChoiceBox.setValue("USD$");

        admin = (Administrator) user;
        setSettingsButton.setOnAction(event -> {
            setSettings();
        });
    }

    @FXML
    private void setSettings(){
        String taxrate = taxrateTextField.getText();
        String currency = currencyChoiceBox.getValue();

        if (taxrate.isEmpty() || currency.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Please fill in all fields");
            alert.showAndWait();
        } else {
            // Set taxrate and currency
            //validate taxrate is double
            try{
                double tax = Double.parseDouble(taxrate);
            } catch (NumberFormatException e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error");
                alert.setContentText("Please enter a valid tax rate");
                alert.showAndWait();
            }
            admin.setSystemSettings(Double.parseDouble(taxrate), currency);
        }
    }

    @FXML
    private void CurrentSettings(){
        taxrateTextField.setText(String.valueOf(admin.getSystemSettings().getTaxRate()));
        currencyChoiceBox.setValue(admin.getSystemSettings().getCurrency());
        //Alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Current Settings");
        alert.setHeaderText("Current Settings");
        alert.setContentText("Tax Rate: " + admin.getSystemSettings().getTaxRate() + "\nCurrency: " + admin.getSystemSettings().getCurrency());
        alert.showAndWait();

    }
    @FXML
    private void goBack(){
        logOut();
    }
    @FXML
    private void logOut(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/clothingapp/LogIn.fxml"));

            Stage stage = (Stage) backButton.getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load(), 600, 700);

            stage.setScene(scene);


            // Assuming your signup page controller has a method to set up initial data.
            LogInController logInController = fxmlLoader.getController();
            logInController.initialize();  // Adjust this line accordingly.

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception appropriately, e.g., show an error message.
        }
    }
}
